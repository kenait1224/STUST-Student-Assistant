package Data.CrawlerData.BulletinData;

public class BulletinInfo {
    String SortName;
    String Title;
    String Href;
    String Date;
    String Icon;

    public BulletinInfo(String icon ,String className, String title, String href, String date) {
        Href = href;
        SortName = className;
        Title = title;
        Icon = icon;
        Date = date;
    }

    public String getSortName() {
        return SortName;
    }

    public String getTitle() {
        return Title;
    }

    public String getIcon() {
        return Icon;
    }

    public String getHref() {
        return Href;
    }

    public String getDate() {
        return Date;
    }
}
