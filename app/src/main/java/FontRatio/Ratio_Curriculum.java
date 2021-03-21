package FontRatio;

public class Ratio_Curriculum extends Ratio{

    private float CurriculumHeight_weight = (float) 0.16;
    private float CurriculumWidth_weight = (float) 0.29;

    public Ratio_Curriculum(int width, int height){
        super(width,height);
    }
    public int getCurriculumHeight(){ return (int) (DisplayHeight * CurriculumHeight_weight);}
    public int getCurriculumWidth(){ return (int) (DisplayWidth * CurriculumWidth_weight);}
}