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
import FontRatio.Ratio_Loading_Dialog;
import FontRatio.Ratio_News;
import FragmentPage.News.Dialog.NewsDetailDialogFragment;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import RecyclerViewController.News.NewsGroupRecyclerAdapter;
import RecyclerViewController.News.NewsSpacesDecoration;
import Data.CrawlerData.BulletinData.BulletinInfo;
import Data.CrawlerData.Common.Detail;
import Data.StaticData.UserBulletinInfo;
import WebCrawler.Thread.NewsPage.NewsDialogCrawlerInterface;
import WebCrawler.Thread.NewsPage.NewsDialogCrawlerThread;
import activity.News_Page;
import activity.R;

public class NewsFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    Ratio_News ratioNews;
    private Handler NewsFragmentHandler;
    private int DisplayWidth;
    private int DisplayHeight;
    private int Source;
    private float Density;


    public NewsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsFragmentHandler = new Handler();
        Source = getArguments().getInt("Source");
        Density = getArguments().getFloat("Density");
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        ratioNews = new Ratio_News(DisplayWidth, DisplayHeight);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.News_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        NewsGroupRecyclerAdapter newsGroupRecyclerAdapter = new NewsGroupRecyclerAdapter(getContext(), Density, ratioNews.getNewsContentHeight());
        newsGroupRecyclerAdapter.setShowAllListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                String target = (view.findViewById(R.id.news_group_show_all)).getTag().toString();
                Intent intent = new Intent(getActivity(), News_Page.class);
                intent.putExtra("Target_Index", Integer.parseInt(target));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });
        newsGroupRecyclerAdapter.setItemClickListener(new OnRecyclerViewClickListener() {
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
                                        NewsDetailDialogFragment newsDetailDialogFragment = new NewsDetailDialogFragment(Source, DisplayWidth, DisplayHeight, Density, detail);
                                        newsDetailDialogFragment.show(getFragmentManager(), "news");
                                    }
                                });
                            }

                            @Override
                            public void CrawlerFailed() {
                                loadingDialogBar.dismiss();
                            }
                        }, Group_idx, bulletinInfo);
                        courseDialogCrawlerThread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.print("News Detail Dialog Crawler Failed");
                    }
                } else {
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
        recyclerView.addItemDecoration(new NewsSpacesDecoration((int) (10 * Density)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsGroupRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        newsGroupRecyclerAdapter.notifyDataSetChanged();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
