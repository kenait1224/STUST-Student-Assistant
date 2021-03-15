package WebCrawler.Thread.CoursePage;

import WebCrawler.Crawler.Detail.Course.CourseDetailInfoCrawler;
import activity.R;

public class CourseDetailInfoCrawlerThread extends Thread{
    CourseDetailInfoCrawlerInterface courseDetailInfoCrawlerInterface;
    CourseDetailInfoCrawler courseDetailInfoCrawler;

    public CourseDetailInfoCrawlerThread(CourseDetailInfoCrawlerInterface courseDetailInfoCrawlerInterface, String href) {
        this.courseDetailInfoCrawlerInterface = courseDetailInfoCrawlerInterface;
        courseDetailInfoCrawler = new CourseDetailInfoCrawler(href);
    }

    @Override
    public void run() {
        try {
            courseDetailInfoCrawlerInterface.CrawlerLoading(R.string.DetailInfoLoading);
            courseDetailInfoCrawler.run();
            courseDetailInfoCrawlerInterface.CrawlerSuccessful(courseDetailInfoCrawler.getTarget());
        }catch (Exception e){
            courseDetailInfoCrawlerInterface.CrawlerFailed();
        }
    }
}
