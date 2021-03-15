package WebCrawler.Crawler.Detail.Course.Flip;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItem;
import Data.CrawlerData.Common.Attachment;
import Data.CrawlerData.Common.Detail;
import Data.StaticData.UserLoginCookies;

public class FlipCourseDialogCrawler {

    ArrayList<Attachment> AttachmentLink = new ArrayList<>();
    ArrayList<String> pptImageLink = new ArrayList<>();
    String HyperLink = "";
    String Html_Content = "";
    String Post_Time;
    String Views;
    String Target_URL = "https://flip.stust.edu.tw";
    String CrawlerType;
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    Detail TargetDetail;
    CourseItem Target;

    public FlipCourseDialogCrawler(CourseItem target) {
        Target = target;
        CrawlerType = getCrawlerType();
    }

    public Detail getTargetDetail() {
        return TargetDetail;
    }

    public void run() {
        try {
            Document document = Jsoup.connect(Target_URL + Target.Link)
                    .cookies(UserLoginCookies.getFlipLoginInfo())
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .post();
            switch (CrawlerType) {
                case "Common":
                    Common_Crawler(document);
                    break;
                case "Document":
                    Document_Crawler(document);
                    break;
                case "Exercise":
                    Exercise_Crawler(document);
            }
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Connection Failed");
            e.printStackTrace();
        }
    }


    private String getCrawlerType() {
        switch (Target.Type) {
            case "doc":
                return "Document";
            case "exercise":
            case "exerciseSuccessful":
                return "Exercise";
            case "ppt":
            case "hyperlink":
            case "embed":
            case "pdf":
            case "mp3":
            case "iframe":
                return "Common";
            default:
                return "Other";
        }
    }

    private String getHyperLink(Elements content) {
        if (Target.Type.compareTo("pdf") == 0)
            return Target_URL + content.select("div#pdfContainer").select("object").attr("data").split("#")[0];
        if (Target.Type.compareTo("mp3") == 0)
            return Target_URL + content.select("audio").select("source").attr("src");
        if (Target.Type.compareTo("iframe") == 0)
            return content.select("iframe[src]").attr("src");
        return "";
    }

    private String getAttachmentJsonString(String target) {
        String StartKeyWord = "$.uploader(";
        String EndKeyWord = ");";
        int start = target.indexOf(StartKeyWord) + StartKeyWord.length();
        int end = target.indexOf(EndKeyWord, start);
        return target.substring(start, end);
    }

    private String getHyperlinkJsonString(String target) {
        String StartKeyWord = "var jsMediaInfo = ";
        String EndKeyWord = "};";
        int start = target.indexOf(StartKeyWord) + StartKeyWord.length();
        int end = target.indexOf(EndKeyWord, start);
        return target.substring(start, end + 1);
    }

    /*目標發布時間、閱覽次數*/
    private void getTargetPostInfo(Document document) {
        Element elements;
        try {
            Post_Time = document.select("div[class=module] div.hint").get(0).childNode(0).toString().replace("\n", "");
            Views = document.select("div[class=module] div.hint").get(1).childNode(0).toString().replace("\n", "");
            if (Target.Type.compareTo("embed") == 0)
                Post_Time = Post_Time.split(",")[0];
        } catch (Exception e) {
            Post_Time = "查無資料";
            Views = "查無資料";
        }
    }

    /*目標提供訊息，以 HTML 字串儲存，並避開附件列表*/
    private void getTargetContent(Document document) {
        Elements elements;
        switch (CrawlerType) {
            case "Common":
                elements = document.select("div[class=module] div.body");
                for (Node node : elements) {
                    if (node instanceof Element) {
                        if (node.toString().indexOf("hint") == -1 && node.toString().indexOf("style=\"margin-bottom: -20px;\"") == -1)
                            Html_Content += node.toString();
                    }
                }
                break;

            case "Document":
                elements = document.select("div#noteBox div:not([class])");
                if (elements != null)
                    Html_Content = elements.toString();
                break;

            case "Exercise":
                elements = document.select("ul.infoList");
                for (int i = 0; i < elements.get(0).children().size(); i++) {
                    if (elements.get(0).child(i).children().get(0).toString().indexOf("附件") != -1) {
                        elements.get(0).child(i).remove();
                        break;
                    }
                }
                Html_Content = elements.toString();
                break;
        }
    }

    /*目標附件名稱、連結*/
    private void getTargetAttachment(Document document) {
        Elements elements;
        switch (CrawlerType) {
            case "Common":
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
                break;
            case "Document":
                try {
                    String Uploader_Json = getAttachmentJsonString(document.select("script:not([src])").get(4).toString());
                    JSONObject AttachmentJson = new JSONObject(Uploader_Json);
                    JSONArray Data = AttachmentJson.getJSONObject("data").getJSONArray("original");
                    for (int i = 0; i < Data.length(); i++) {
                        AttachmentLink.add(new Attachment(Data.getJSONObject(i).getString("filename"),
                                Data.getJSONObject(i).getString("cdnDownloadUrl").replace("\\", "")));
                    }
                } catch (Exception e) {
                    System.out.println("Crawler Error : Document Attachment Get Error");
                    e.printStackTrace();
                }
                break;
            case "Exercise":
                elements = document.select("ul.infoList");
                for (int i = 0; i < elements.get(0).children().size(); i++) {
                    if (elements.get(0).child(i).children().get(0).toString().indexOf("附件") != -1) {
                        elements.get(0).child(i).remove();
                        break;
                    }
                }
                Html_Content = elements.toString();
                break;
        }
    }

    private void getHyperLink(Document document) {
        try {

            if (Target.Type.compareTo("hyperlink") == 0 || Target.Type.compareTo("embed") == 0) {
                String Uploader_Json = getHyperlinkJsonString(document.select("script:not([src])").get(4).toString());
                JSONObject AttachmentJson = new JSONObject(Uploader_Json);
                HyperLink = AttachmentJson.get("srcFrom").toString();
            } else if (Target.Type.compareTo("ppt") == 0) {
                for (Element element : document.select("div#xbox").select("img[src]"))
                    pptImageLink.add(Target_URL + element.attr("src"));

            } else
                HyperLink = getHyperLink(document.select("div#xbox"));
        } catch (Exception e) {
            System.out.println("Crawler Error : getHyperLink Failed");
            e.printStackTrace();
        }
    }

    private void Common_Crawler(Document document) {
        try {
            /*目標發布時間、閱覽次數*/
            getTargetPostInfo(document);
            /*目標提供訊息，以 HTML 字串儲存，並避開附件列表*/
            getTargetContent(document);
            /*目標附件名稱、連結*/
            getTargetAttachment(document);
            /*目標連結*/
            getHyperLink(document);

            if (pptImageLink.size() != 0)
                TargetDetail = new Detail(AttachmentLink, Target.Name, Html_Content, Post_Time, Views, pptImageLink);
            else
                TargetDetail = new Detail(AttachmentLink, Target.Name, Html_Content, Post_Time, Views, HyperLink);
        } catch (Exception e) {
            System.out.println("Crawler Error : Common Crawler Failed");
            e.printStackTrace();
        }
    }

    private void Document_Crawler(Document document) {
        try {
            /*目標發布時間、閱覽次數*/
            getTargetPostInfo(document);
            /*目標提供訊息，以 HTML 字串儲存，並避開附件列表*/
            getTargetContent(document);
            /*目標附件名稱、連結*/
            getTargetAttachment(document);
            TargetDetail = new Detail(AttachmentLink, Target.Name, Html_Content, Post_Time, Views);
        } catch (Exception e) {
            System.out.println("Crawler Error :  Document Crawler Failed");
            e.printStackTrace();
        }
    }

    private void Exercise_Crawler(Document document) {
        try {
            /*目標提供訊息，以 HTML 字串儲存，並避開附件清單加入至 AttachmentLink*/
            getTargetContent(document);
            TargetDetail = new Detail(AttachmentLink, Target.Name, Html_Content, Post_Time, Views);
        } catch (Exception e) {
            System.out.println("Crawler Error : Exercise Crawler Failed");
            e.printStackTrace();
        }
    }
}

