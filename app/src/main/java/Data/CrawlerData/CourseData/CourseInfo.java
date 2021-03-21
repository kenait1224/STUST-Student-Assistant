package Data.CrawlerData.CourseData;

import java.util.ArrayList;

public class CourseInfo {
    private String STUST_URL = "https://flip.stust.edu.tw";
    private String STUSTC_URL = "https://flipclass.stust.edu.tw";
    private String CourseName;
    private String CourseHref;
    private String CoursePhoto;
    private String CourseInfoHref;
    private ArrayList<Teacher> CourseTeachers;

    public CourseInfo(int idx ,String coursePhoto, String courseName, String courseHref, ArrayList<Teacher> courseTeacher) {
        CoursePhoto = (coursePhoto != null && coursePhoto != "") ? ((idx==0)?STUST_URL:STUSTC_URL) + coursePhoto : null;
        CourseHref = ((idx==0)?STUST_URL:STUSTC_URL) + courseHref;
        CourseName = courseName;
        CourseTeachers = courseTeacher;
    }

    public void setCourseInfoHref(String courseInfoHref) {
        CourseInfoHref = courseInfoHref;
    }

    public String getTeachersString() {
        String teachers = "";
        for (Teacher teacher : CourseTeachers)
            teachers += teacher.getTeacherName() + ",";
        return new StringBuilder(teachers).deleteCharAt(teachers.length() - 1).toString();
    }

    public ArrayList<Teacher> getCourseTeachers() {
        return CourseTeachers;
    }

    public String getCoursePhoto() {
        return CoursePhoto;
    }

    public String getCourseName() {
        return CourseName;
    }

    public String getCourseHref() {
        return CourseHref;
    }

    public String getCourseInfoHref() {
        return CourseInfoHref;
    }
}
