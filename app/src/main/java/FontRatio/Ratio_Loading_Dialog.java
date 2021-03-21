package FontRatio;

public class Ratio_Loading_Dialog extends Ratio{
    private float LoadingDialogWidth_weight = (float) 0.185;

    public Ratio_Loading_Dialog(int width, int height){
        super(width, height);
    }

    public int getLoadingDialogWidth() { return (int) (DisplayWidth * LoadingDialogWidth_weight); }
}
