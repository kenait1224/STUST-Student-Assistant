package WebCrawler.Crawler.Outline.FlipClass;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.CrawlerData.MessageData.MessageInfo;
import Data.StaticData.UserLoginCookies;
import Data.StaticData.UserLoginInfo;
import Data.StaticData.UserMessageInfo;
import WebCrawler.Crawler.Core.DataCrawler;

public class FlipClassMessageCrawler extends DataCrawler {
    Elements Message_Elements;
    String STUST_URL ="https://flipclass.stust.edu.tw";
    String LOGIN_URL = "https://flipclass.stust.edu.tw/service/loginagent/";
    String MESSAGE_URL = "https://flipclass.stust.edu.tw/dashboard/latestEvent";

    public FlipClassMessageCrawler() {
    }

    public FlipClassMessageCrawler(int page) {
        MESSAGE_URL += "?page="+String.valueOf(page);
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
            document = Jsoup.connect(MESSAGE_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(res.cookies())
                    .post();
            UserLoginCookies.setFlipClassLoginInfo(res.cookies());
        }catch (Exception e){
            System.out.println("Crawler Error : Flip Login Error, Please Check Account");
            e.printStackTrace();
        }
        if (document != null)
            Message_Elements = document.select("tbody");
    }

    @Override
    protected void Connect_Cookies(){
        Document document = null;
        try {
            document = Jsoup.connect(MESSAGE_URL)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .post();
        }catch (Exception e){
            System.out.println("Crawler Error : Flip Login Error, Please Re-Login");
            e.printStackTrace();
        }
        if (document != null)
            Message_Elements = document.select("tbody");
    }

    @Override
    public Boolean Successful() {
        return Message_Elements != null && Message_Elements.size() != 0;
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
        for (Element element: Message_Elements.get(0).children()){
            if(element.toString().indexOf("沒有資料") != -1)
                break;
            Elements Content = element.select("span.text");
            Elements Time = element.select("td.col-date").select("div.text-overflow");
            Element Link = element.selectFirst("a[href]");
            if (Content.size() == 2 ){
                Title = Content.get(0).childNode(0).toString();
                SortName = Content.get(1).childNode(0).toString().split("_")[0];
            }
            if (Link != null )
                Href = Link.attr("href");
            if (Time.size() != 0 )
                Date = Time.get(0).childNode(0).toString().replace("\n","");
            UserMessageInfo.setMessage(1,new MessageInfo(Title,STUST_URL+Href,SortName,Date));
        }
    }

}
