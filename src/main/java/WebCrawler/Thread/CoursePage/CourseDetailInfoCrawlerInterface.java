package WebCrawler.Thread.CoursePage;


import java.util.ArrayList;

import Data.CrawlerData.CourseDetailInfo.DetailInfoContent;

public interface CourseDetailInfoCrawlerInterface {
    void CrawlerLoading(int text);
    void CrawlerSuccessful(ArrayList<DetailInfoContent> Target);
    void CrawlerFailed();
}
