package Data.CrawlerData.CurriculumData;

public class Content {
    private int Section;
    private CourseInfo courseInfo;

    public Content( int section, CourseInfo courseInfo) {
        Section = section;
        this.courseInfo = courseInfo;
    }

    public int getSection() {
        return Section;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }
}
