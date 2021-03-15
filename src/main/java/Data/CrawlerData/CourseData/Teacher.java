package Data.CrawlerData.CourseData;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Teacher {
    private final String TeacherName;
    private String TeacherHref = "https://flip.stust.edu.tw";
    private String TeacherEmail;
    private String TeacherDepartment;

    public Teacher(final String teacherName, String teacherHref) {
        TeacherName = teacherName;
        TeacherHref += teacherHref;
//        CrawlerTeacherInfo();
    }

    @Deprecated
    private void CrawlerTeacherInfo(){
        String USER_AGENT = "User-Agent";
        String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
        try {
            Connection.Response res = Jsoup.connect(TeacherHref)
                    .header(USER_AGENT,USER_AGENT_VALUE)
                    .method(Connection.Method.GET)
                    .execute();
            Elements elements = res.parse().select(".main.view");
            TeacherDepartment = elements.get(0).childNode(5).childNode(0).toString().replaceAll(System.lineSeparator(),"");
            TeacherEmail = elements.get(0).childNode(7).childNode(0).childNode(0).toString();
        }catch (Exception e){
            System.out.println("Teacher Crawler Error");
            TeacherEmail = "查無資料";
            TeacherDepartment = "查無資料";
        }
    }

    public String getTeacherDepartment() {
        return TeacherDepartment;
    }

    public String getTeacherEmail() {
        return TeacherEmail;
    }

    public String getTeacherHref() {
        return TeacherHref;
    }

    public String getTeacherName() {
        return TeacherName;
    }
}
