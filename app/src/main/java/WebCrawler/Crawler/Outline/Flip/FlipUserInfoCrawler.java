package WebCrawler.Crawler.Outline.Flip;

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

public class FlipUserInfoCrawler extends DataCrawler {
    Elements UserInfo_Elements;
    String PhotoHref = "https://flipclass.stust.edu.tw/";
    String LOGIN_URL = "https://flip.stust.edu.tw/service/loginagent/";
    String TARGET_URL = "https://flip.stust.edu.tw/user/" + UserLoginInfo.getUser().getUsername() + "/myInfo";


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
            UserLoginCookies.setFlipLoginInfo(res.cookies());
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Check Account");
            e.printStackTrace();
        }
        if (document != null) {
            UserInfo_Elements =document.select("div.field-value");
        }
    }

    @Override
    protected void Connect_Cookies() {
        Document document = null;
        try {
            document = Jsoup.connect(TARGET_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getFlipLoginInfo())
                    .post();
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Login Error, Please Re-Login");
            e.printStackTrace();
        }
        if (document != null) {
            UserInfo_Elements = document.select("div.field-value");
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
        String Name = "";
        String Role = "無法取得資訊";
        String EducationSystem = "無法取得資訊";
        String SchoolClass = "無法取得資訊";
        if (UserInfo_Elements.size() > 3) {
            for (int i = 0; i < 3; i++) {
                if (UserInfo_Elements.get(i).childNodes().size() != 0) {
                    switch (i) {
                        case 0:
                            StudentID = UserInfo_Elements.get(i).childNodes().get(0).toString().replace("\n", "");
                            break;
                        case 1:
                            Name = UserInfo_Elements.get(i).childNodes().get(0).toString().replace("\n", "");
                            break;
                        case 2:
                            Email = UserInfo_Elements.get(i).childNodes().get(0).toString().replace("\n", "");
                            break;
                    }
                }
            }
        }
        UserPersonalInfo.setMyUserInfo(new UserInfo(StudentID, Name, Email, Role, EducationSystem, SchoolClass, PhotoHref));
        UserPersonalInfo.getMyUserInfo().BuildDetailContent();
    }
}
