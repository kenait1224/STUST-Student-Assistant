package FragmentPage.News.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import Data.CrawlerData.BulletinData.BulletinInfo;
import Data.CrawlerData.Common.Detail;
import Data.StaticData.UserBulletinInfo;
import FontRatio.Ratio_Loading_Dialog;
import FontRatio.Ratio_News;
import FragmentPage.News.Dialog.NewsDetailDialogFragment;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import RecyclerViewController.News.NewsLoadingRecyclerAdapter;
import WebCrawler.Thread.NewsPage.NewsDialogCrawlerInterface;
import WebCrawler.Thread.NewsPage.NewsDialogCrawlerThread;
import WebCrawler.Thread.NewsPage.NewsLoadMoreInterface;
import WebCrawler.Thread.NewsPage.NewsLoadMoreThread;
import activity.R;

public class NewsListFragment extends Fragment {
    private View view;
    RecyclerView recyclerView;
    NewsLoadingRecyclerAdapter newsLoadingRecyclerAdapter;
    NewsLoadMoreThread newsLoadMoreThread;
    Ratio_News ratioNews;
    private Handler NewsFragmentHandler;
    private int Page = 2;
    private int Group;
    private int DisplayWidth;
    private int DisplayHeight;
    private float Density;
    boolean isLoading = false;


    public NewsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Density = getArguments().getFloat("Density");
        Group =  getArguments().getInt("Group");
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        ratioNews = new Ratio_News(DisplayWidth, DisplayHeight);
        NewsFragmentHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list_view,container,false);
        Page = (int) (Math.ceil((float)UserBulletinInfo.getBulletin(Group).size()/20)+1);
        recyclerView = (RecyclerView) view.findViewById(R.id.news_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsLoadingRecyclerAdapter = new NewsLoadingRecyclerAdapter(ratioNews.getNewsContentHeight(),Group,Density);
        newsLoadingRecyclerAdapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                String target = (view.findViewById(R.id.News_card)).getTag().toString();
                final int Group_idx = Integer.parseInt(target.split(",")[0]);
                int Child_idx = Integer.parseInt(target.split(",")[1]);
                final BulletinInfo bulletinInfo = UserBulletinInfo.getBulletin(Group_idx).get(Child_idx);
                if (Group_idx != 2) {
                    try {
                        final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(getContext(), new Ratio_Loading_Dialog(DisplayWidth, DisplayHeight), Density);
                        loadingDialogBar.show();
                        NewsDialogCrawlerThread courseDialogCrawlerThread = new NewsDialogCrawlerThread(new NewsDialogCrawlerInterface() {
                            @Override
                            public void CrawlerLoading(final int text) {
                                NewsFragmentHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                                        LoadingText.setText(text);
                                    }
                                });
                            }

                            @Override
                            public void CrawlerSuccessful(final Detail detail) {
                                NewsFragmentHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialogBar.dismiss();
                                        NewsDetailDialogFragment newsDetailDialogFragment = new NewsDetailDialogFragment(Group,DisplayWidth, DisplayHeight, Density, detail);
                                        newsDetailDialogFragment.show(getFragmentManager(), "news");
                                    }
                                });
                            }

                            @Override
                            public void CrawlerFailed() {
                                loadingDialogBar.dismiss();
                            }
                        },Group, bulletinInfo);
                        courseDialogCrawlerThread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.print("NewsListFragment Crawler Failed");
                    }
                }
                else{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(bulletinInfo.getHref()));
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });
        recyclerView.setAdapter(newsLoadingRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        initScrollListener();
        return view;
    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == UserBulletinInfo.getBulletin(Group).size() - 1 &&  UserBulletinInfo.getLoadable(Group)) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        UserBulletinInfo.getBulletin(Group).add(null);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                newsLoadingRecyclerAdapter.notifyItemInserted(UserBulletinInfo.getBulletin(Group).size() - 1);
            }
        });
        newsLoadMoreThread = new NewsLoadMoreThread(new NewsLoadMoreInterface(){
            @Override
            public void CrawlerSuccessful() {
                NewsFragmentHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        newsLoadingRecyclerAdapter.notifyItemRemoved(UserBulletinInfo.getBulletin(Group).size());
                        newsLoadingRecyclerAdapter.notifyDataSetChanged();
                        Page += 1;
                        isLoading = false;
                    }
                });
            }

            @Override
            public void CrawlerFailed() {
                NewsFragmentHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        newsLoadingRecyclerAdapter.notifyItemRemoved(UserBulletinInfo.getBulletin(Group).size());
                        newsLoadingRecyclerAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                });
            }
        }, Group, Page);
        newsLoadMoreThread.start();
    }

    @Override
    public void onDestroy() {
        System.out.println("detail destory");
        super.onDestroy();
    }
}
