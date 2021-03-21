package FontRatio;

public class Ratio_PPT_Viewer extends Ratio {

    private float PPTViewrSimpleDraweeViewHeight_Weight = (float)0.5;
    private float PPTViewerUnionPadding_weight= (float) 0.3;

    public Ratio_PPT_Viewer(int width, int height) {
        super(width, height);
    }

    public int getPPTViewrSimpleDraweeViewHeight() { return (int) (this.DisplayHeight*0.8*PPTViewrSimpleDraweeViewHeight_Weight); }

    public int getPPTViewerUnionPadding() {
        return (int) (this.DisplayHeight*0.1*PPTViewerUnionPadding_weight);
    }
}
