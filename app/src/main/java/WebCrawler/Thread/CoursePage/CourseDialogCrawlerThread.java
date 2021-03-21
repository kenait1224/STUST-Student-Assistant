package WebCrawler.Thread.CoursePage;

import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItem;
import Data.CrawlerData.Common.Detail;
import WebCrawler.Crawler.Detail.Course.Flip.FlipCourseDialogCrawler;
import WebCrawler.Crawler.Detail.Course.FlipClass.FlipClassCourseDialogCrawler;
import activity.R;

public class CourseDialogCrawlerThread extends Thread {
    CourseDialogCrawlerInterface courseDialogCrawlerInterface;
    FlipCourseDialogCrawler flipCourseDialogCrawler;
    FlipClassCourseDialogCrawler flipClassCourseDialogCrawler;
    CourseItem Target;
    int Source;

    public CourseDialogCrawlerThread(int source, CourseDialogCrawlerInterface courseDialogCrawlerInterface, CourseItem target) {
        this.courseDialogCrawlerInterface = courseDialogCrawlerInterface;
        this.Target = target;
        Source = source;
    }

    @Override
    public void run() {
        try {
            courseDialogCrawlerInterface.CrawlerLoading(R.string.DetailDialogLoading);
            getCourseDialogInfo();
        } catch (Exception e) {
            System.out.println("Dialog FAIL");
            courseDialogCrawlerInterface.CrawlerFailed();
        }
    }

    private void getCourseDialogInfo() {
        if (!Target.isTitle) {
            if(Source == 0) {
                flipCourseDialogCrawler = new FlipCourseDialogCrawler(Target);
                flipCourseDialogCrawler.run();
                courseDialogCrawlerInterface.CrawlerSuccessful(flipCourseDialogCrawler.getTargetDetail());
            }
            else{
                flipClassCourseDialogCrawler = new FlipClassCourseDialogCrawler(Target);
                flipClassCourseDialogCrawler.run();
                courseDialogCrawlerInterface.CrawlerSuccessful(flipClassCourseDialogCrawler.getTargetDetail());
            }

        }
        else if(Target.Type.compareTo("title_content") == 0){
            courseDialogCrawlerInterface.CrawlerSuccessful(new Detail(Target.Name,Target.Html_Content));
        }
    }

}
