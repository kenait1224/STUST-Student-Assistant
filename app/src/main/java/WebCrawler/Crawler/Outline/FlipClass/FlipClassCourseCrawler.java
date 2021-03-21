package WebCrawler.Crawler.Outline.FlipClass;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import Data.CrawlerData.CourseData.CourseInfo;
import Data.CrawlerData.CourseData.Teacher;
import Data.StaticData.UserCourseInfo;
import Data.StaticData.UserLoginCookies;
import Data.StaticData.UserLoginInfo;
import WebCrawler.Crawler.Core.DataCrawler;

public class FlipClassCourseCrawler extends DataCrawler {

    Elements Course_Element;
    String LOGIN_URL = "https://flipclass.stust.edu.tw/service/loginagent/";
    String COURSE_URL = "https://flipclass.stust.edu.tw/dashboard";

    public FlipClassCourseCrawler() {
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
            UserLoginCookies.setFlipClassLoginInfo(res.cookies());
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Check Account");
            e.printStackTrace();
        }
        if (document != null)
            Course_Element = document.select("div.inline.clearfix");
    }

    @Override
    protected void Connect_Cookies() {
        Document document = null;
        try {
            document = Jsoup.connect(COURSE_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .post();
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Re-Login");
            e.printStackTrace();
        }
        if (document != null)
            Course_Element = document.select("div.inline.clearfix");
    }

    @Override
    public Boolean Successful() {
        return Course_Element != null && Course_Element.size() != 0;
    }

    @Override
    public void Getting() {
        if (UserLoginCookies.getFlipClassLoginInfo().size() == 0) {
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
            Elements photo = element.select("div.img-container");
            Elements info = element.select("div.fs-caption").select("a[href]");
            Elements teachers = element.select("div.fs-hint").select("div[style]");
            course_teachers = new ArrayList<>();
            if (photo.size() != 0){
                String photo_style = photo.attr("style");
                course_photo = photo_style.substring(photo_style.indexOf("(")+1,photo_style.indexOf(")"));
            }
            if (info.size() != 0) {
                course_name = info.get(0).childNode(0).toString().split("_")[0].replace(" ","");
                course_href = info.attr("href");
            }
            if(teachers.size() != 0){
                String[] Teachers = teachers.get(0).childNode(0).toString().split(":")[1].split(",");
                for(String t : Teachers)
                    course_teachers.add(new Teacher(t, ""));
            }
            if (course_href != null && course_href != "" )
                UserCourseInfo.SetCourseInfo(1,new CourseInfo(1,course_photo, course_name, course_href, course_teachers));
        }
        FindCourseInfoHref();
    }


    private void FindCourseInfoHref() {
        try {
            for (int i = 0; i < UserCourseInfo.getCourseInfo(1).size(); i++) {
                String target =UserCourseInfo.getCourseInfo(1).get(i).getCourseHref();
                target = target.substring(0,target.lastIndexOf("/"))+"/info"+target.substring(target.lastIndexOf("/"),target.length());
                Document Course_document = Jsoup.connect(target)
                        .header(USER_AGENT, USER_AGENT_VALUE)
                        .cookies(UserLoginCookies.getFlipClassLoginInfo())
                        .post();
                UserCourseInfo.getCourseInfo(1).get(i).setCourseInfoHref(Course_document.select("iframe").attr("src"));
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
