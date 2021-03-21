package Data.CrawlerData.CourseDetailInfo;

public class DetailInfoContent {
    String Title;
    String Content;

    public DetailInfoContent(String title, String content) {
        this.Title = title;
        this.Content = content;
    }

    public String getContent() {
        return Content;
    }

    public String getTitle() {
        return Title;
    }
}
