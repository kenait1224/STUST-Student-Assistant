package WebCrawler.Thread.NewsPage;

import Data.CrawlerData.BulletinData.BulletinInfo;
import WebCrawler.Crawler.Detail.News.Flip.FlipNewsDialogCrawler;
import WebCrawler.Crawler.Detail.News.FlipClass.FlipClassNewsDialogCrawler;
import activity.R;

public class NewsDialogCrawlerThread extends Thread {

    NewsDialogCrawlerInterface newsDialogCrawlerInterface;
    FlipNewsDialogCrawler flipNewsDialogCrawler;
    FlipClassNewsDialogCrawler flipClassNewsDialogCrawler;
    int Source;

    public NewsDialogCrawlerThread(NewsDialogCrawlerInterface newsDialogCrawlerInterface, int source , BulletinInfo bulletinInfo) {
        this.newsDialogCrawlerInterface = newsDialogCrawlerInterface;
        if (source == 0)
            flipNewsDialogCrawler = new FlipNewsDialogCrawler(bulletinInfo);
        else
            flipClassNewsDialogCrawler = new FlipClassNewsDialogCrawler(bulletinInfo);
        Source = source;
    }

    @Override
    public void run() {
        try {
            newsDialogCrawlerInterface.CrawlerLoading(R.string.BulletinDetailLoading);
            if ( Source == 0 ){
                flipNewsDialogCrawler.run();
                if (flipNewsDialogCrawler.getTargetDetail() != null )
                    newsDialogCrawlerInterface.CrawlerSuccessful(flipNewsDialogCrawler.getTargetDetail());
                else
                    newsDialogCrawlerInterface.CrawlerFailed();
            }
            else {
                flipClassNewsDialogCrawler.run();
                if ( flipClassNewsDialogCrawler.getTargetDetail() != null)
                    newsDialogCrawlerInterface.CrawlerSuccessful(flipClassNewsDialogCrawler.getTargetDetail());
                else
                    newsDialogCrawlerInterface.CrawlerFailed();
            }
        } catch (Exception e) {
            newsDialogCrawlerInterface.CrawlerFailed();
            System.out.println("NewsDialogCrawlerThread Error");
            e.printStackTrace();
        }

    }
}
