package WebCrawler.Crawler.Outline.FlipClass;

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

public class FlipClassBulletinCrawler extends DataCrawler {
    Elements Bulletin_Elements;
    String STUST_URL ="https://flipclass.stust.edu.tw";
    String BULLETIN_URL = "https://flipclass.stust.edu.tw/dashboard/latestBulletin";
    String LOGIN_URL = "https://flipclass.stust.edu.tw/service/loginagent/";

    public FlipClassBulletinCrawler() {
    }

    public FlipClassBulletinCrawler(int page) {
        BULLETIN_URL += "?page="+ String.valueOf(page);
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
            Bulletin_Elements = document.select("tbody");
    }

    @Override
    protected void Connect_Cookies(){
        Document document = null;
        try {
            document = Jsoup.connect(BULLETIN_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .post();
        }catch (Exception e){
            System.out.println("Crawler Error : Flip Login Error, Please Re-Login");
            e.printStackTrace();
        }
        if (document != null)
            Bulletin_Elements = document.select("tbody");
    }

    @Override
    public Boolean Successful() {
        return Bulletin_Elements != null && Bulletin_Elements.size() != 0;
    }

    @Override
    public void Getting() {
        if(UserLoginCookies.getFlipClassLoginInfo().size() == 0){
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
        for (Element element: Bulletin_Elements.get(0).children()){
            if(element.toString().indexOf("沒有資料") != -1)
                break;
            Elements Content = element.select("span.text");
            Elements Time = element.select("td.col-date").select("div.text-overflow");
            Elements Link = element.select("a[data-url]").select("a");
            if (Content.size() == 2 ){
                Title = Content.get(0).childNode(0).toString();
                SortName = Content.get(1).childNode(0).toString().split("_")[0];
            }
            if (Link.size() != 0 )
                Href = Link.attr("data-url");
            if (Time.size() != 0 )
                Date = Time.get(0).childNode(0).toString().replace("\n","");
            Icon = UserCourseInfo.FindCoursePhoto(1,SortName);
            UserBulletinInfo.setBulletin(1,new BulletinInfo(Icon,SortName,Title,STUST_URL+Href,Date));
        }
    }
}
