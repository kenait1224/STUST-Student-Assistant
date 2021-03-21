package Data.StaticData;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Data.CrawlerData.CurriculumData.Content;
import Data.CrawlerData.CurriculumData.CourseInfo;
import Data.CrawlerData.CurriculumData.CurriculumTable;

public class UserCurriculumInfo {

    private static CurriculumTable MyCurriculum = new CurriculumTable();


    public static void Clear(){
        MyCurriculum.Clear();
    }

    public static void SetCurriculumTable(CurriculumTable Target){
        MyCurriculum = Target;
    }

    public static CurriculumTable getMyCurriculum(){
        return MyCurriculum;
    }

    public static void ReadUserCurriculumInfo(SharedPreferences Target){
        String JsonString = Target.getString("CurriculumJson", "Null");
        try {
            if (JsonString == "Null")
                return;
            JSONArray Curriculum = new JSONArray(JsonString);
            for(int i = 0 ; i < 7 ; i++ ){
                JSONArray jsonArray = Curriculum.getJSONObject(i).getJSONArray("Data");
                for (int j = 0 ; j < jsonArray.length();j++){
                    MyCurriculum.add(i,new Content(Integer.parseInt(jsonArray.getJSONObject(j).get("Section").toString()),new CourseInfo(
                            jsonArray.getJSONObject(j).get("Name").toString(),
                            jsonArray.getJSONObject(j).get("Teacher").toString(),
                            jsonArray.getJSONObject(j).get("Location").toString()
                    )));
                }
            }
            System.out.println("Read Json Done!");
        }
        catch (Exception e){
            System.out.println("Read Json Failed!");
            e.printStackTrace();
        }
    }

    public static void SaveUserCurriculumInfo(SharedPreferences Target){
        if (MyCurriculum.Size() == 0)
            return;
        JSONArray Curriculum = new JSONArray();
        try {
            for (int i = 0 ; i < 7 ; i++){
                ArrayList<Content> item = MyCurriculum.getContent(i);
                JSONObject DayOfWeek = new JSONObject();
                DayOfWeek.put("DayOfWeek",i);
                JSONArray Course_list = new JSONArray();
                for (Content content : item){
                    JSONObject course = new JSONObject();
                    course.put("Section",content.getSection());
                    course.put("Name",content.getCourseInfo().getName());
                    course.put("Teacher",content.getCourseInfo().getTeacher());
                    course.put("Location",content.getCourseInfo().getLocation());
                    Course_list.put(course);
                }
                DayOfWeek.put("Data",Course_list);
                Curriculum.put(DayOfWeek);
            }
            Target.edit().putString("CurriculumJson",Curriculum.toString()).commit();
        }catch (Exception e){
            System.out.println("Save Json Failed!");
            e.printStackTrace();
        }
    }
}
