package FragmentPage.Offline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import ApplicationDialog.FailedDialog.LoginFailedMessage;
import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import Data.StaticData.UserConfig;
import FontRatio.Ratio_Error_Dialog;
import FontRatio.Ratio_Loading_Dialog;
import FontSet.FontSetter;
import WebCrawler.Thread.LoginPage.LoginCrawlerInterface;
import WebCrawler.Thread.LoginPage.LoginCrawlerThread;
import activity.R;

import static android.content.Context.MODE_PRIVATE;

public class OfflineFragment extends Fragment {
    View view;
    float Density;
    Handler Offline;
    int DisplayWidth;
    int DisplayHeight;

    public OfflineFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Density = getArguments().getFloat("Density");
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        Offline = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        UserConfig.getMyConfig().put("CurriculumSynchronize",0);
//        SharedPreferences User_Config = getActivity().getSharedPreferences("UserConfig", MODE_PRIVATE);
//        UserConfig.SaveLocalConfig(User_Config);
        view = inflater.inflate(R.layout.fragment_offline,container,false);
        ConstraintLayout constraintLayout = view.findViewById(R.id.offline_fragment_layout);
        Button Reconnect = view.findViewById(R.id.offline_fragment_reconnect);
        Reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(getContext(),new Ratio_Loading_Dialog(DisplayWidth,DisplayHeight),Density);
                final LoginFailedMessage failedMessage = new LoginFailedMessage(getContext() , new Ratio_Error_Dialog(DisplayWidth, DisplayHeight),Density);
                loadingDialogBar.show();
                LoginCrawlerThread crawlerThread = new LoginCrawlerThread(new LoginCrawlerInterface() {
                    @Override
                    public void CrawlerLoading(final int text) {
                        Offline.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                                LoadingText.setText(text);
                            }
                        });
                    }

                    @Override
                    public void CrawlerSuccessful() {
                        Offline.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialogBar.dismiss();
                                UserConfig.setMyConfig("OfflineMode",0);
                                SharedPreferences User_Config = getActivity().getSharedPreferences("UserConfig", MODE_PRIVATE);
                                UserConfig.SaveLocalConfig(User_Config);
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                                getActivity().overridePendingTransition(0,0);
                            }
                        });
                    }


                    @Override
                    public void CrawlerFailed() {
                        Offline.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialogBar.dismiss();
                                failedMessage.show();
                            }
                        });
                    }
                });
                crawlerThread.start();
            }
        });
        FontSetter.resizeTextfont(constraintLayout,Density);
        return view;
    }


}
