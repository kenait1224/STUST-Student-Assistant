package Data.StaticData;

import java.util.ArrayList;
import java.util.Hashtable;

import RecyclerViewController.Course.ExpandableRecyclerView.Menu.CourseMenu;

public class UserCourseDetail {

    private static Hashtable<String, ArrayList<CourseMenu>> FlipCourseDetail = new Hashtable<>();
    private static Hashtable<String, ArrayList<CourseMenu>> FlipClassCourseDetail = new Hashtable<>();

    public static void Clear(){
        FlipCourseDetail.clear();
        FlipClassCourseDetail.clear();
    }

    public static  Hashtable<String, ArrayList<CourseMenu>>  getCourseDetail(int idx){
        switch (idx){
            case 0:
                return FlipCourseDetail;
            case 1:
                return FlipClassCourseDetail;
        }
        return null;
    }
}
