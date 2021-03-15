package WebCrawler.Crawler.Detail.Course;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.Arrays;

import Data.CrawlerData.CourseDetailInfo.DetailInfoContent;

public class CourseDetailInfoCrawler {
    String Course_Href ;
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    ArrayList<String> infoList = new ArrayList<>(Arrays.asList("課程代碼","課程中文名稱","課程英文名稱","學分數","必選修","開課班級","任課教師","上課教室(時間)"));
    ArrayList<DetailInfoContent> target = new ArrayList<>();

    public CourseDetailInfoCrawler(String course_Href) {
        Course_Href = course_Href;
    }

    public ArrayList<DetailInfoContent> getTarget() {
        return target;
    }

    public void run(){
        try {
            Document document = Jsoup.connect(Course_Href)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .get();
            Element element = document.select("table.table").get(0);
            for (Node node : element.childNode(1).childNodes()) {
                if (infoList.size() == 0)
                    break;
                if (node instanceof Element && infoList.indexOf(node.childNode(1).childNode(0).toString()) != -1) {
                    String title = node.childNode(1).childNode(0).toString();
                    String content = "";
                    if (title.compareTo("上課教室(時間)") != 0) {
                        content = node.childNode(3).childNode(1).childNode(0).toString();
                    } else if (node.childNode(3).childNode(1).childNodeSize() > 1) {
                        for (Element items : ((Element) node).select("div tr")) {
                            for (Element item : items.select("span")) {
                                content += item.childNode(0).toString() + " ";
                            }
                            if (((Element) node).select("div tr").indexOf(items) != ((Element) node).select("div tr").size() - 1)
                                content += "\n";
                        }
                    }
                    target.add(new DetailInfoContent(title, content));
                    infoList.remove(title);
                }
            }
            target.add(new DetailInfoContent("全部資訊...", Course_Href));
        }catch (Exception e){
            System.out.println("Crawler Error : Detail Info Get Error");
            e.printStackTrace();
        }
    }
}
