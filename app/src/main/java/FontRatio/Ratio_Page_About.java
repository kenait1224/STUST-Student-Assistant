package FontRatio;

public class Ratio_Page_About extends Ratio{
    private float About_page_rectangle_width_weight = (float) 0.78;
    private float About_page_rectangle_height_weight = (float) 0.2;
    private float About_page_union_width_weight = (float) 0.11;
    private float About_page_union_height_weight = (float) 0.06;

    public Ratio_Page_About(int width, int height) {
        super(width, height);
    }

    public int getRectangleWidth(){ return (int)(this.DisplayWidth*this.About_page_rectangle_width_weight);}
    public int getRectangleHeight(){ return (int)(this.DisplayHeight*this.About_page_rectangle_height_weight);}
    public int getUnionWidth(){return (int)(this.DisplayWidth*this.About_page_union_width_weight);}
    public int getUnionHeight(){return (int)(this.DisplayHeight*this.About_page_union_height_weight);}



}
