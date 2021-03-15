package WebCrawler.Thread.MessagePage;

import Data.CrawlerData.Common.Detail;

public interface MessageDialogCrawlerInterface {
    void CrawlerLoading(int text);
    void CrawlerSuccessful(Detail detail);
    void CrawlerFailed();
}
