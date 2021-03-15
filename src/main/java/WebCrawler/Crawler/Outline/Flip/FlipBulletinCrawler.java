package WebCrawler.Crawler.Outline.Flip;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.CrawlerData.BulletinData.BulletinInfo;
import Data.StaticData.UserBulletinInfo;
import Data.StaticData.UserCourseInfo;
import Data.StaticData.UserLoginCookies;
import Data.StaticData.UserLoginInfo;
import WebCrawler.Crawler.Core.DataCrawler;

public class FlipBulletinCrawler extends DataCrawler {
    Elements Bulletin_Elements;
    String STUST_URL ="https://flip.stust.edu.tw";
    String LOGIN_URL = "https://flip.stust.edu.tw/service/loginagent/";
    String BULLETIN_URL = "https://flip.stust.edu.tw/user/";

    public FlipBulletinCrawler() {
        BULLETIN_URL += UserLoginInfo.getUser().getUsername() + "/newest/bulletin";
    }

    public FlipBulletinCrawler(int page) {
        BULLETIN_URL += UserLoginInfo.getUser().getUsername() + "/newest/bulletin?page="+String.valueOf(page);
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
            document = Jsoup.connect(BULLETIN_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(res.cookies())
                    .post();
            UserLoginCookies.setFlipLoginInfo(res.cookies());
        }catch (Exception e){
            System.out.println("Crawler Error : Flip Login Error, Please Check Account");
            e.printStackTrace();
        }
        if (document != null)
            Bulletin_Elements = document.select("tr[id*=bulletin]");
    }

    @Override
    protected void Connect_Cookies(){
        Document document = null;
        try {
            document = Jsoup.connect(BULLETIN_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getFlipLoginInfo())
                    .post();
        }catch (Exception e){
            System.out.println("Crawler Error : Flip Login Error, Please Re-Login");
            e.printStackTrace();
        }
        if (document != null)
            Bulletin_Elements = document.select("tr[id*=bulletin]");
    }

    @Override
    public Boolean Successful() {
        return Bulletin_Elements != null && Bulletin_Elements.size() != 0;
    }

    @Override
    public void Getting() {
        if(UserLoginCookies.getFlipLoginInfo().size() == 0){
            Connect_UserInfo();
        }
        else {
            Connect_Cookies();
            if (!Successful())
                Connect_UserInfo();
        }
    }

    @Override
    public void Building() {
        String SortName = "";
        String Title = "";
        String Href = "";
        String Date = "";
        String Icon;
        for (Element element: Bulletin_Elements){
            Elements Link = element.child(1).select("a");
            Elements Sort = element.child(2).select("a");
            Elements Time = element.child(3).select("span");
            if (Link.size() != 0 ){
                Title = Link.get(0).attr("title");
                Href = Link.get(0).attr("href");
            }
            if (Sort.size() != 0 )
                SortName = Sort.get(0).childNode(0).toString().split("_")[0];
            if (Time.size() != 0 )
                Date = Time.attr("title");
            Icon = UserCourseInfo.FindCoursePhoto(0,SortName);
            UserBulletinInfo.setBulletin(0,new BulletinInfo(Icon,SortName,Title,STUST_URL+Href,Date));
        }
    }
}
