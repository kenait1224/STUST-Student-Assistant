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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import FontRatio.Ratio_Course;
import FontRatio.Ratio_Loading_Dialog;
import FontSet.FontSetter;
import FragmentPage.Course.Dialog.CourseInfoDialogFragment;
import FragmentPage.Course.Fragment.CourseListFragment;
import Data.CrawlerData.CourseDetailInfo.DetailInfoContent;
import Data.StaticData.UserCourseDetail;
import Data.StaticData.UserCourseInfo;
import WebCrawler.Thread.CoursePage.CourseDetailCrawlerInterface;
import WebCrawler.Thread.CoursePage.CourseDetailInfoCrawlerInterface;
import WebCrawler.Thread.CoursePage.CourseDetailCrawlerThread;
import WebCrawler.Thread.CoursePage.CourseDetailInfoCrawlerThread;

public class Course_Page extends AppCompatActivity {

    DisplayMetrics Display;
    Handler CoursePageHandler;
    Intent intent;
    int Source;
    int Position;

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1)
            Union(null);
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoursePageHandler = new Handler();
        setContentView(R.layout.activity_course_page);
        Composing();
    }

    private void Composing() {
        intent = getIntent();
        Source = intent.getIntExtra("Source",-1);
        Position = intent.getIntExtra("Target_Index", -1);
        TextView Title = (TextView) findViewById(R.id.Course_Menu_Title);
        Title.setText(UserCourseInfo.getCourseInfo(Source).get(Position).getCourseName());
        Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        Ratio_Course ratio_course = new Ratio_Course(Display.widthPixels, Display.heightPixels);
        int Refresh_padding = ratio_course.getCourseMenuRefreshPadding();
        int Union_padding = ratio_course.getCourseMenuUnionPadding();
        int Info_padding = ratio_course.getCourseMenuInfoPadding();
        ImageButton union_button = (ImageButton) findViewById(R.id.Course_detail_union);
        ImageButton refresh_button = (ImageButton) findViewById(R.id.Course_detail_refresh);
        ImageButton info_button = (ImageButton) findViewById(R.id.Course_info);
        SetButtonPadding(union_button, Union_padding);
        SetButtonPadding(refresh_button, Refresh_padding);
        SetButtonPadding(info_button, Info_padding);
        LinearLayout home = (LinearLayout) findViewById(R.id.course_menu_page);
        FontSetter.resizeTextfont(home, Display.density);
        CourseListFragment fragment = new CourseListFragment(Display, ratio_course, UserCourseInfo.getCourseInfo(Source).get(Position).getCourseName(),Source);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.course_title_fragment, fragment, "Course_Detail");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void SetButtonPadding(ImageButton view, int padding) {
        view.setPadding((int) (1.05 * padding), padding, (int) (1.05 * padding), padding);
    }

    public void Refresh(View view) {
        UserCourseDetail.getCourseDetail(Source).remove(UserCourseInfo.getCourseInfo(Source).get(Position).getCourseName());
        try {
            final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(this, new Ratio_Loading_Dialog(Display.widthPixels, Display.heightPixels), Display.density);
            loadingDialogBar.show();
            CourseDetailCrawlerThread courseDetailCrawlerThread = new CourseDetailCrawlerThread(new CourseDetailCrawlerInterface() {
                @Override
                public void CrawlerLoading(final int text) {
                    CoursePageHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                            LoadingText.setText(text);
//                            LoadingFragment loadingFragment = new LoadingFragment(new Ratio_Loading(Display.widthPixels,Display.heightPixels));
//                            getSupportFragmentManager().beginTransaction().replace(R.id.course_title_fragment,loadingFragment).addToBackStack(null).commit();
                        }
                    });
                }

                @Override
                public void CrawlerSuccessful() {
                    CoursePageHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag("Course_Detail");
                            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.detach(fragment);
                            fragmentTransaction.attach(fragment);
                            fragmentTransaction.commit();
                            loadingDialogBar.dismiss();
                        }
                    });
                }

                @Override
                public void CrawlerFailed() {
                    loadingDialogBar.dismiss();
                }
            }, Source,UserCourseInfo.getCourseInfo(Source).get(Position).getCourseName(), UserCourseInfo.getCourseInfo(Source).get(Position).getCourseHref());
            courseDetailCrawlerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Course Detail Crawler Failed");
        }
    }

    public void Union(View view) {
        Intent Target_intent = new Intent(Course_Page.this, UserInterface_Page.class);
        Target_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Target_intent.putExtra("ActivityID", 1);
        Target_intent.putExtra("Source",Source);
        startActivity(Target_intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void Info(View view) {
        try {
            final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(this, new Ratio_Loading_Dialog(Display.widthPixels, Display.heightPixels), Display.density);
            loadingDialogBar.show();
            CourseDetailInfoCrawlerThread courseDetailInfoCrawlerThread = new CourseDetailInfoCrawlerThread(new CourseDetailInfoCrawlerInterface() {
                @Override
                public void CrawlerLoading(final int text) {
                    CoursePageHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                            LoadingText.setText(text);
                        }
                    });
                }

                @Override
                public void CrawlerSuccessful(final ArrayList<DetailInfoContent> Target) {
                    CoursePageHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogBar.dismiss();
                            CourseInfoDialogFragment courseInfoDialogFragment = new CourseInfoDialogFragment(Display.heightPixels,Display.widthPixels,Display.density, Target);
                            courseInfoDialogFragment.show(getSupportFragmentManager(), "Detail_Info");
                        }
                    });

                }

                @Override
                public void CrawlerFailed() {

                }
            }, UserCourseInfo.getCourseInfo(Source).get(Position).getCourseInfoHref());
            courseDetailInfoCrawlerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Course Detail Crawler Failed");
        }

    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(Course_Page.this, UserInterface_Page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}