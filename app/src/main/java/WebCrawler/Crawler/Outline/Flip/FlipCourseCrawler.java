package WebCrawler.Crawler.Outline.Flip;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import Data.CrawlerData.CourseData.CourseInfo;
import Data.CrawlerData.CourseData.Teacher;
import Data.StaticData.UserCourseInfo;
import Data.StaticData.UserLoginCookies;
import Data.StaticData.UserLoginInfo;
import WebCrawler.Crawler.Core.DataCrawler;

public class FlipCourseCrawler extends DataCrawler {

    Elements Course_Element;
    String LOGIN_URL = "https://flip.stust.edu.tw/service/loginagent/";
    String COURSE_URL = "https://flip.stust.edu.tw/user/";

    public FlipCourseCrawler() {
        COURSE_URL += UserLoginInfo.getUser().getUsername() + "/myCourse";
    }

    @Override
    protected void Connect_UserInfo() {
        Document document = null;
        try {
            Connection.Response res = Jsoup.connect(LOGIN_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .data("account", UserLoginInfo.getUser().getUsername())
                    .data("password", UserLoginInfo.getUser().getPassword())
                    .method(Connection.Method.POST).execute();
            document = Jsoup.connect(COURSE_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(res.cookies())
                    .post();
            UserLoginCookies.setFlipLoginInfo(res.cookies());
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Check Account");
            e.printStackTrace();
        }
        if (document != null)
            Course_Element = document.select("div.item.clearfix");
    }

    @Override
    protected void Connect_Cookies() {
        Document document = null;
        try {
            document = Jsoup.connect(COURSE_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getFlipLoginInfo())
                    .post();
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Re-Login");
            e.printStackTrace();
        }
        if (document != null)
            Course_Element = document.select("div.item.clearfix");
    }

    @Override
    public Boolean Successful() {
        return Course_Element != null && Course_Element.size() != 0;
    }

    @Override
    public void Getting() {
        if (UserLoginCookies.getFlipLoginInfo().size() == 0) {
            Connect_UserInfo();
        } else {
            Connect_Cookies();
            if (!Successful())
                Connect_UserInfo();
        }
    }

    @Override
    public void Building() {
        String course_href = null;
        String course_name = null;
        String course_photo = null;
        ArrayList<Teacher> course_teachers;
        for (Element element : Course_Element) {
            Elements photo = element.select("div.photo");
            Elements info = element.select("div.info");
            course_teachers = new ArrayList<>();
            if (photo.size() != 0){
                course_photo = photo.select("img").get(0).attr("src");
            }
            if (info.size() != 0) {
                course_name = info.get(0).childNode(1).attr("title").split("_")[0];
                course_href = info.get(0).childNode(1).childNode(0).attr("href");
                for (Node teacher_info : info.get(0).childNode(3).childNode(0).childNodes()) {
                    if (teacher_info instanceof Element) {
                        course_teachers.add(new Teacher(teacher_info.attr("title"), teacher_info.attr("href")));
                    }
                }

            }
            if (course_href != null && course_href != "" )
                UserCourseInfo.SetCourseInfo(0,new CourseInfo(0,course_photo, course_name, course_href, course_teachers));
        }
        FindCourseInfoHref();
    }


    private void FindCourseInfoHref() {
        try {
            for (int i = 0; i < UserCourseInfo.getCourseInfo(0).size(); i++) {
                Document Course_document = Jsoup.connect(UserCourseInfo.getCourseInfo(0).get(i).getCourseHref() + "/introduction")
                        .header(USER_AGENT, USER_AGENT_VALUE)
                        .cookies(UserLoginCookies.getFlipLoginInfo())
                        .post();
                UserCourseInfo.getCourseInfo(0).get(i).setCourseInfoHref(Course_document.select("iframe#client").attr("src"));
            }
        } catch (Exception e) {
            System.out.println("Crawler Error : FlipCourseAddressList Get Error");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getFlipCourseAddressList() {
        ArrayList<String> CourseList = new ArrayList<>();
        try {
            for (int i = 0; i < UserCourseInfo.getCourseInfo(0).size(); i++) {
                Document Target = Jsoup.connect(UserCourseInfo.getCourseInfo(0).get(i).getCourseInfoHref())
                        .header(USER_AGENT, USER_AGENT_VALUE)
                        .post();
                if (Target.select("tr").get(8).select("tbody").size() != 0)
                    CourseList.add(UserCourseInfo.getCourseInfo(0).get(i).getCourseName());
            }
        } catch (Exception e) {
            System.out.println("Crawler Error : FlipCourseAddressList Get Error");
            e.printStackTrace();
        }
        return CourseList;
    }

}
