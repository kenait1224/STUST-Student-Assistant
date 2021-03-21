package WebCrawler.Crawler.Outline.FlipClass;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.CrawlerData.PersonalData.UserInfo;
import Data.StaticData.UserLoginCookies;
import Data.StaticData.UserLoginInfo;
import Data.StaticData.UserPersonalInfo;
import WebCrawler.Crawler.Core.DataCrawler;

public class FlipClassUserInfoCrawler extends DataCrawler {
    Elements UserInfo_Elements;
    String PhotoHref = "https://flipclass.stust.edu.tw/";
    String LOGIN_URL = "https://flipclass.stust.edu.tw/service/loginagent/";
    String TARGET_URL = "https://flipclass.stust.edu.tw/user/?action=mgr&mod=user&op=info";


    @Override
    protected void Connect_UserInfo() {
        Document document = null;
        try {
            Connection.Response res = Jsoup.connect(LOGIN_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .data("account", UserLoginInfo.getUser().getUsername())
                    .data("password", UserLoginInfo.getUser().getPassword())
                    .method(Connection.Method.POST).execute();
            document = Jsoup.connect(TARGET_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(res.cookies())
                    .post();
            UserLoginCookies.setFlipClassLoginInfo(res.cookies());
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Check Account");
            e.printStackTrace();
        }
        if (document != null) {
            UserInfo_Elements = document.select("div.module.app-user.app-user-info");
            PhotoHref += document.select("img.fs-user-info-photo").attr("src");
        }
    }

    @Override
    protected void Connect_Cookies() {
        Document document = null;
        try {
            document = Jsoup.connect(TARGET_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .post();
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Re-Login");
            e.printStackTrace();
        }
        if (document != null) {
            UserInfo_Elements = document.select("div.module.app-user.app-user-info");
            PhotoHref += document.select("img.fs-user-info-photo").attr("src");
        }
    }

    @Override
    public Boolean Successful() {
        return UserInfo_Elements != null && UserInfo_Elements.size() != 0;
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
        String StudentID = "";
        String Email = "";
        String Name;
        String Role;
        String EducationSystem;
        String SchoolClass;
        for (Element element : UserInfo_Elements) {
            Elements BasicInfo = element.child(0).select("dd");
            Elements AccountInfo = element.child(2).select("dd");
            Name = BasicInfo.get(0).childNode(0).toString().replace("\n","");
            Role = AccountInfo.get(0).childNode(0).toString().replace("\n","");
            EducationSystem = AccountInfo.get(1).childNode(0).toString().replace("\n","");
            SchoolClass = AccountInfo.get(2).childNode(0).toString().replace("\n","");
            if(UserLoginInfo.getUser() != null){
                StudentID = UserLoginInfo.getUser().getUsername();
                Email = StudentID + "@stust.edu.tw";
            }
             UserPersonalInfo.setMyUserInfo(new UserInfo(StudentID, Name, Email, Role,EducationSystem,SchoolClass,PhotoHref));
            UserPersonalInfo.getMyUserInfo().BuildDetailContent();
        }
    }
}
