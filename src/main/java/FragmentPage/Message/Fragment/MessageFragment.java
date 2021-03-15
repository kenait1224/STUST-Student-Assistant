package FragmentPage.Message.Fragment;

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
import Data.CrawlerData.Common.Detail;
import Data.CrawlerData.MessageData.MessageInfo;
import Data.StaticData.UserBulletinInfo;
import Data.StaticData.UserMessageInfo;
import FontRatio.Ratio_Loading_Dialog;
import FontRatio.Ratio_Message;
import FragmentPage.Message.Dialog.MessageDetailDialogFragment;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import RecyclerViewController.Message.MessageLoadingRecyclerAdapter;
import RecyclerViewController.Message.MessageSpacesDecoration;
import WebCrawler.Thread.MessagePage.MessageDialogCrawlerInterface;
import WebCrawler.Thread.MessagePage.MessageDialogCrawlerThread;
import WebCrawler.Thread.MessagePage.MessageLoadMoreInterface;
import WebCrawler.Thread.MessagePage.MessageLoadMoreThread;
import activity.R;

public class MessageFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    Ratio_Message ratioMessage;
    MessageLoadingRecyclerAdapter messageLoadingRecyclerAdapter;
    private Handler MessageFragmentHandler;
    private int DisplayWidth;
    private int DisplayHeight;
    private int Source;
    private float Density;
    private int Page;
    boolean isLoading = false;


    public MessageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageFragmentHandler = new Handler();
        Source = getArguments().getInt("Source",0);
        Density = getArguments().getFloat("Density");
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        ratioMessage = new Ratio_Message(DisplayWidth, DisplayHeight);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        Page = (int) (Math.ceil((float)UserMessageInfo.getMessage(Source).size()/20)+1);
        recyclerView = view.findViewById(R.id.Message_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        messageLoadingRecyclerAdapter = new MessageLoadingRecyclerAdapter(ratioMessage.getMessageContentHeight(),Source,Density);
        messageLoadingRecyclerAdapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                String target = view.getTag().toString();
                final int Group_idx = Integer.parseInt(target.split(",")[0]);
                int Child_idx = Integer.parseInt(target.split(",")[1]);
                final MessageInfo messageInfo = UserMessageInfo.getMessage(Group_idx).get(Child_idx);
                try {
                    final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(getContext(), new Ratio_Loading_Dialog(DisplayWidth, DisplayHeight), Density);
                    loadingDialogBar.show();
                    MessageDialogCrawlerThread courseDialogCrawlerThread = new MessageDialogCrawlerThread(new MessageDialogCrawlerInterface() {
                        @Override
                        public void CrawlerLoading(final int text) {
                            MessageFragmentHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                                    LoadingText.setText(text);
                                }
                            });
                        }

                        @Override
                        public void CrawlerSuccessful(final Detail detail) {
                            MessageFragmentHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialogBar.dismiss();
                                    MessageDetailDialogFragment messageDetailDialogFragment = new MessageDetailDialogFragment(Source,DisplayWidth, DisplayHeight, Density, detail);
                                    messageDetailDialogFragment.show(getFragmentManager(), "news");
                                }
                            });
                        }

                        @Override
                        public void CrawlerFailed() {
                            loadingDialogBar.dismiss();
                        }
                    },Source,messageInfo );
                    courseDialogCrawlerThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("Course Detail Dialog Crawler Failed");
                }
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(messageLoadingRecyclerAdapter);
        recyclerView.addItemDecoration(new MessageSpacesDecoration((int)(5*Density)));
        messageLoadingRecyclerAdapter.notifyDataSetChanged();
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == UserBulletinInfo.getBulletin(Source).size() - 1 &&  UserBulletinInfo.getLoadable(Source)) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        UserBulletinInfo.getBulletin(Source).add(null);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                messageLoadingRecyclerAdapter.notifyItemInserted(UserBulletinInfo.getBulletin(Source).size() - 1);
            }
        });
        MessageLoadMoreThread messageLoadMoreThread = new MessageLoadMoreThread(new MessageLoadMoreInterface(){
            @Override
            public void CrawlerSuccessful() {
                MessageFragmentHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        messageLoadingRecyclerAdapter.notifyItemRemoved(UserBulletinInfo.getBulletin(Source).size());
                        messageLoadingRecyclerAdapter.notifyDataSetChanged();
                        Page += 1;
                        isLoading = false;
                    }
                });
            }

            @Override
            public void CrawlerFailed() {
                MessageFragmentHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        messageLoadingRecyclerAdapter.notifyItemRemoved(UserBulletinInfo.getBulletin(Source).size());
                        messageLoadingRecyclerAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                });
            }
        }, Source, Page);
        messageLoadMoreThread.start();
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
