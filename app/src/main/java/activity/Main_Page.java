package activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import ApplicationDialog.FailedDialog.LoginFailedMessage;
import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import ApplicationDialog.OfflineModeDialog.OfflineModeAsked;
import Data.StaticData.UserPersonalInfo;
import FontRatio.Ratio_Error_Dialog;
import FontRatio.Ratio_Loading_Dialog;
import FontRatio.Ratio_Offline_Mode_Dialog;
import FontRatio.Ratio_Page_Main;
import FontSet.FontSetter;
import Data.StaticData.UserConfig;
import Data.StaticData.UserCurriculumInfo;
import Data.StaticData.UserLoginInfo;
import WebCrawler.Network.NetworkState;
import WebCrawler.Thread.LoginPage.LoginCrawlerInterface;
import WebCrawler.Thread.LoginPage.LoginCrawlerThread;
import pub.devrel.easypermissions.EasyPermissions;

public class Main_Page extends Activity {


    LoadingDialogBar loadingDialogBar;
    LoginFailedMessage failedMessage;
    Handler MainPage;
    DisplayMetrics Display;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainPage = new Handler();
        setContentView(R.layout.activity_main_page);
        getPermissions();
        getUserConfig();
        Composing();
        System.out.println("main is create!");

    }


    private void getPermissions() {
        SharedPreferences PermissionsSetting = getSharedPreferences("PermissionsChecked", MODE_PRIVATE);
        if (PermissionsSetting.getAll().size() == 0) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    !EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                EasyPermissions.requestPermissions(this, "下載附件需要儲存權限，是否同意此應用程式取得權限?", 1, permissions);
            }
            PermissionsSetting.edit().putString("Asked", "true").commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckUserInfo();
    }


    protected void Composing() {
        ConstraintLayout HomePage = (ConstraintLayout) findViewById(R.id.Logged_Page), Logo;
        ImageView Background, Logo_Text;
        Button Login_Button, About_Button;
        Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        Ratio_Page_Main Font_Std = new Ratio_Page_Main(Display.widthPixels, Display.heightPixels);
        Login_Button = (Button) findViewById(R.id.login_button);
        About_Button = (Button) findViewById(R.id.about_button);
        Background = (ImageView) findViewById(R.id.background);
        Logo_Text = (ImageView) findViewById(R.id.stust_logo_text);
        Logo = (ConstraintLayout) findViewById(R.id.stust_logo);
        FontSetter.SetButtonSize(Login_Button, Font_Std.getButtonWidth(), Font_Std.getButtonHeight());
        FontSetter.SetButtonSize(About_Button, Font_Std.getButtonWidth(), Font_Std.getButtonHeight());
        FontSetter.SetImageViewSize(Background, Font_Std.getBackgroundWidth(), Font_Std.getBackgroundHeight());
        FontSetter.SetImageViewSize(Logo_Text, Font_Std.getLogoWidth(), Font_Std.getLogoTextHeight());
        FontSetter.SetConstrainLayoutSize(Logo, Font_Std.getLogoWidth(), Font_Std.getLogoHeight());
        FontSetter.resizeTextfont(HomePage, Display.density);
        System.out.println(Display.density);
    }


    public void CheckUserInfo() {
        final SharedPreferences User_Data = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences User_Personal = getSharedPreferences("UserPersonalInfo", MODE_PRIVATE);
        UserLoginInfo.ReadUserLoginInfo(User_Data);
        UserPersonalInfo.ReadUserPersonalInfo(User_Personal);
        if (UserLoginInfo.getUser().getUsername() == "Null" || UserLoginInfo.getUser().getPassword() == "Null")
            return;
        getCurriculumInfo();
        OfflineModeAsked offlineModeAsked = new OfflineModeAsked(this, new Ratio_Offline_Mode_Dialog(Display.widthPixels, Display.heightPixels), Display.density,UserConfig.getMyConfig().get("OfflineMode"));
        if (NetworkState.haveInternet(this) && UserConfig.getMyConfig().get("OfflineMode") == 0) {
            try {
                loadingDialogBar = new LoadingDialogBar(this, new Ratio_Loading_Dialog(Display.widthPixels, Display.heightPixels), Display.density);
                failedMessage = new LoginFailedMessage(this, new Ratio_Error_Dialog(Display.widthPixels, Display.heightPixels), Display.density);
                loadingDialogBar.show();
                LoginCrawlerThread crawlerThread = new LoginCrawlerThread(new LoginCrawlerInterface() {
                    @Override
                    public void CrawlerLoading(final int text) {
                        MainPage.post(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingDialogBar != null) {
                                    TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                                    LoadingText.setText(text);
                                }
                            }
                        });
                    }

                    @Override
                    public void CrawlerSuccessful() {
                        MainPage.post(new Runnable() {
                            @Override
                            public void run() {
                                DestroyDialog();
                                Intent intent = new Intent(Main_Page.this, UserInterface_Page.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("ActivityID", UserConfig.getMyConfig().get("StartPage"));
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                            }
                        });
                    }

                    @Override
                    public void CrawlerFailed() {
                        MainPage.post(new Runnable() {
                            @Override
                            public void run() {
//                                User_Data.edit().clear().commit();
                                loadingDialogBar.dismiss();
                                failedMessage.show();
                            }
                        });
                    }
                });
                crawlerThread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            offlineModeAsked.show();
        }
    }

    private void getCurriculumInfo() {
        SharedPreferences User_Curriculum = getSharedPreferences("UserCurriculum", MODE_PRIVATE);
        UserCurriculumInfo.ReadUserCurriculumInfo(User_Curriculum);
    }

    private void getUserConfig() {
        SharedPreferences User_Config = getSharedPreferences("UserConfig", MODE_PRIVATE);
        UserConfig.ReadLocalConfig(User_Config);
    }

    public void Login(View view) {
        Intent intent = new Intent(Main_Page.this, Login_Page.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void About(View view) {
        Intent intent = new Intent(Main_Page.this, About_Page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void DestroyDialog() {
        if (loadingDialogBar != null) {
            if (loadingDialogBar.isShowing()) { //check if dialog is showing.

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) loadingDialogBar.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                        loadingDialogBar.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    loadingDialogBar.dismiss();
            }
            loadingDialogBar = null;
        }
    }

    @Override
    protected void onPause() {
        DestroyDialog();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DestroyDialog();
        super.onDestroy();
    }
}