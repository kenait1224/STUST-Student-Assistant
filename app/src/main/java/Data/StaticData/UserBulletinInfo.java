package Data.StaticData;

import java.util.ArrayList;

import Data.CrawlerData.BulletinData.BulletinInfo;
public class UserBulletinInfo {
    private static ArrayList<BulletinInfo> SchoolBulletin = new ArrayList<>();
    private static ArrayList<BulletinInfo> FlipBulletin = new ArrayList<>();
    private static ArrayList<BulletinInfo> FlipClassBulletin = new ArrayList<>();

    public static void Clear(){
        SchoolBulletin.clear();
        FlipBulletin.clear();
        FlipClassBulletin.clear();
    }

    public static void Clear(int index){
        switch (index){
            case 0:
                FlipBulletin.clear();
                break;
            case 1:
                FlipClassBulletin.clear();
                break;
            case 2:
                SchoolBulletin.clear();
        }
    }

    public static void setBulletin(int idx , BulletinInfo Target) {
        switch (idx){
            case 0:
                FlipBulletin.add(Target);
                break;
            case 1:
                FlipClassBulletin.add(Target);
                break;
            case 2:
                SchoolBulletin.add(Target);
        }
    }

    public static ArrayList<BulletinInfo> getBulletin(int idx) {
        switch (idx){
            case 0:
                return FlipBulletin;
            case 1:
                return FlipClassBulletin;
            case 2:
                return SchoolBulletin;
        }
        return null;
    }

    public static String getBulletinName(int idx){
        switch (idx){
            case 0:
                return "課程公告 Flip";
            case 1:
                return "課程公告 Flip Class";
            case 2:
                return "學校公告";
        }
        return null;
    }

    public static boolean getLoadable(int idx){
        switch (idx){
            case 0:
                return FlipBulletin.size() >= 20;
            case 1:
                return FlipClassBulletin.size() >= 20 ;
            case 2:
                return SchoolBulletin.size() >= 20;
        }
        return false;
    }

    public static void CheckNullItem(int idx){
        
    }

}
