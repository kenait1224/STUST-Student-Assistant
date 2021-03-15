package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.backends.pipeline.Fresco;

import FontRatio.Ratio_Course;
import FontSet.FontSetter;
import FragmentPage.News.Fragment.NewsListFragment;
import Data.StaticData.UserBulletinInfo;

public class News_Page extends AppCompatActivity {

    DisplayMetrics Display;
    Handler NewsPageHandler;
    Intent intent;
    int Position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_news_page);
        NewsPageHandler = new Handler();
        Composing();
    }

    public void onBackPressed() {
        Union(null);
    }

    public void Union(View view) {
        Intent Target_intent = new Intent(News_Page.this, UserInterface_Page.class);
        Target_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Target_intent.putExtra("ActivityID", 2);
        startActivity(Target_intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void Composing() {
        intent = getIntent();
        Position = intent.getIntExtra("Target_Index", -1);
        TextView Title = (TextView) findViewById(R.id.news_title_name);
        Title.setText(UserBulletinInfo.getBulletinName(Position));
        Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        Ratio_Course ratio_course = new Ratio_Course(Display.widthPixels, Display.heightPixels);
        int Refresh_padding = ratio_course.getCourseMenuRefreshPadding();
        int Union_padding = ratio_course.getCourseMenuUnionPadding();
        ImageButton union_button = findViewById(R.id.news_detail_union);
        ImageButton refresh_button = findViewById(R.id.news_refresh);
        SetButtonPadding(union_button, Union_padding);
        SetButtonPadding(refresh_button, Refresh_padding);
        LinearLayout home = findViewById(R.id.news_detail_menu);
        FontSetter.resizeTextfont(home, Display.density);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle Fragment_Bundle = new Bundle();
        Fragment_Bundle.putFloat("Density", Display.density);
        Fragment_Bundle.putInt("Group", Position);
        Fragment_Bundle.putInt("DisplayWidth", Display.widthPixels);
        Fragment_Bundle.putInt("DisplayHeight", Display.heightPixels);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(Fragment_Bundle);
        fragmentTransaction.replace(R.id.news_page_fragment, fragment, "News_Detail");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void SetButtonPadding(ImageButton view, int padding) {
        view.setPadding((int) (1.05 * padding), padding, (int) (1.05 * padding), padding);
    }

}