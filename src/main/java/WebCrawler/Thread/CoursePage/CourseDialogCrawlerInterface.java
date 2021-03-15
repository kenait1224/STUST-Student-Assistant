package WebCrawler.Thread.CoursePage;

import Data.CrawlerData.Common.Detail;

public interface CourseDialogCrawlerInterface {
    void CrawlerLoading(int text);
    void CrawlerSuccessful(Detail detail);
    void CrawlerFailed();
}
