package WebCrawler.Crawler.Detail.Course.Flip;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItem;
import RecyclerViewController.Course.ExpandableRecyclerView.Menu.CourseMenu;
import Data.StaticData.UserLoginCookies;

public class FlipCourseDetailMenuCrawler {
    String Activities_URL;
    String Material_URL;
    String Assignment_URL;
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    ArrayList<CourseMenu> Course_Menu = new ArrayList<>();

    public FlipCourseDetailMenuCrawler(String COURSE_URL) {
        Activities_URL = COURSE_URL + "/section/lecture/";
        Material_URL = COURSE_URL + "/section/doclist/";
        Assignment_URL = COURSE_URL + "/section/exercise?status=3";
    }

    public ArrayList<CourseMenu> getCourse_Menu() {
        return Course_Menu;
    }

    public void UpdateActivities(){
        Course_Menu.add(new CourseMenu("課程活動", CrawlerActivities()));
    }

    public void UpdateMaterial(){
        Course_Menu.add(new CourseMenu("課程教材", CrawlerContent(Material_URL)));
    }

    public void UpdateAssignment(){
        Course_Menu.add(new CourseMenu("課程作業", CrawlerContent(Assignment_URL)));
    }

    private ArrayList<CourseItem> CrawlerActivities() {
        ArrayList<CourseItem> activities = new ArrayList<>();
        String Title;
        String Title_content_html = null;
        try {
            Document document = Jsoup.connect(Activities_URL)
                    .cookies(UserLoginCookies.getFlipLoginInfo())
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .post();
            Elements elements = document.select("li.chapter");
            if (elements.size() != 0) {
                String Type;
                for (Element element : elements) {
                    Title = "";
                    for (Node node : element.childNode(1).childNodes()) {
                        if (node instanceof Element) {
                            if (((Element) node).selectFirst("div.desc") != null)
                                Title_content_html = ((Element) node).selectFirst("div.desc").toString();
                            else
                                Title += ((Element) node).selectFirst("span").childNode(0).toString();
                        }
                    }
                    Type = (Title_content_html.compareTo("<div class=\"desc\"></div>") == 0) ? "title" : "title_content";
                    if (Title != "")
                        activities.add(new CourseItem(Title, Type, Title_content_html, true));
                    System.out.println(Type);
                    for (Node node : element.childNode(3).childNodes()) {
                        if (node instanceof Element) {
                            String Link = ((Element) node).select("a[href]").get(0).attr("href");
                            String Name = ((Element) node).select("a[href]").get(0).selectFirst("span.value").attr("title");
                            Type = node.attr("data-playtype");
                            if(Type.compareTo("exercise") == 0 || Type.compareTo("exerciseSuccessful") == 0)
                                Link+="?popup=true";
                            if (Link != null && Name != null)
                                activities.add(new CourseItem(Name, Link, Type));
                        }
                    }
                }
            } else {
                activities.add(new CourseItem("查無資訊"));
            }
        }catch (Exception e){
            System.out.println("Crawler Error : Activities Get Error");
            e.printStackTrace();
        }
        return activities;
    }

    private ArrayList<CourseItem> CrawlerContent(String NextPage) {
        ArrayList<CourseItem> content = new ArrayList<>();
        if (NextPage == "Last")
            return content;
        else {
            try {
                ArrayList<Boolean> checked = new ArrayList<>();
                Document document = Jsoup.connect(NextPage)
                        .cookies(UserLoginCookies.getFlipLoginInfo())
                        .header(USER_AGENT, USER_AGENT_VALUE)
                        .post();
                if (NextPage == Assignment_URL) {
                    if (document.select("tr#noData").size() == 1){
                        content.add(new CourseItem("查無資訊"));
                        return content;
                    }
                    Elements elements = document.select("tbody#table-exerciseTbody");
                    for ( Node element:elements.get(0).childNodes()){
                        if (element instanceof Element)
                            checked.add((((Element) element).child(3).selectFirst("img") == null)?false:true);
                    }
                }
                Elements elements = document.select("td.td.major");
                String Next = (document.selectFirst("li.fs-page-next.fs-page-content a[href]") != null)?
                        "https://flip.stust.edu.tw"+document.selectFirst("li.fs-page-next.fs-page-content a[href]").attr("href"):"Last";
                if (elements.size() != 0) {
                    String Type;
                    String Name;
                    String Link;
                    int Count = 0;
                    for (Element element : elements) {
                        if(element.selectFirst("div.placeholder")!=null) {
                            Type = getIconType((element.selectFirst("div.placeholder").attr("title") + ((NextPage == Assignment_URL && checked.get(Count++))?"完成":"")));
                            Name = element.selectFirst("a[href]").attr("title");
                            Link = element.selectFirst("a[href]").attr("href");
                            if(Type.compareTo("exercise") == 0 || Type.compareTo("exerciseSuccessful") == 0)
                                Link+="?popup=true";
                            content.add(new CourseItem(Name, Link, Type));
                        }
                    }
                } else {
                    content.add(new CourseItem("查無資訊"));
                }
                content.addAll(CrawlerContent(Next));
            }catch (Exception e){
                System.out.println("Crawler Error : Content Get Error");
                e.printStackTrace();
            }
            return content;
        }
    }

    private String getIconType(String type) {
        switch (type) {
            case "外部網頁":
                return "iframe";
            case "PowerPoint":
                return "ppt";
            case "文件":
                return "doc";
            case "PDF":
                return "pdf";
            case "MP3":
                return "mp3";
            case "影片":
                return "embed";
            case "作業":
                return "exercise";
            case "作業完成":
                return "exerciseSuccessful";
            default:
                return type;
        }
    }
}
