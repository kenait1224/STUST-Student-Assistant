package Data.StaticData;

import android.content.SharedPreferences;

import Data.CrawlerData.Common.User;

public class UserLoginInfo {
    private static User user;

    public  static void Clear(){
        user = null;
    }

    public static void setUser(User user) {
        UserLoginInfo.user = user;
    }

    public static User getUser() {
        return user;
    }

    public static void ReadUserLoginInfo(SharedPreferences Target) {
        try {
            String Username = Target.getString("Username", "Null");
            String Password = Target.getString("Password", "Null");
            user = new User(Username, Password);
        } catch (Exception e) {
            System.out.println("UserLoginInfo Error : SaveUserLoginInfo");
            e.printStackTrace();
        }
    }

    public static void SaveUserLoginInfo(SharedPreferences Target) {
        try {
            Target.edit()
                    .putString("Username",user.getUsername())
                    .putString("Password",user.getPassword())

                    .commit();
        } catch (Exception e) {
            System.out.println("UserLoginInfo Error : SaveUserLoginInfo");
            e.printStackTrace();
        }
    }
}
