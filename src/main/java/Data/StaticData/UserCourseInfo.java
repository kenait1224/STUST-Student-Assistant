package Data.StaticData;

import java.util.ArrayList;

import Data.CrawlerData.CourseData.CourseInfo;

public class UserCourseInfo {

    private static ArrayList<CourseInfo> FlipCourse = new ArrayList<>();
    private static ArrayList<CourseInfo> FlipClassCourse = new ArrayList<>();

    public static void Clear(){
        FlipCourse.clear();
        FlipClassCourse.clear();
    }

    public static void Clear(int idx){
        switch (idx){
            case 0:
                FlipCourse.clear();
            case 1:
                FlipClassCourse.clear();
        }
    }

    public static void SetCourseInfo(int idx ,CourseInfo courseInfo){
        switch (idx){
            case 0:
                FlipCourse.add(courseInfo);
                break;
            case 1:
                FlipClassCourse.add(courseInfo);
                break;
        }
    }

    public static ArrayList<CourseInfo> getCourseInfo(int idx) {
        switch (idx){
            case 0:
                return FlipCourse;
            case 1:
                return FlipClassCourse;
        }
        return null;
    }

    public static String FindCoursePhoto(int idx,String courseName){
        ArrayList<CourseInfo> Target = null ;
        switch (idx){
            case 0:
                Target = FlipCourse;
                break;
            case 1:
                Target = FlipClassCourse;
                break;
        }
        if(Target != null){
            for (int i = 0 ; i < Target.size() ; i++){
                if (Target.get(i).getCourseName().compareTo(courseName) == 0)
                    return  Target.get(i).getCoursePhoto();
            }
        }
        return null;
    }

}
