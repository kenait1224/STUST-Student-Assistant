package activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Dictionary;
import java.util.Hashtable;

import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import Data.StaticData.UserConfig;
import Data.StaticData.UserCurriculumInfo;
import FontRatio.Ratio_Loading_Dialog;
import FontRatio.Ratio_Page_Userinterface;
import FragmentPage.Offline.OfflineFragment;
import ViewPagerAdapter.ScreenSlidePagerAdapter;
import WebCrawler.Thread.Refresh.RefreshInfoInterface;
import WebCrawler.Thread.Refresh.RefreshThread;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class UserInterface_Page extends AppCompatActivity {

    BottomNavigationView Userinterface_menu;

    private Handler UserInterfacePageHandler;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private OfflineFragment offline;
    private TabLayout tabLayout;
    private boolean fragmentTransition = false;
    private TextSwitcher Userinterface_title;
    private Dictionary MenuDict;
    private String[] MenuTitle = {"curriculum", "course", "news", "message", "personal"};
    private ImageButton refresh_button;
    private int viewPage_index;
    private String LastMenuItemID;


    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {

            // Create a new TextView
            DisplayMetrics d = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(d);
//            Typeface font = ResourcesCompat.getFont(getBaseContext(), R.font.comfortaa_light);
            float weight = d.density / ((d.density >= 2.5) ? (float) d.density : (float) (d.density + 0.35));
            TextView t = new TextView(UserInterface_Page.this);
            t.setTextSize((19 * weight));
//            t.setTypeface(font);
            t.setTextColor(getColor(R.color.colorBlack));
            return t;
        }
    };

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
        Fresco.initialize(this);
        int Target = (UserConfig.getMyConfig().get("OfflineMode") == 1) ? 0 : getIntent().getIntExtra("ActivityID", 0);
        UserInterfacePageHandler = new Handler();
        viewPage_index = getIntent().getIntExtra("Source", 0);
        setContentView(R.layout.activity_userinterface);
        DisplayMetrics Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        Ratio_Page_Userinterface ratio_userinterface = new Ratio_Page_Userinterface(Display.widthPixels, Display.heightPixels);
        mPager = (ViewPager) findViewById(R.id.Userinterface_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.Userinterface_tab);
        refresh_button = (ImageButton) findViewById(R.id.Userinterface_Refresh);
        Userinterface_title = (TextSwitcher) findViewById(R.id.Userinterface_Title_Name);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), Target, Display.density, Display.widthPixels, Display.heightPixels);
        mPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mPager);
        Userinterface_menu = findViewById(R.id.Userinterface_menu);
        Userinterface_menu.setItemIconTintList(null);
        Userinterface_menu.getMenu().findItem(R.id.icon1).setChecked(true);
        Userinterface_menu.setItemIconSize(ratio_userinterface.getUserinterfaceMenuSize());
        int Refresh_Padding = ratio_userinterface.getCourseMenuRefreshPadding();
        refresh_button.setPadding(Refresh_Padding, Refresh_Padding, Refresh_Padding, Refresh_Padding);
        setTextTransition(Userinterface_title);
        getMenuDict(Userinterface_menu);
        setPage(Target);

        //角標設定f
//        setMenuBadge(0,0,Display.heightPixels,Display.widthPixels);
//        setTitleBadge(refresh,Display.heightPixels,Display.widthPixels);
        // ViewPager切換監聽
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(position);
                String selected = (String) Userinterface_menu.getMenu().findItem(Userinterface_menu.getSelectedItemId()).getTitle();
                if (selected.compareTo("我的課程") == 0 || selected.compareTo("我的通知") == 0) {
                    Userinterface_title.setText(selected + (String) ((position == 0) ? "  Flip" : "  Flip Class"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Userinterface_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (!(item.getTitle().toString().compareTo(LastMenuItemID) == 0)) {
                    LastMenuItemID = item.getTitle().toString();
                    switch (item.getItemId()) {
                        case R.id.icon1:
                            pagerAdapter.update(0);
                            pagerAdapter.notifyDataSetChanged();
                            Userinterface_title.setText(getResources().getText(R.string.MyCurriclum));
                            tabLayout.setVisibility(View.GONE);
                            return true;
                        case R.id.icon2:
                            pagerAdapter.update(1);
                            pagerAdapter.notifyDataSetChanged();
                            mPager.setCurrentItem(0);
//                        Fragment target = (UserConfig.getMyConfig().get("OfflineMode").compareTo("1") == 0)?offline:fragment2;
                            Userinterface_title.setText(getResources().getText(R.string.MyCourse) + ((viewPage_index == 0) ? "  Flip" : "  Flip Class"));
                            tabLayout.setVisibility(View.VISIBLE);
                            return true;
                        case R.id.icon3:
                            pagerAdapter.update(2);
                            pagerAdapter.notifyDataSetChanged();
                            Userinterface_title.setText(getResources().getText(R.string.MyNews));
                            tabLayout.setVisibility(View.GONE);
                            return true;
                        case R.id.icon4:
                            mPager.setCurrentItem(0);
                            pagerAdapter.update(3);
                            pagerAdapter.notifyDataSetChanged();
                            Userinterface_title.setText(getResources().getText(R.string.MyMessage) + ((viewPage_index == 0) ? "  Flip" : "  Flip Class"));
                            tabLayout.setVisibility(View.VISIBLE);
                            return true;
                        case R.id.icon5:
                            pagerAdapter.update(4);
                            pagerAdapter.notifyDataSetChanged();
                            Userinterface_title.setText(getResources().getText(R.string.MyInfo));
                            tabLayout.setVisibility(View.GONE);
                            return true;
                    }
                }
                return false;
            }
        });

    }

//    private void setFirstTab() {
//        int id = getIntent().getIntExtra("SwitchSource", -1);
//        if (id != -1)
//            tabLayout.selectTab(tabLayout.getTabAt(id));
//    }

    // 設定 First Page

    private void setPage(int CallingActivityID) {
        switch (CallingActivityID) {
            case 0:
                Userinterface_title.setText(getResources().getText(R.string.MyCurriclum));
                Userinterface_menu.getMenu().getItem(0).setChecked(true);
                tabLayout.setVisibility(View.GONE);
                LastMenuItemID = (String) getResources().getText(R.string.MyCurriclum);
                break;
            case 1:
//                setFirstTab();
                mPager.setCurrentItem(viewPage_index);
                Userinterface_title.setText(getResources().getText(R.string.MyCourse) + ((viewPage_index == 0) ? "  Flip" : "  Flip Class"));
                Userinterface_menu.getMenu().getItem(1).setChecked(true);
                tabLayout.setVisibility(View.VISIBLE);
                viewPage_index = 0;
                LastMenuItemID = (String) getResources().getText(R.string.MyCourse);
                break;
            case 2:
//                setFirstTab();
                Userinterface_title.setText(getResources().getText(R.string.MyNews));
                Userinterface_menu.getMenu().getItem(2).setChecked(true);
                tabLayout.setVisibility(View.GONE);
                LastMenuItemID = (String) getResources().getText(R.string.MyNews);
                break;
            case 3:
//                setFirstTab();
                mPager.setCurrentItem(viewPage_index);
                Userinterface_title.setText(getResources().getText(R.string.MyMessage) + ((viewPage_index == 0) ? "  Flip" : "  Flip Class"));
                Userinterface_menu.getMenu().getItem(3).setChecked(true);
                tabLayout.setVisibility(View.VISIBLE);
                viewPage_index = 0;
                LastMenuItemID = (String) getResources().getText(R.string.MyMessage);
                ;
                break;
            default:
                break;
        }
    }

    // 選項 Menu Icon 角標設定

    private void setMenuBadge(int ItemIndex, int number, int displayHeight, int displayWidth) {
        int targetID = Userinterface_menu.getMenu().getItem(ItemIndex).getItemId();
        BadgeDrawable icon = Userinterface_menu.getOrCreateBadge(targetID);
        if (number > 0) {
            icon.setVerticalOffset(displayHeight / 150);
            icon.setHorizontalOffset(displayWidth / 30);
            icon.setNumber(number);
        } else {
            icon.setVerticalOffset(displayHeight / 150);
            icon.setHorizontalOffset(displayWidth / 34);
        }
    }

    public void Logout(View view) {
        SharedPreferences User_Data = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences User_Permissions = getSharedPreferences("PermissionsChecked", MODE_PRIVATE);
        User_Permissions.edit().clear().commit();
        User_Data.edit().clear().commit();
    }

    // 選項 Menu Icon 角標設定

    private void setTitleBadge(View view, int displayHeight, int displayWidth) {
        Badge badge = new QBadgeView(this).bindTarget(view);
        badge.setBadgeText("");
        badge.setBadgeTextSize(1, true);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        badge.setGravityOffset(0, displayHeight / 65, false);
    }


    private void getMenuDict(BottomNavigationView Userinterface_menu) {
        MenuDict = new Hashtable();
        for (int i = 0; i < Userinterface_menu.getMenu().size(); i++)
            MenuDict.put(MenuTitle[i], i);
    }

    // Userinterface Title 文字切換動畫

    private void setTextTransition(TextSwitcher view) {
        view.setFactory(mFactory);
        view.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.title_fade_in));
        view.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.title_fade_out));
    }

    public void FragmentRefresh(View view) {
        DisplayMetrics Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        try {
            final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(this, new Ratio_Loading_Dialog(Display.widthPixels, Display.heightPixels), Display.density);
            loadingDialogBar.show();
            RefreshThread refreshThread = new RefreshThread(new RefreshInfoInterface() {
                @Override
                public void CrawlerLoading(final int text) {
                    UserInterfacePageHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                            LoadingText.setText(text);
                        }
                    });
                }

                @Override
                public void CrawlerSuccessful() {
                    UserInterfacePageHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(LastMenuItemID.compareTo("我的課表")==0)
                                SaveUserLocalCurriculum();
                            pagerAdapter.notifyDataSetChanged();
                            loadingDialogBar.dismiss();
                        }
                    });
                }

                @Override
                public void CrawlerFailed() {
                    UserInterfacePageHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialogBar.dismiss();
                        }
                    });

                }
            }, LastMenuItemID);
            refreshThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Course Detail Crawler Failed");
        }

    }

    protected void SaveUserLocalCurriculum()  {
        SharedPreferences User_Curriculum = getSharedPreferences("UserCurriculum", MODE_PRIVATE);
        UserCurriculumInfo.SaveUserCurriculumInfo(User_Curriculum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}