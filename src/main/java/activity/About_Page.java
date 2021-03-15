package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import FontRatio.Ratio_Page_About;
import FontSet.FontSetter;

public class About_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        this.Composing();
        System.out.println("About is create");
    }

    protected void Composing(){
        ConstraintLayout HomePage = (ConstraintLayout) findViewById(R.id.About_Page);
        LinearLayout About_Content = (LinearLayout)findViewById(R.id.about_page_linear);
        Button Union_Button = (Button) findViewById(R.id.about_page_union);
        DisplayMetrics Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        Ratio_Page_About Font_std = new Ratio_Page_About(Display.widthPixels,Display.heightPixels);
        FontSetter.SetLinearLayoutSize(About_Content,Font_std.getRectangleWidth(),Font_std.getRectangleHeight());
        FontSetter.SetButtonSize(Union_Button,Font_std.getUnionWidth(),Font_std.getUnionHeight());
        FontSetter.resizeTextfont(HomePage,Display.density);
    }

    public void Union(View view){
        Intent intent = new Intent(About_Page.this, Main_Page.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(About_Page.this, Main_Page.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("About is destory");
    }
}