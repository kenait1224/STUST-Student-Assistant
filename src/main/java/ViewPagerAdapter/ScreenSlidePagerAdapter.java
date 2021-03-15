package ViewPagerAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import Data.StaticData.UserConfig;
import Data.StaticData.UserCourseInfo;
import Data.StaticData.UserMessageInfo;
import FragmentPage.Course.Fragment.CourseFragment;
import FragmentPage.Curriculum.Fragment.CurriculumFragment;
import FragmentPage.Message.Fragment.MessageFragment;
import FragmentPage.News.Fragment.NewsFragment;
import FragmentPage.NoData.NoDataFragment;
import FragmentPage.Offline.OfflineFragment;
import FragmentPage.Personal.Fragment.PersonalFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int Width , Height, Icon_ID;
    float Density;

    public ScreenSlidePagerAdapter(@NonNull FragmentManager fm , int icon_ID , float density, int width , int height) {
        super(fm);
        Width = width;
        Height = height;
        Density = density;
        Icon_ID = icon_ID;
    }

    public void update(int target){
        Icon_ID = target;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle Fragment_Bundle = new Bundle();
        Fragment_Bundle.putInt("Source",position);
        Fragment_Bundle.putFloat("Density", Density);
        Fragment_Bundle.putInt("DisplayWidth", Width);
        Fragment_Bundle.putInt("DisplayHeight", Height);
        Fragment f = null;
        switch (Icon_ID){
            case 0:
                f = new CurriculumFragment();
                break;
            case 1:
                f = getDefaultFragment(UserCourseInfo.getCourseInfo(position).size() == 0);
                if (f == null)
                    f = new CourseFragment();
                break;
            case 2:
                f = (UserConfig.getMyConfig().get("OfflineMode") == 1)?new OfflineFragment() : new NewsFragment();
                break;
            case 3:
                f = getDefaultFragment(UserMessageInfo.getMessage(position).size() == 0);
                if (f == null)
                    f = new MessageFragment();
                break;
            case 4:
                f = new PersonalFragment();
                break;
        }
        f.setArguments(Fragment_Bundle);
        return f;
    }

    private Fragment getDefaultFragment(boolean NoData){
        if(UserConfig.getMyConfig().get("OfflineMode") == 1)
            return new OfflineFragment();
        else if (NoData)
            return new NoDataFragment();
        return null;
    }

    @Override
    public int getCount() {
        if (Icon_ID == 1 || Icon_ID == 3 )
            return 2;
        return  1;
    }
}
