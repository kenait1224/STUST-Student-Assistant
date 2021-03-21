package Data.StaticData;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserConfig {

    private static Map<String, Integer> MyConfig = new LinkedHashMap<>();
    private static ArrayList<String> NotificationContent = new ArrayList<>(Arrays.asList("前 1 天","前 2 天","前 3 天","前 4 天","前 5 天","前 6 天","前 7 天","永不"));
    private static ArrayList<String> StartPageContent = new ArrayList<>(Arrays.asList("我的課表","我的課程","我的公告","我的通知"));

    static {

        MyConfig.put("CurriculumSynchronize",1);
        MyConfig.put("OfflineMode",0);
        MyConfig.put("NotificationDate",7);
        MyConfig.put("StartPage",0);

    }

    public static ArrayList<String> getNotificationContent() {
        return NotificationContent;
    }

    public static ArrayList<String> getStartPageContent() {
        return StartPageContent;
    }

    public static Map<String, Integer> getMyConfig() {
        return MyConfig;
    }

    public static void setMyConfig(String Name , int value){
        MyConfig.put(Name,value);
    }

    public static void ReadLocalConfig(SharedPreferences Target){
        String JsonString = Target.getString("ConfigJson", "Null");
        try {
            if (JsonString == "Null")
                return;
            JSONArray Config  = new JSONArray(JsonString);
            for(int i = 0 ; i < Config.length();i++){
                MyConfig.put(Config.getJSONObject(i).getString("Name"),Config.getJSONObject(i).getInt("Value"));
            }
            System.out.println("Read Json Done!");
        }
        catch (Exception e){
            System.out.println("Read Json Failed!");
            e.printStackTrace();
        }
    }

    public static void SaveLocalConfig(SharedPreferences Target){
        JSONArray ConfigJson = new JSONArray();
        List keys = new ArrayList(MyConfig.keySet());
        try {
            for (int i = 0 ; i < MyConfig.size() ; i++){
                JSONObject Item = new JSONObject();
                Item.put("Name", keys.get(i));
                Item.put("Value", MyConfig.get(keys.get(i)));
                ConfigJson.put(Item);
            }
            Target.edit().putString("ConfigJson",ConfigJson.toString()).commit();
        }catch (Exception e){
            System.out.println("UserConfig : Save Json Failed!");
            e.printStackTrace();
        }
    }
}
