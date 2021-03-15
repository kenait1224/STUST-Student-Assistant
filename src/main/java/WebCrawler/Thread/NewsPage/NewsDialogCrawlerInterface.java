package WebCrawler.Thread.NewsPage;

import Data.CrawlerData.Common.Detail;

public interface NewsDialogCrawlerInterface {
    void CrawlerLoading(int text);
    void CrawlerSuccessful(Detail detail);
    void CrawlerFailed();
}
