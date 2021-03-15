package WebCrawler.Thread.MessagePage;

import Data.StaticData.UserBulletinInfo;
import Data.StaticData.UserMessageInfo;
import WebCrawler.Crawler.Outline.Flip.FlipMessageCrawler;

public class MessageLoadMoreThread extends Thread {

    MessageLoadMoreInterface messageLoadMoreInterface;
    int Group, Page;


    public MessageLoadMoreThread(MessageLoadMoreInterface messageLoadMoreInterface, int group, int page) {
        this.messageLoadMoreInterface = messageLoadMoreInterface;
        Group = group;
        Page = page;
    }

    @Override
    public void run() {
        try {
            LoadMoreBulletin();
            messageLoadMoreInterface.CrawlerSuccessful();
        } catch (Exception e) {
            System.out.println("NewsLoadMoreThread Error");
            messageLoadMoreInterface.CrawlerFailed();
            e.printStackTrace();
        }
    }

    public void LoadMoreBulletin() {
        switch (Group) {
            case 0:
                FlipMessageCrawler flipMessageCrawler = new FlipMessageCrawler(Page);
                flipMessageCrawler.Getting();
                UserMessageInfo.getMessage(Group).remove(UserBulletinInfo.getBulletin(Group).size() - 1);
                flipMessageCrawler.Building();
                break;
            case 1:
                break;
        }
    }
}
