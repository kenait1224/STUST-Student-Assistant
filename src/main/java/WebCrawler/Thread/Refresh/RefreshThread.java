package WebCrawler.Thread.Refresh;

import Data.StaticData.UserBulletinInfo;
import Data.StaticData.UserCourseAttachment;
import Data.StaticData.UserCourseDetail;
import Data.StaticData.UserCourseInfo;
import Data.StaticData.UserCurriculumInfo;
import Data.StaticData.UserMessageInfo;
import Data.StaticData.UserPersonalInfo;
import WebCrawler.Crawler.Outline.Flip.FlipBulletinCrawler;
import WebCrawler.Crawler.Outline.Flip.FlipCourseCrawler;
import WebCrawler.Crawler.Outline.CourseSelectionSystem.CurriculumCrawler;
import WebCrawler.Crawler.Outline.Flip.FlipMessageCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassBulletinCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassCourseCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassMessageCrawler;
import WebCrawler.Crawler.Outline.FlipClass.FlipClassUserInfoCrawler;
import WebCrawler.Crawler.Outline.Stust.BulletinCrawler;
import activity.R;

public class RefreshThread extends Thread {
    private RefreshInfoInterface refreshInfoInterface;
    private String TargetID;
    FlipCourseCrawler flipCourseCrawler;
    FlipBulletinCrawler flipBulletinCrawler;
    FlipMessageCrawler flipMessageCrawler;
    FlipClassUserInfoCrawler flipClassUserInfoCrawler;
    FlipClassBulletinCrawler flipClassBulletinCrawler;
    FlipClassMessageCrawler flipClassMessageCrawler;
    FlipClassCourseCrawler flipClassCourseCrawler;
    BulletinCrawler bulletinCrawler;
    CurriculumCrawler curriculumCrawler;

    public RefreshThread(RefreshInfoInterface refreshInfoInterface, String targetID) {
        this.refreshInfoInterface = refreshInfoInterface;
        this.TargetID = targetID;
    }

    @Override
    public void run() {
        try {
            switch (TargetID) {
                case "我的課表":
                    curriculumCrawler = new CurriculumCrawler();
                    UserCurriculumInfo.Clear();
                    getCurriculumInfo();
                    if (curriculumCrawler.Successful())
                        refreshInfoInterface.CrawlerSuccessful();
                    else
                        refreshInfoInterface.CrawlerFailed();
                    break;
                case "我的課程":
                    flipCourseCrawler = new FlipCourseCrawler();
                    flipClassCourseCrawler = new FlipClassCourseCrawler();
                    UserCourseInfo.Clear();
                    UserCourseDetail.Clear();
                    UserCourseAttachment.Clear();
                    getCourseInfo();
                    if (flipCourseCrawler.Successful())
                        refreshInfoInterface.CrawlerSuccessful();
                    else
                        refreshInfoInterface.CrawlerFailed();
                    break;
                case "我的公告":
                    bulletinCrawler = new BulletinCrawler();
                    flipBulletinCrawler = new FlipBulletinCrawler();
                    flipClassBulletinCrawler = new FlipClassBulletinCrawler();
                    UserBulletinInfo.Clear();
                    getBulletin();
                    if (bulletinCrawler.Successful() || flipBulletinCrawler.Successful())
                        refreshInfoInterface.CrawlerSuccessful();
                    else
                        refreshInfoInterface.CrawlerFailed();
                    break;
                case "我的通知":
                    flipMessageCrawler = new FlipMessageCrawler();
                    flipClassMessageCrawler = new FlipClassMessageCrawler();
                    UserMessageInfo.Clear();
                    getMessage();
                    if (flipMessageCrawler.Successful())
                        refreshInfoInterface.CrawlerSuccessful();
                    else
                        refreshInfoInterface.CrawlerFailed();
                    break;
                case "我的資訊":
                    flipClassUserInfoCrawler = new FlipClassUserInfoCrawler();
                    UserPersonalInfo.Clear();
                    getUserInfo();
                    if (flipClassUserInfoCrawler.Successful())
                        refreshInfoInterface.CrawlerSuccessful();
                    else
                        refreshInfoInterface.CrawlerFailed();
                    break;
            }
        } catch (Exception e) {
            System.out.println();
        }
    }


    public void getUserInfo(){
        try {
            refreshInfoInterface.CrawlerLoading(R.string.UserInfoLoading);
            flipClassUserInfoCrawler.Getting();
            flipClassUserInfoCrawler.Building();
        }
        catch (Exception e){
            System.out.println("getUserInfo Error");
            e.printStackTrace();
        }
    }

    private void getCourseInfo() {
        try {
            refreshInfoInterface.CrawlerLoading(R.string.CourseLoading);
            flipCourseCrawler.Getting();
            flipCourseCrawler.Building();
            flipClassCourseCrawler.Getting();
            flipClassCourseCrawler.Building();
        } catch (Exception e) {
            System.out.println("getFlipInfo Error");
            e.printStackTrace();
        }
    }

    public void getMessage(){
        try {
            UserMessageInfo.Clear();
            refreshInfoInterface.CrawlerLoading(R.string.MessageLoading);
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
            refreshInfoInterface.CrawlerLoading(R.string.BulletinLoading);
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

    private void getCurriculumInfo() {
        try {
            refreshInfoInterface.CrawlerLoading(R.string.CurriculumLoading);
            curriculumCrawler.Getting();
            curriculumCrawler.Building();
        } catch (Exception e) {
            System.out.println("getCurriculumInfo Error");
            e.printStackTrace();
        }
    }
}
