package WebCrawler.Crawler.Detail.News.FlipClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import Data.CrawlerData.BulletinData.BulletinInfo;
import Data.CrawlerData.Common.Attachment;
import Data.CrawlerData.Common.Detail;
import Data.StaticData.UserLoginCookies;

public class FlipClassNewsDialogCrawler {
    ArrayList<Attachment> AttachmentLink = new ArrayList<>();
    String Html_Content = "";
    String Post_Time = "查無資料";
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    Detail TargetDetail;
    BulletinInfo Target;

    public FlipClassNewsDialogCrawler(BulletinInfo target) {
        Target = target;
    }

    public Detail getTargetDetail() {
        return TargetDetail;
    }

    public void run() {
        try {
            Document document = Jsoup.connect(Target.getHref())
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .post();
            BuildTargetDetail(document);

        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Connection Failed");
            e.printStackTrace();
        }
    }

    private void BuildTargetDetail(Document document) {
        getTargetContent(document);
        TargetDetail = new Detail(AttachmentLink, Target.getTitle(), Html_Content, Post_Time);
    }



    /*目標提供訊息，以 HTML 字串儲存，並避開附件列表*/
    private void getTargetContent(Document document) {
        Elements elements = document.select("div.module.app-bulletin");
        if (elements.size() != 0) {
            Elements post = elements.get(0).select("div.modal-iframe-ext2");
            if (post.size() != 0 && post.get(0).childNodeSize() != 0)
                Post_Time = post.get(0).childNodes().get(0).toString().replace("\n","");

            for (int i = 0 ; i < elements.get(0).child(0).childNodeSize() ; i++){
                String content = elements.get(0).child(0).child(i).toString();
                Html_Content +=  (content.indexOf("modal-iframe-ext2") != -1) ? "" : content;
            }
        }
    }

    /*目標附件名稱、連結*/
    /*
    private void getTargetAttachment(Document document) {
        try {
            String Uploader_Json = getAttachmentJsonString(document.select("script:not([src])").get(4).toString());
            JSONObject AttachmentJson = new JSONObject(Uploader_Json);
            JSONArray Data = AttachmentJson.getJSONObject("data").getJSONArray("original");
            for (int i = 0; i < Data.length(); i++) {
                AttachmentLink.add(new Attachment(Data.getJSONObject(i).getString("filename"),
                        Data.getJSONObject(i).getString("cdnDownloadUrl").replace("\\", "")));
            }
        } catch (Exception e) {
            System.out.println("Crawler Error : Common Attachment Get Error");
        }
    }

    private String getAttachmentJsonString(String target) {
        String StartKeyWord = "$.uploader(";
        String EndKeyWord = ");";
        int start = target.indexOf(StartKeyWord) + StartKeyWord.length();
        int end = target.indexOf(EndKeyWord, start);
        return target.substring(start, end);
    }
    */
}
