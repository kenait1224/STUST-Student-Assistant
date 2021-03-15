package Data.StaticData;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserLoginCookies {
    private static Map<String, String> FlipLoginInfo = new LinkedHashMap<>();
    private static Map<String, String> FlipClassLoginInfo = new LinkedHashMap<>();
    private static Map<String, String> CourseSelectSystemLoginInfo = new LinkedHashMap<>();

    public static void Clear(){
        FlipLoginInfo.clear();
        FlipClassLoginInfo.clear();
        CourseSelectSystemLoginInfo.clear();
    }

    public static void setFlipLoginInfo(Map<String, String> userLoginInfo) {
        FlipLoginInfo = userLoginInfo;
    }

    public static void setFlipClassLoginInfo(Map<String, String> flipClassLoginInfo) {
        FlipClassLoginInfo = flipClassLoginInfo;
    }

    public static void setCourseSelectSystemLoginInfo(Map<String, String> userLoginInfo) {
        CourseSelectSystemLoginInfo= userLoginInfo;
    }

    public static Map<String, String> getFlipClassLoginInfo() {
        return FlipClassLoginInfo;
    }

    public static Map<String, String> getFlipLoginInfo() {
        return FlipLoginInfo;
    }

    public static Map<String, String> getCourseSelectSystemLoginInfo() {
        return CourseSelectSystemLoginInfo;
    }
}
