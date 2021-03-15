package Data.CrawlerData.CurriculumData;

import java.util.ArrayList;

public class CurriculumTable {
    ArrayList<ArrayList<Content>> contents = new ArrayList<>();
    ArrayList<String> CourseList = new ArrayList<>();

    public void Clear() {
        CourseList.clear();
        contents.clear();
    }

    public int Size() {
        return CourseList.size();
    }

    public CurriculumTable() {
        for (int i = 0; i < 7; i++) {
            contents.add(new ArrayList<Content>());
        }
    }

    public void add(int day, Content content) {
        if (CourseList.indexOf(content.getCourseInfo().name) == -1 && content.getCourseInfo().name.compareTo("導師時間") != 0)
            CourseList.add(content.getCourseInfo().name);
        contents.get(day).add(content);
    }

    public boolean CheckCurriculumTableIsSame(ArrayList<String> courseList) {
        for (String CourseName : courseList) {
            if (CourseList.indexOf(CourseName) == -1)
                return false;
        }
        return true;
    }

    public ArrayList<Content> getContent(int index) {
        if(contents.size() == 0)
            return null;
        return contents.get(index);
    }

    public ArrayList<String> getCourseList() {
        return CourseList;
    }
}
