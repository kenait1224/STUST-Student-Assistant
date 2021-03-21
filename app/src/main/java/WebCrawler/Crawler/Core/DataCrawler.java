package WebCrawler.Crawler.Core;

abstract public class DataCrawler {

    protected String USER_AGENT = "User-Agent";
    protected String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";

    protected abstract void Connect_UserInfo();
    protected abstract void Connect_Cookies();
    public abstract Boolean Successful();
    public abstract void Getting();
    public abstract void Building();
}
