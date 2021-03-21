package FontRatio;

public class Ratio_Course extends Ratio{

    private float CourseDetailTitleHeight_weight = (float) 0.06;
    private float CourseDetailContentHeight_weight = (float) 0.055;
    private float CourseDetailContentWidthPadding_weight = (float) 0.07;
    private float CourseDetailTitleWidthPadding_weight = (float) 0.05;
    private float CourseDetailContentPadding_weight = (float) 0.003;
    private float CourseDetailTitlePadding_weight = (float) 0.019;
    private float CourseItemHeight_weight = (float) 0.21;
    private float CourseMenuUnionPadding_weight= (float) 0.3;
    private float CourseMenuInfoPadding_weight= (float) 0.19;
    private float CourseMenuRefreshPadding_weight= (float) 0.215;
    private float CourseDialogUnionPadding_weight= (float) 0.15;

    public Ratio_Course(int width, int height) {
        super(width, height);
    }

    public int getCourseDetailTitleHeight(){return (int)(DisplayHeight*CourseDetailTitleHeight_weight);}
    public int getCourseDetailContentHeight(){return (int)(DisplayHeight*CourseDetailContentHeight_weight);}
    public int getCourseItemHeight(){return (int) (DisplayHeight *0.8 *CourseItemHeight_weight);}
    public int getCourseMenuRefreshPadding(){return (int) ((int)DisplayHeight*0.1*CourseMenuRefreshPadding_weight);}
    public int getCourseMenuInfoPadding(){return (int) ((int)DisplayHeight*0.1*CourseMenuInfoPadding_weight);}
    public int getCourseMenuUnionPadding(){return (int) ((int)DisplayHeight*0.1*CourseMenuUnionPadding_weight);}
    public int getCourseDialogUnionPadding(){return (int) ((int)DisplayHeight*0.2*CourseDialogUnionPadding_weight);}
    public int getCourseDetailTitleWidthPadding(){return (int) ((int)DisplayWidth*CourseDetailTitleWidthPadding_weight);}
    public int getCourseDetailContentWidthPadding(){return (int) ((int)DisplayWidth*CourseDetailContentWidthPadding_weight);}
    public int getCourseDetailTitlePadding(){return (int) ((int)DisplayHeight*CourseDetailTitlePadding_weight);}
    public int getCourseDetailContentPadding(){return (int) ((int)DisplayHeight*CourseDetailContentPadding_weight);}

}
