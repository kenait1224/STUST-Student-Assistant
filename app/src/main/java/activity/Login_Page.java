package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import ApplicationDialog.FailedDialog.LoginFailedMessage;
import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import Data.StaticData.UserPersonalInfo;
import FontRatio.Ratio_Error_Dialog;
import FontRatio.Ratio_Loading_Dialog;
import FontRatio.Ratio_Page_Login;
import FontSet.FontSetter;
import Data.CrawlerData.Common.User;
import Data.StaticData.UserConfig;
import Data.StaticData.UserCurriculumInfo;
import Data.StaticData.UserLoginInfo;
import WebCrawler.Thread.LoginPage.LoginCrawlerInterface;
import WebCrawler.Thread.LoginPage.LoginCrawlerThread;

public class Login_Page extends AppCompatActivity {

    Handler login_page;
    DisplayMetrics Display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_page = new Handler();
        setContentView(R.layout.activity_login_page);
        this.Composing();
        this.EditTextSetting();
        System.out.println("login is create!");
    }

    public void EditTextSetting(){
        ArrayList<EditText> EditList = new ArrayList<>();
        EditText UsernameEdit = (EditText) findViewById(R.id.login_username);
        EditText PasswordEdit = (EditText) findViewById(R.id.login_password);
        EditList.add(UsernameEdit);
        EditList.add(PasswordEdit);
        for( EditText editText:EditList){
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                    else{
                        TextView failed = (TextView)findViewById(R.id.failed_login);
                        failed.setText("");
                    }
                }
            });
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void Composing() {
        Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        Ratio_Page_Login Font_Std = new Ratio_Page_Login(Display.widthPixels, Display.heightPixels);
        ConstraintLayout HomePage = (ConstraintLayout) findViewById(R.id.Loin_Page);
        LinearLayout Input_Layout = (LinearLayout) findViewById(R.id.input_layout);
        LinearLayout Submit_Layout = (LinearLayout) findViewById(R.id.submit_layout);
        ImageButton Union_Button = (ImageButton) findViewById(R.id.login_page_union);
        TextView Login_Page_Title = (TextView) findViewById(R.id.login_title);
        FontSetter.SetTextViewSize(Login_Page_Title, Font_Std.getLoginTextWidth(), Font_Std.getLoginTextHeight());
        FontSetter.SetImageButtonSize(Union_Button, Font_Std.getUnionWidth(), Font_Std.getUnionHeight());
        FontSetter.SetLinearLayoutSize(Input_Layout, Font_Std.getRectangleWidth(), Font_Std.getRectangleHeight());
        FontSetter.SetLinearLayoutSize(Submit_Layout, Font_Std.getSubmitWidth(), Font_Std.getSubmitHeight());
        FontSetter.resizeTextfont(HomePage, Display.density);
    }

    public void Registration(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://webap.stust.edu.tw/pwd/enablepwd.aspx"));
        startActivity(intent);
    }

    public void Union(View view) {
        Intent intent = new Intent(Login_Page.this, Main_Page.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void Submit(View view) {
        String username = ((EditText)findViewById(R.id.login_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.login_password)).getText().toString();
        UserLoginInfo.setUser(new User(username,password));

       try {
           final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(this,new Ratio_Loading_Dialog(Display.widthPixels,Display.heightPixels),Display.density);
           final LoginFailedMessage failedMessage = new LoginFailedMessage(this , new Ratio_Error_Dialog(Display.widthPixels, Display.heightPixels),Display.density);
           loadingDialogBar.show();
           LoginCrawlerThread crawlerThread = new LoginCrawlerThread(new LoginCrawlerInterface() {
               @Override
               public void CrawlerLoading(final int text) {
                   login_page.post(new Runnable() {
                       @Override
                       public void run() {
                           TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                           LoadingText.setText(text);
                       }
                   });
               }

               @Override
               public void CrawlerSuccessful() {
                   login_page.post(new Runnable() {
                       @Override
                       public void run() {
                           loadingDialogBar.dismiss();
                           SaveUserLocalInfo();
                           Intent intent = new Intent(Login_Page.this, UserInterface_Page.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                           intent.putExtra("ActivityID" , UserConfig.getMyConfig().get("StartPage"));
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                       }
                   });
               }


               @Override
               public void CrawlerFailed() {
                   login_page.post(new Runnable() {
                       @Override
                       public void run() {
                           loadingDialogBar.dismiss();
                           failedMessage.show();
                           TextView failed = (TextView)findViewById(R.id.failed_login);
                           failed.setText(R.string.FailedMessage);
                       }
                   });
               }
           });
           crawlerThread.start();

       }catch (Exception e){
           e.printStackTrace();
       }
    }


    protected void SaveUserLocalInfo()  {
        SharedPreferences User_Data = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences User_Personal = getSharedPreferences("UserPersonalInfo", MODE_PRIVATE);
        SharedPreferences User_Curriculum = getSharedPreferences("UserCurriculum", MODE_PRIVATE);
        UserLoginInfo.SaveUserLoginInfo(User_Data);
        UserPersonalInfo.SaveUserPersonalInfo(User_Personal);
        UserCurriculumInfo.SaveUserCurriculumInfo(User_Curriculum);
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(Login_Page.this, Main_Page.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("login is destory!");
    }
}