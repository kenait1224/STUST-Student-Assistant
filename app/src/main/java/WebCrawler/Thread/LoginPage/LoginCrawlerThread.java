package WebCrawler.Thread.LoginPage;

import Data.StaticData.UserBulletinInfo;
import Data.StaticData.UserConfig;
import Data.StaticData.UserCourseInfo;
import Data.StaticData.UserCurriculumInfo;
import Data.StaticData.UserMessageInfo;
import WebCrawler.Crawler.Outline.CourseSelectionSystem.CurriculumCrawler;
import WebCrawler.Crawler.Outline.Flip.FlipBulletinCrawler;
import WebCrawler.Crawler.Outline.Flip.FlipCourseCrawler;
import WebCrawler.Crawler.Outline.Flip.FlipMessageCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassBulletinCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassCourseCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassMessageCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassUserInfoCrawler;
import WebCrawler.Crawler.Outline.Stust.BulletinCrawler;
import activity.R;

public class LoginCrawlerThread extends Thread {
    LoginCrawlerInterface crawlerInterface;
    FlipCourseCrawler flipCourseCrawler;
    FlipBulletinCrawler flipBulletinCrawler;
    FlipMessageCrawler flipMessageCrawler;
    FlipClassUserInfoCrawler flipClassUserInfoCrawler;
    FlipClassBulletinCrawler flipClassBulletinCrawler;
    FlipClassMessageCrawler flipClassMessageCrawler;
    FlipClassCourseCrawler flipClassCourseCrawler;
    BulletinCrawler bulletinCrawler;
    CurriculumCrawler curriculumCrawler;


    public LoginCrawlerThread(LoginCrawlerInterface crawlerInterface) {
        this.crawlerInterface = crawlerInterface;
        flipCourseCrawler = new FlipCourseCrawler();
        flipBulletinCrawler = new FlipBulletinCrawler();
        flipMessageCrawler = new FlipMessageCrawler();
        flipClassUserInfoCrawler = new FlipClassUserInfoCrawler();
        flipClassBulletinCrawler = new FlipClassBulletinCrawler();
        flipClassMessageCrawler = new FlipClassMessageCrawler();
        flipClassCourseCrawler = new FlipClassCourseCrawler();
        curriculumCrawler = new CurriculumCrawler();
        bulletinCrawler = new BulletinCrawler();
    }

    @Override
    public void run() {
        getUserInfo();
        getCourseInfo();
        getCurriculumInfo();
        getBulletin();
        getMessage();
        if(!flipClassCourseCrawler.Successful()&&!curriculumCrawler.Successful())
            crawlerInterface.CrawlerFailed();
        else {
            crawlerInterface.CrawlerSuccessful();
        }
    }

    public void getUserInfo(){
        try {
            crawlerInterface.CrawlerLoading(R.string.UserInfoLoading);
            flipClassUserInfoCrawler.Getting();
            flipClassUserInfoCrawler.Building();
        }
        catch (Exception e){
            System.out.println("getUserInfo Error");
            e.printStackTrace();
        }
    }

    public void getMessage(){
        try {
            UserMessageInfo.Clear();
            crawlerInterface.CrawlerLoading(R.string.MessageLoading);
            flipMessageCrawler.Getting();
            flipMessageCrawler.Building();
            flipClassMessageCrawler.Getting();
            flipClassMessageCrawler.Building();
        }
        catch (Exception e){
            System.out.println("getMessage Error");
            e.printStackTrace();
        }
    }

    public void getBulletin() {
        try {
            UserBulletinInfo.Clear();
            crawlerInterface.CrawlerLoading(R.string.BulletinLoading);
            bulletinCrawler.Getting();
            bulletinCrawler.Building();
            flipBulletinCrawler.Getting();
            flipBulletinCrawler.Building();
            flipClassBulletinCrawler.Getting();
            flipClassBulletinCrawler.Building();
        }
        catch (Exception e){
            System.out.println("getBulletin Error");
            e.printStackTrace();
        }
    }

    public void getCourseInfo(){
        try {
            UserCourseInfo.Clear();
            crawlerInterface.CrawlerLoading(R.string.CourseLoading);
            flipCourseCrawler.Getting();
            flipCourseCrawler.Building();
            flipClassCourseCrawler.Getting();
            flipClassCourseCrawler.Building();
        }
        catch (Exception e){
            System.out.println("getCourseInfo Error");
            e.printStackTrace();
        }
    }

    public void getCurriculumInfo() {
        try {
            if (UserCurriculumInfo.getMyCurriculum().Size() == 0 || (!UserCurriculumInfo.getMyCurriculum().CheckCurriculumTableIsSame(flipCourseCrawler.getFlipCourseAddressList())&& UserConfig.getMyConfig().get("CurriculumSynchronize")==1)) {
                crawlerInterface.CrawlerLoading(R.string.CurriculumLoading);
                curriculumCrawler.Getting();
                curriculumCrawler.Building();
            }
        }
        catch (Exception e){
            System.out.println("getCurriculumInfo Error");
            e.printStackTrace();
        }
    }
}
