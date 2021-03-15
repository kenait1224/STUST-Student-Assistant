package WebCrawler.Crawler.Outline.CourseSelectionSystem;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import Data.CrawlerData.CurriculumData.Content;
import Data.CrawlerData.CurriculumData.CurriculumTable;
import Data.StaticData.UserCurriculumInfo;
import Data.StaticData.UserLoginCookies;
import Data.StaticData.UserLoginInfo;
import WebCrawler.Crawler.Core.DataCrawler;

public class CurriculumCrawler extends DataCrawler {

    Element Curriculum_Element;
    String CourseSelectionSystem_URL = "https://course.stust.edu.tw/CourSel/Login.aspx?ReturnUrl=%2fCourSel%2fboard.aspx";
    String Curriculum_URL = "https://course.stust.edu.tw/CourSel/Pages/MyTimeTable.aspx?role=S";

    @Override
    protected void Connect_UserInfo() {
        Document document = null;
        try {
            Document WebRes = Jsoup.connect(CourseSelectionSystem_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .method(Connection.Method.GET).get();
            Connection.Response DataRes = Jsoup.connect(CourseSelectionSystem_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .data("__VIEWSTATEGENERATOR", WebRes.select("input#__VIEWSTATEGENERATOR").attr("value"))
                    .data("__EVENTVALIDATION", WebRes.select("input#__EVENTVALIDATION").attr("value"))
                    .data("__VIEWSTATE", WebRes.select("input#__VIEWSTATE").attr("value"))
                    .data("Login1$UserName", UserLoginInfo.getUser().getUsername())
                    .data("Login1$Password", UserLoginInfo.getUser().getPassword())
                    .data("Login1$LoginButton", "登入")
                    .method(Connection.Method.POST).execute();
            document = Jsoup.connect(Curriculum_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(DataRes.cookies())
                    .post();
            UserLoginCookies.setCourseSelectSystemLoginInfo(DataRes.cookies());
        } catch (Exception e) {
            System.out.println("Crawler Error : Curriculum Login Error Please Check Account ");
            e.printStackTrace();
        }
        if (document != null)
            Curriculum_Element = document.select("tbody").last();
    }

    @Override
    protected void Connect_Cookies() {
        Document document = null;
        try {
            document = Jsoup.connect(Curriculum_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getCourseSelectSystemLoginInfo())
                    .post();
        } catch (Exception e) {
            System.out.println("Crawler Error : Curriculum Login Error Please Re-Login ");
            e.printStackTrace();
        }
        if (document != null)
            Curriculum_Element = document.select("tbody").last();
    }

    public Boolean Successful() {
        return Curriculum_Element != null && Curriculum_Element.children().size() == 15;
    }

    @Override
    public void Getting() {
        if (UserLoginCookies.getCourseSelectSystemLoginInfo().size() == 0) {
            Connect_UserInfo();
        } else {
            Connect_Cookies();
            if (!Successful())
                Connect_UserInfo();
        }
    }

    @Override
    public void Building() {
        CurriculumTable curriculumTable = new CurriculumTable();
        for (int i = 1; i < 15; i++) {
            for (Element element : Curriculum_Element.children().get(i).select("span")) {
                if (element.childNodeSize() != 0) {
                    String time = element.attr("id").substring(element.attr("id").lastIndexOf("ctl") + 3, element.attr("id").length());
                    int day = Integer.parseInt(time.split("_l_")[1]) - 1;
                    int section = Integer.parseInt(time.split("_l_")[0]) - 2;
                    String name = element.childNode(1).toString();
                    String[] info = element.childNode(3).toString().split(" ");
                    String teachers = element.childNode(3).toString().split(" ")[0];
                    String address = (info.length==2)?element.childNode(3).toString().split(" ")[1]:"";
                    curriculumTable.add(day, new Content(section, new Data.CrawlerData.CurriculumData.CourseInfo(name, teachers, address)));
                }
            }
        }
        if (curriculumTable.Size() != 0)
            UserCurriculumInfo.SetCurriculumTable(curriculumTable);
    }
}
