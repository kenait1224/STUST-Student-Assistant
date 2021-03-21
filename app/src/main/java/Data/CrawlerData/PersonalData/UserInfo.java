package Data.CrawlerData.PersonalData;

import java.util.ArrayList;

import Data.CrawlerData.CourseDetailInfo.DetailInfoContent;

public class UserInfo {
    private ArrayList<DetailInfoContent> UserDetailInfo = new ArrayList<>();
    private String StudentID;
    private String Name;
    private String Email;
    private String Role;
    private String EducationSystem;
    private String SchoolClass;
    private String PhotoHref;

    public UserInfo(String studentID, String name, String email, String role, String educationSystem, String aClass, String photoHref) {
        StudentID = studentID;
        Name = name;
        Email = email;
//        StudentID = "測試帳號";
//        Name = "測試帳號";
//        Email = "測試帳號";
        Role = role;
        EducationSystem = educationSystem;
        SchoolClass = aClass;
        PhotoHref = photoHref;
    }

    public void BuildDetailContent() {
        try {
            UserDetailInfo.add(new DetailInfoContent("帳號", StudentID));
            UserDetailInfo.add(new DetailInfoContent("姓名", Name));
            UserDetailInfo.add(new DetailInfoContent("電子郵件", Email));
            UserDetailInfo.add(new DetailInfoContent("身份", Role));
            UserDetailInfo.add(new DetailInfoContent("班級", SchoolClass));
            UserDetailInfo.add(new DetailInfoContent("學制", EducationSystem));
        } catch (Exception e) {
            System.out.println("BuildDetailContent Error");
            e.printStackTrace();
        }
    }

    public ArrayList<DetailInfoContent> getUserDetailInfo() {
        return UserDetailInfo;
    }

    public String getStudentID() {
        return StudentID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getRole() {
        return Role;
    }

    public String getEducationSystem() {
        return EducationSystem;
    }

    public String getSchoolClass() {
        return SchoolClass;
    }

    public String getPhotoHref() {
        return PhotoHref;
    }
}
