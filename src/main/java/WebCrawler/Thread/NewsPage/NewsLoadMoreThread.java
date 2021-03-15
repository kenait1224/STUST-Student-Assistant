package WebCrawler.Thread.NewsPage;

import Data.StaticData.UserBulletinInfo;
import WebCrawler.Crawler.Outline.Flip.FlipBulletinCrawler;
import WebCrawler.Crawler.Outline.Stust.BulletinCrawler;

public class NewsLoadMoreThread extends Thread{

    NewsLoadMoreInterface newsLoadMoreInterface;
    int Group,Page;


    public NewsLoadMoreThread(NewsLoadMoreInterface newsLoadMoreInterface,int group ,int page) {
        this.newsLoadMoreInterface = newsLoadMoreInterface;
        Group = group;
        Page = page;
    }

    @Override
    public void run() {
        try {
            LoadMoreBulletin();
            newsLoadMoreInterface.CrawlerSuccessful();
        }
        catch (Exception e){
            System.out.println("NewsLoadMoreThread Error");
            newsLoadMoreInterface.CrawlerFailed();
            e.printStackTrace();
        }
    }

    public void LoadMoreBulletin(){
        switch (Group){
            case 0:
                FlipBulletinCrawler flipBulletinCrawler = new FlipBulletinCrawler(Page);
                flipBulletinCrawler.Getting();
                UserBulletinInfo.getBulletin(Group).remove(UserBulletinInfo.getBulletin(Group).size() - 1 );
                flipBulletinCrawler.Building();
                break;
            case 1:
                break;
            case 2:
                BulletinCrawler bulletinCrawler = new BulletinCrawler(Page);
                bulletinCrawler.Getting();
                UserBulletinInfo.getBulletin(Group).remove(UserBulletinInfo.getBulletin(Group).size() - 1 );
                bulletinCrawler.Building();
                break;
        }
    }
}
