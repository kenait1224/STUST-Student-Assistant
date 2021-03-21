package WebCrawler.Thread.MessagePage;

import Data.CrawlerData.MessageData.MessageInfo;
import WebCrawler.Crawler.Detail.Message.Flip.FlipMessageDialogCrawler;
import WebCrawler.Crawler.Detail.Message.FlipClass.FlipClassMessageDialogCrawler;
import activity.R;

public class MessageDialogCrawlerThread extends Thread {
    MessageDialogCrawlerInterface messageDialogCrawlerInterface;
    FlipMessageDialogCrawler flipMessageDialogCrawler;
    FlipClassMessageDialogCrawler flipClassMessageDialogCrawler;
    int Source;

    public MessageDialogCrawlerThread(MessageDialogCrawlerInterface messageDialogCrawlerInterface, int source, MessageInfo messageInfo) {
        this.messageDialogCrawlerInterface = messageDialogCrawlerInterface;
        if (source == 0)
            flipMessageDialogCrawler = new FlipMessageDialogCrawler(messageInfo);
        else
            flipClassMessageDialogCrawler = new FlipClassMessageDialogCrawler(messageInfo);
        Source = source;
    }

    @Override
    public void run() {
        try {
            messageDialogCrawlerInterface.CrawlerLoading(R.string.MessageDetailLoading);
            if (Source == 0) {
                flipMessageDialogCrawler.run();
                if (flipMessageDialogCrawler.getTargetDetail() != null)
                    messageDialogCrawlerInterface.CrawlerSuccessful(flipMessageDialogCrawler.getTargetDetail());
                else
                    messageDialogCrawlerInterface.CrawlerFailed();
            } else {
                flipClassMessageDialogCrawler.run();
                if (flipClassMessageDialogCrawler.getTargetDetail() != null)
                    messageDialogCrawlerInterface.CrawlerSuccessful(flipClassMessageDialogCrawler.getTargetDetail());
                else
                    messageDialogCrawlerInterface.CrawlerFailed();
            }
        } catch (Exception e) {
            messageDialogCrawlerInterface.CrawlerFailed();
            System.out.println("NewsDialogCrawlerThread Error");
            e.printStackTrace();
        }
    }
}
