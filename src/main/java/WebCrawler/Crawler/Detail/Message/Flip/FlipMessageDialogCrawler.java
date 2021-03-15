package WebCrawler.Crawler.Detail.Message.Flip;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import Data.CrawlerData.Common.Attachment;
import Data.CrawlerData.Common.Detail;
import Data.CrawlerData.MessageData.MessageInfo;
import Data.StaticData.UserLoginCookies;

public class FlipMessageDialogCrawler {

    ArrayList<Attachment> AttachmentLink = new ArrayList<>();
    String Html_Content = "";
    String Post_Time = "查無資料";
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    Detail TargetDetail;
    MessageInfo Target;

    public FlipMessageDialogCrawler(MessageInfo target) {
        Target = target;
    }

    public Detail getTargetDetail() {
        return TargetDetail;
    }

    public void run() {
        try {
            Document document = Jsoup.connect(Target.getHref())
                    .cookies(UserLoginCookies.getFlipLoginInfo())
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
        getTargetAttachment(document);
        TargetDetail = new Detail(AttachmentLink,Target.getTitle(),Html_Content,Post_Time);
    }

    private String getAttachmentJsonString(String target) {
        String StartKeyWord = "$.uploader(";
        String EndKeyWord = ");";
        int start = target.indexOf(StartKeyWord) + StartKeyWord.length();
        int end = target.indexOf(EndKeyWord, start);
        return target.substring(start, end);
    }

    /*目標提供訊息，以 HTML 字串儲存，並避開附件列表*/
    private void getTargetContent(Document document) {
        Elements elements = document.select("ul.infoList");
        for (int i = 0; i < elements.get(0).children().size(); i++) {
            if (elements.get(0).child(i).children().get(0).toString().indexOf("附件") != -1) {
                elements.get(0).child(i).remove();
                break;
            }
        }
        Html_Content = elements.toString();
    }


    /*目標附件名稱、連結*/
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
}
