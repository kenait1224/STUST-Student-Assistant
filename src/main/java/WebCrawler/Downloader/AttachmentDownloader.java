package WebCrawler.Downloader;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;

import Data.StaticData.UserLoginCookies;

public class AttachmentDownloader extends DownloadManager.Request {
    String USER_AGENT = "User-Agent";
    String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
    String FileName;
    /**
     * @param uri the HTTP or HTTPS URI to download.
     */
    public AttachmentDownloader(int source ,String uri, String title) {
        super(Uri.parse(uri));
        setFileName(source,uri,title);
        this.setTitle(title)
                .setDescription("下載 " + title + " 中")
                .addRequestHeader(USER_AGENT, USER_AGENT_VALUE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setVisibleInDownloadsUi(false)
                .setAllowedOverMetered(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FileName);
        if(source == 0)
            this.addRequestHeader("Cookie", "PHPSESSID=" + UserLoginCookies.getFlipLoginInfo().get("PHPSESSID") + ";STUST-LB=" + UserLoginCookies.getFlipLoginInfo().get("STUST-LB"));
        else
            this.addRequestHeader("Cookie", "PHPSESSID=" + UserLoginCookies.getFlipClassLoginInfo().get("PHPSESSID") + ";STUST-LB=" + UserLoginCookies.getFlipClassLoginInfo().get("STUST-LB"));

    }

    private void setFileName(int source ,String Link , String title_name){
        String File_Name;
        String File_Type;
        if(source == 0) {
            File_Name = Link.substring(Link.lastIndexOf("/") + 1, Link.lastIndexOf("."));
            File_Type = Link.substring(Link.lastIndexOf("."), Link.length()).toLowerCase();
            FileName = ((File_Name.compareTo("pdf") == 0) ? title_name : File_Name) + File_Type;
        }
        else
            FileName = title_name;
    }

    public String getFileName() {
        return FileName;
    }
}
