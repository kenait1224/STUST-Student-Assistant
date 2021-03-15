package WebCrawler.Crawler.Detail.Course.FlipClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import Data.StaticData.UserLoginCookies;
import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItem;
import RecyclerViewController.Course.ExpandableRecyclerView.Menu.CourseMenu;

public class FlipClassCourseDetailMenuCrawler {
    String Activities_URL;
    String Material_URL;
    String Assignment_URL;
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    ArrayList<CourseMenu> Course_Menu = new ArrayList<>();

    public FlipClassCourseDetailMenuCrawler(String COURSE_URL) {
        int Insert_Index = COURSE_URL.lastIndexOf("/");
        Activities_URL = COURSE_URL;
        Material_URL = COURSE_URL.substring(0, Insert_Index) + "/material" + COURSE_URL.substring(Insert_Index);
        Assignment_URL = COURSE_URL.substring(0, Insert_Index) + "/homeworkList" + COURSE_URL.substring(Insert_Index);
    }

    public ArrayList<CourseMenu> getCourse_Menu() {
        return Course_Menu;
    }

    public void UpdateActivities() {
        Course_Menu.add(new CourseMenu("課程活動", CrawlerActivities()));
    }

    public void UpdateMaterial() {
        Course_Menu.add(new CourseMenu("課程教材", CrawlerContent(Material_URL)));
    }

    public void UpdateAssignment() {
        Course_Menu.add(new CourseMenu("課程作業", CrawlerContent(Assignment_URL)));
    }

    private ArrayList<CourseItem> CrawlerActivities() {
        ArrayList<CourseItem> activities = new ArrayList<>();
        String Title;
        String Name = null;
        String Link = null;
        String Title_content_html = null;
        try {
            Document document = Jsoup.connect(Activities_URL)
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .post();
            Elements elements = document.select("li.xtree-node.type-group.clearfix");
            if (elements.size() != 0) {
                String Type;
                for (Element element : elements) {
                    if (element.childNodeSize() == 2) {
                        Title = "";
                        Elements TitleElements = element.child(0).select("div.node-title");
                        if (TitleElements.size() != 0 && TitleElements.get(0).childNodeSize() != 0)
                            Title = TitleElements.get(0).childNode(0).toString().replace("\n", "");
                        Type = (Title_content_html == null) ? "title" : "title_content";
                        if (Title != "")
                            activities.add(new CourseItem(Title, Type, Title_content_html, true));
                        for (Element bodyElement : element.child(1).select("li").select("div.header.hover")) {
                            Elements LinkElements = bodyElement.select("a[href]:not([href*=\"####\"],[href*=\"mobile\"])");
                            Elements TypeElements = bodyElement.select("span.font-icon.fs-fw.far");
                            if (LinkElements.size() != 0) {
                                Link = LinkElements.get(0).attr("href");
                                Name = LinkElements.get(0).child(0).childNode(0).toString();
                            }
                            if (TypeElements.size() != 0)
                                Type = getIconType(TypeElements.attr("class"));
                            if (Link != null && Name != null)
                                activities.add(new CourseItem(Name, Link, Type));
                        }
                    }
                }
            } else {
                activities.add(new CourseItem("查無資訊"));
            }
        } catch (Exception e) {
            System.out.println("Crawler Error : Activities Get Error");
            e.printStackTrace();
        }
        return activities;
    }

    private ArrayList<CourseItem> CrawlerContent(String Target) {
        ArrayList<CourseItem> content = new ArrayList<>();
        String Link = null;
        String Name = null;
        String Type = null;
        try {
            Document document = Jsoup.connect(Target)
                    .cookies(UserLoginCookies.getFlipClassLoginInfo())
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .post();
            for (Element element : document.select("tbody").select("tr")) {
                if (element.toString().indexOf("沒有資料") != -1) {
                    content.add(new CourseItem("查無資訊"));
                    return content;
                }
                Elements BodyElements = element.select("a[href]");
                Elements NameElements = BodyElements.select("span.text");
                Elements TypeElements = BodyElements.select("span.fs-iconfont");
                if (BodyElements.size() != 0)
                    Link = BodyElements.attr("href");
                if (NameElements.size() != 0)
                    Name = NameElements.get(0).childNode(0).toString().replace("\n", "");
                if (TypeElements.size() != 0 && Target.indexOf("homeworkList") == -1 )
                    Type = getIconType(TypeElements.attr("class"));
                else if (Target.indexOf("homeworkList") != -1)
                    Type = "exercise";
                if (Name != null && Link != null)
                    content.add(new CourseItem(Name, Link, Type));
            }
        } catch (Exception e) {
            System.out.println("Crawler Error : Content Get Error");
            e.printStackTrace();
        }
        return content;
    }

    private String getIconType(String type) {
        if (type.indexOf("file-alt") != -1)
            return "doc";
        if (type.indexOf("clipboard") != -1)
            return "exercise";
        if (type.indexOf("pdf") != -1)
            return "pdf";
        if (type.indexOf("globe") != -1)
            return "iframe";
        if (type.indexOf("youtube") != -1)
            return "embed";
        if (type.indexOf("file-audio") != -1)
            return "mp3";
        if (type.indexOf("font-play") != -1)
            return "everCam";
        return "default";
//        switch (type) {
//            case "外部網頁":
//                return "iframe";
//            case "PowerPoint":
//                return "ppt";
//            case "文件":
//                return "doc";
//            case "PDF":
//                return "pdf";
//            case "MP3":
//                return "mp3";
//            case "影片":
//                return "embed";
//            case "作業":
//                return "exercise";
//            case "作業完成":
//                return "exerciseSuccessful";
//            default:
//                return type;
//        }
    }
}
