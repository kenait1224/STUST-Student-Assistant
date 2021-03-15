package Data.CrawlerData.Common;

import java.util.ArrayList;

public class Detail {
    ArrayList<Attachment> AttachmentLink;
    ArrayList<String> ImageLinkList;
    String Name;
    String Post_Time;
    String Views;
    String Html_Content = "<h6>沒有提供相關訊息，請查閱附件清單。</h6>";
    String HyperLink;

    public Detail(String name,String html_Content) {
        Name = name;
        if(html_Content.compareTo("") != 0)
            Html_Content = html_Content;
    }

    public Detail(ArrayList<Attachment> attachmentLink,String name, String html_Content , String post_Time) {
        Name = name;
        Post_Time = post_Time;
        AttachmentLink = attachmentLink;
        if(html_Content.compareTo("") != 0)
            Html_Content = html_Content;
    }

    public Detail(ArrayList<Attachment> attachmentLink,String name, String html_Content , String post_Time, String views) {
        Name = name;
        Views = views;
        Post_Time = post_Time;
        AttachmentLink = attachmentLink;
        if(html_Content.compareTo("") != 0)
            Html_Content = html_Content;
    }

    public Detail(ArrayList<Attachment> attachmentLink,String name, String html_Content, String post_Time, String views, String hyperLink) {
        Name = name;
        Views = views;
        Post_Time = post_Time;
        AttachmentLink = attachmentLink;
        HyperLink = hyperLink;
        if(html_Content.compareTo("") != 0)
            Html_Content = html_Content;

    }

    public Detail(ArrayList<Attachment> attachmentLink,String name, String html_Content, String post_Time, String views, ArrayList<String> imageLinkList) {
        Name = name;
        Views = views;
        Post_Time = post_Time;
        AttachmentLink = attachmentLink;
        ImageLinkList = imageLinkList;
        if(html_Content.compareTo("") != 0)
            Html_Content = html_Content;
    }

    public ArrayList<Attachment> getAttachmentLink() {
        return AttachmentLink;
    }

    public ArrayList<String> getImageLinkList() {
        return ImageLinkList;
    }


    public String getPost_Time() {
        return Post_Time;
    }

    public String getViews() {
        return Views;
    }

    public String getHtml_Content() {
        return Html_Content;
    }

    public String getHyperLink() {
        return HyperLink;
    }

    public String getName() {
        return Name;
    }
}
