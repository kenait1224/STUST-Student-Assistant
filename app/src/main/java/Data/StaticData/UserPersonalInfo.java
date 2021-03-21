package Data.StaticData;

import android.content.SharedPreferences;

import Data.CrawlerData.PersonalData.UserInfo;

public class UserPersonalInfo {

    private static UserInfo MyUserInfo;

    public static void Clear(){
        MyUserInfo = null;
    }

    public static void setMyUserInfo(UserInfo myUserInfo) {
        MyUserInfo = myUserInfo;
    }

    public static UserInfo getMyUserInfo() {
        return MyUserInfo;
    }

    public static void ReadUserPersonalInfo(SharedPreferences Target){
        try {
            String StudentID = Target.getString("StudentID", "Null");
            String Email = Target.getString("Email", "Null");
            String Name = Target.getString("Name", "Null");
            String Role = Target.getString("Role", "Null");
            String EducationSystem = Target.getString("EducationSystem", "Null");
            String SchoolClass = Target.getString("SchoolClass", "Null");
            String PhotoHref =  Target.getString("Photo", "Null");
            MyUserInfo = new UserInfo(StudentID, Name, Email, Role,EducationSystem,SchoolClass,PhotoHref);
            MyUserInfo.BuildDetailContent();
        } catch (Exception e) {
            System.out.println("UserPersonalInfo Error : ReadUserPersonalInfo");
            e.printStackTrace();
        }
    }

    public static void SaveUserPersonalInfo(SharedPreferences Target) {
        try {
            if(MyUserInfo!=null) {
                Target.edit()
                        .putString("Name",MyUserInfo.getName())
                        .putString("Photo", MyUserInfo.getPhotoHref())
                        .putString("Email",MyUserInfo.getEmail())
                        .putString("EducationSystem",MyUserInfo.getEducationSystem())
                        .putString("Role",MyUserInfo.getRole())
                        .putString("StudentID",MyUserInfo.getStudentID())
                        .putString("SchoolClass",MyUserInfo.getSchoolClass())
                        .commit();
            }
        } catch (Exception e) {
            System.out.println("UserPersonalInfo Error : SaveUserPersonalInfo");
            e.printStackTrace();
        }
    }
}
