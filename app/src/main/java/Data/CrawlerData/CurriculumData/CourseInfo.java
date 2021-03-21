package Data.CrawlerData.CurriculumData;

public class CourseInfo {
    String name;
    String teacher;
    String location;

    public CourseInfo(String name, String teacher, String location) {
        this.name = name;
        this.teacher = teacher;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getLocation() {
        return location;
    }
}
