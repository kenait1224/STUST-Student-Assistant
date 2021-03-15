package WebCrawler.Crawler.Detail.Course.FlipClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import Data.CrawlerData.Common.Attachment;
import Data.CrawlerData.Common.Detail;
import Data.StaticData.UserLoginCookies;
import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItem;

public class FlipClassCourseDialogCrawler {
    ArrayList<Attachment> AttachmentLink = new ArrayList<>();
    ArrayList<String> pptImageLink = new ArrayList<>();
    String HyperLink = "";
    String Html_Content = "";
    String Post_Time;
    String Views;
    String Target_URL = "https://flipclass.stust.edu.tw";
    String CrawlerType;
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    Detail TargetDetail;
    CourseItem Target;

    public FlipClassCourseDialogCrawler(CourseItem target) {
        Target = target;
        CrawlerType = getCrawlerType();
    }

    public Detail getTargetDetail() {
        return TargetDetail;
    }

    public void run() {
        try {
            Document document = Jsoup.connect(Target_URL + Target.Link)
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .post();
            Common_Crawler(document);

//            switch (CrawlerType) {
//                case "Common":
//                    Common_Crawler(document);
//                    break;
//                case "Document":
////                    Document_Crawler(document);
//                    break;
//                case "Exercise":
////                    Exercise_Crawler(document);
//            }
        } catch (Exception e) {
            System.out.println("Crawler Error : Flip Connection Failed");
            e.printStackTrace();
        }
    }

    private void Common_Crawler(Document document){
        try {
            getTargetPostInfo(document);
            getTargetContent(document);
            getTargetAttachment(document);
            getHyperLink(document);
            TargetDetail = new Detail(AttachmentLink, Target.Name, Html_Content, Post_Time, Views, HyperLink);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getTargetPostInfo(Document document) {
        Elements elements;
        try {
            elements = document.select("div.module.mod_media[class*=\"Title\"]");
            if ( elements.size() != 0 ) {
                String TitleText = elements.get(0).selectFirst("div.fs-hint").childNode(0).toString().replace("\n","");
                if(TitleText.split(",").length > 2 ){
                    Views = TitleText.split(",")[0];
                    Post_Time = TitleText.split(",")[1];
                }
                else{
                    Post_Time = "查無資料";
                    Views = "查無資料";
                }
                if (Target.Type.compareTo("embed") == 0)
                    Post_Time = Post_Time.split(",")[0];
            }
        } catch (Exception e) {
            Post_Time = "查無資料";
            Views = "查無資料";
        }
    }

    private void getTargetContent(Document document) {
        Element element;
        switch (CrawlerType) {
            case "Common":
                element = document.selectFirst("div#xbox-inline div.module").select("div.fs-block-body").last();
                if (element != null)
                    Html_Content = element.toString();
                break;
            case "Media":
                element =document.selectFirst("div#xtop-inline div[class*=media-intro]");
                if (element != null)
                    Html_Content = element.toString();
                break;
//            case "Exercise":
//                elements = document.select("ul.infoList");
//                for (int i = 0; i < elements.get(0).children().size(); i++) {
//                    if (elements.get(0).child(i).children().get(0).toString().indexOf("附件") != -1) {
//                        elements.get(0).child(i).remove();
//                        break;
//                    }
//                }
//                Html_Content = elements.toString();
//                break;
        }
    }


    private void getTargetAttachment(Document document) {
        Elements elements;
        switch (CrawlerType) {
            case "Common":
                elements = document.selectFirst("div#xbox-inline div[class*=attachlist]").select("a");
                for (Node node : elements){
                    if(node instanceof Element){
                        String Href = Target_URL + node.attr("href");
                        String Name = (((Element) node).select("span.text") != null)?((Element) node).selectFirst("span.text").childNode(0).toString():null;
                        AttachmentLink.add(new Attachment(Name,Href));
                    }
                }
                break;
            case "Media":
                elements = document.selectFirst("div#xtop-inline div[class*=attachlist]").select("a");
                for (Node node : elements){
                    if(node instanceof Element){
                        String Href = Target_URL + node.attr("href");
                        String Name = (((Element) node).select("span.text") != null)?((Element) node).selectFirst("span.text").childNode(0).toString():null;
                        AttachmentLink.add(new Attachment(Name,Href));
                    }
                }
                break;
            case "Exercise":
                break;
        }
    }

    private void getHyperLink(Document document){
        Elements elements;
        switch (Target.Type) {
            case "everCam":
                break;
            case "embed":
                boolean isSource = false;
                elements = document.select("dl.dl-horizontal.label-width-md");
                if (elements.size() != 0){
                    elements = document.select("dl.dl-horizontal.label-width-md").get(0).children();
                    for(int i = 0 ; i < elements.size() ; i++){
                        if(isSource){
                            HyperLink = elements.get(i).select("a").attr("href");
                            break;
                        }
                        else{
                            if( elements.get(i).toString().indexOf("來源") != -1)
                                isSource = true;
                        }
                    }
                }
                break;
            case "pdf":
                elements = document.select("object");
                if (elements.size() != 0)
                    HyperLink = Target_URL + elements.select("object").attr("data").split("#")[0];
                break;
            case "mp3":
                int start = document.toString().indexOf("fs.audio.init(\"audioPlaceHolder\",") + "fs.audio.init(\"audioPlaceHolder\",".length() + 1;
                if (start != "fs.audio.init(\"audioPlaceHolder\",".length())
                    HyperLink = Target_URL + document.toString().substring(start,document.toString().indexOf("\"",start));
                break;
            case "iframe":
                elements = document.select("iframe");
                if (elements.size() != 0)
                    HyperLink = document.select("iframe").attr("src");
                break;
        }
    }

    private String getCrawlerType() {
        switch (Target.Type) {
            case "exercise":
                return "Exercise";
            case "everCam":
            case "embed":
                return "Media";
            case "doc":
            case "pdf":
            case "mp3":
            case "iframe":
                return "Common";
            default:
                return "Other";
        }
    }
}
