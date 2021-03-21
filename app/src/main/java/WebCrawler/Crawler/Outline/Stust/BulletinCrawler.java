package WebCrawler.Crawler.Outline.Stust;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.CrawlerData.BulletinData.BulletinInfo;
import Data.StaticData.UserBulletinInfo;
import WebCrawler.Crawler.Core.DataCrawler;

public class BulletinCrawler extends DataCrawler {


    Elements Bulletin_Elements;
    String Bulletin_URL = "https://news.stust.edu.tw";
    String Page;

    public BulletinCrawler() {
    }

    public BulletinCrawler(int page) {
        Page = String.valueOf(page);
    }

    @Override
    protected void Connect_UserInfo() {
        Document document = null;
        try {
            document = Jsoup.connect(Bulletin_URL + "/User/RwdNewsList.aspx?page=" + ((Page == null) ? "1" : Page))
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .post();
        } catch (Exception e) {
            System.out.println("Crawler Error : STUST Web Error ");
            e.printStackTrace();
        }
        if (document != null)
            Bulletin_Elements = document.select("tbody").get(0).children();
    }

    @Override
    protected void Connect_Cookies() {

    }

    @Override
    public Boolean Successful() {
        return Bulletin_Elements != null && Bulletin_Elements.size() != 0;
    }

    @Override
    public void Getting() {
        Connect_UserInfo();
    }

    @Override
    public void Building() {
        String SortName = "";
        String Title = "";
        String Icon = "";
        String Href = "";
        String Date = "";
        for (Element element : Bulletin_Elements) {
            Elements Link = element.select("a[id*=HyperLink5]");
            Elements Sort = element.select("span[id*=sortname]");
            Elements Time = element.select("span[id*=time]");
            if (Link.size() != 0) {
                Title = Link.get(0).attr("title");
                Href = Bulletin_URL + trim(Link.get(0).attr("href"));
            }
            if (Sort.size() != 0)
                SortName = Sort.get(0).childNode(0).toString();
            if (Time.size() != 0)
                Date = Time.get(0).childNode(0).toString().replace("/", "-");
            Icon = element.select("img").attr("alt");
            UserBulletinInfo.setBulletin(2, new BulletinInfo(Icon, SortName, Title, Href, Date));
            System.out.println("");
        }
    }

    public String trim(String src) {
        int len = src.length();
        int st = 0;

        while ((st < len) && (src.charAt(st) <= '.')) {
            st++;
        }
        while ((st < len) && (src.charAt(len - 1) <= '.')) {
            len--;
        }
        return ((st > 0) || (len < src.length())) ? src.substring(st, len) : src;
    }
}
