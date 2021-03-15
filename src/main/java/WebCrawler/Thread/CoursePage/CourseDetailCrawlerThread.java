package WebCrawler.Thread.CoursePage;

import Data.StaticData.UserCourseDetail;
import WebCrawler.Crawler.Detail.Course.Flip.FlipCourseDetailMenuCrawler;
import WebCrawler.Crawler.Detail.Course.FlipClass.FlipClassCourseDetailMenuCrawler;
import activity.R;

public class CourseDetailCrawlerThread extends Thread {

    CourseDetailCrawlerInterface courseDetailCrawlerInterface;
    FlipCourseDetailMenuCrawler flipCourseDetailMenuCrawler;
    FlipClassCourseDetailMenuCrawler flipClassCourseDetailMenuCrawler;
    String Course_Name;
    int Source;

    public CourseDetailCrawlerThread(CourseDetailCrawlerInterface courseDetailCrawlerInterface,int source,String courseName, String COURSE_URL) {
        this.courseDetailCrawlerInterface = courseDetailCrawlerInterface;
        flipCourseDetailMenuCrawler = new FlipCourseDetailMenuCrawler(COURSE_URL);
        flipClassCourseDetailMenuCrawler = new FlipClassCourseDetailMenuCrawler(COURSE_URL);
        Course_Name = courseName;
        Source = source;
    }

    @Override
    public void run() {
        try {
            if (UserCourseDetail.getCourseDetail(Source) == null || UserCourseDetail.getCourseDetail(Source).get(Course_Name) == null) {
                // 課程活動載入
                UpdateActivities();
                //課程教材載入
                UpdateMaterial();
                //課程作業載入
                UpdateAssignment();
                UserCourseDetail.getCourseDetail(Source).put(Course_Name, (Source == 0)?flipCourseDetailMenuCrawler.getCourse_Menu():flipClassCourseDetailMenuCrawler.getCourse_Menu());
            }
            courseDetailCrawlerInterface.CrawlerSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            courseDetailCrawlerInterface.CrawlerFailed();
        }
    }

    private void UpdateActivities(){
        courseDetailCrawlerInterface.CrawlerLoading(R.string.ActivitesLoading);
        if(Source == 0)
            flipCourseDetailMenuCrawler.UpdateActivities();
        else
            flipClassCourseDetailMenuCrawler.UpdateActivities();
    }

    private void UpdateMaterial(){
        courseDetailCrawlerInterface.CrawlerLoading(R.string.MaterialLoading);
        if(Source == 0)
            flipCourseDetailMenuCrawler.UpdateMaterial();
        else
            flipClassCourseDetailMenuCrawler.UpdateMaterial();
    }

    private void UpdateAssignment(){
        courseDetailCrawlerInterface.CrawlerLoading(R.string.AssignmentLoading);
        if(Source == 0)
            flipCourseDetailMenuCrawler.UpdateAssignment();
        else
            flipClassCourseDetailMenuCrawler.UpdateAssignment();
    }
}
