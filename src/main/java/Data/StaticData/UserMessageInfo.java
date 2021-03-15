package Data.StaticData;

import java.util.ArrayList;

import Data.CrawlerData.MessageData.MessageInfo;

public class UserMessageInfo {
    private static ArrayList<MessageInfo> FlipMessage = new ArrayList<>();
    private static ArrayList<MessageInfo> FlipClassMessage = new ArrayList<>();

    public static void Clear() {
        FlipMessage.clear();
        FlipClassMessage.clear();
    }

    public static void Clear(int index) {
        switch (index) {
            case 0:
                FlipMessage.clear();
                break;
            case 1:
                FlipClassMessage.clear();
                break;
        }
    }

    public static ArrayList<MessageInfo> getMessage(int idx) {
        switch (idx) {
            case 0:
                return FlipMessage;
            case 1:
                return FlipClassMessage;
            default:
                return null;
        }
    }

    public static void setMessage(int idx, MessageInfo messageInfo) {
        switch (idx) {
            case 0:
                FlipMessage.add(messageInfo);
                break;
            case 1:
                FlipClassMessage.add(messageInfo);
                break;
        }
    }

}
