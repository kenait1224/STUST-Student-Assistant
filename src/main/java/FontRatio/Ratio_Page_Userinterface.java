package FontRatio;

public class Ratio_Page_Userinterface extends Ratio{

    private float CourseMenuRefreshPadding_weight= (float) 0.3;
    private float UserinterfaceMenuSize_weight = (float) 0.07;

    public Ratio_Page_Userinterface(int width, int height) {
        super(width, height);
    }

    public int getCourseMenuRefreshPadding(){return (int) (this.DisplayHeight*0.1*CourseMenuRefreshPadding_weight);}

    public int getUserinterfaceMenuSize() {
        return (int) (UserinterfaceMenuSize_weight*this.DisplayWidth);
    }
}
