package Data.CrawlerData.MessageData;

public class MessageInfo {
    private String Title;
    private String Href;
    private String SortName;
    private String DeadLine;

    public MessageInfo(String title, String href, String sortName, String deadLine) {
        Title = title;
        Href = href + "?popup=true";
        SortName = sortName;
        DeadLine = deadLine;
    }

    public String getTitle() {
        return Title;
    }

    public String getHref() {
        return Href;
    }

    public String getSortName() {
        return SortName;
    }

    public String getDeadLine() {
        return DeadLine;
    }
}
