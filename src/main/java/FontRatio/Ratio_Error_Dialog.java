package FontRatio;

public class Ratio_Error_Dialog extends Ratio {

    private float ErrorDialogWidth_weight = (float) 0.5;
    private float ErrorDialogHeight_weight = (float) 0.275;

    public Ratio_Error_Dialog(int width, int height) {
        super(width, height);
    }

    public int getErrorDialogWidth() {
        return (int) (ErrorDialogWidth_weight * this.DisplayWidth);
    }

    public int getErrorDialogHeight() {
        return (int) (ErrorDialogHeight_weight * this.DisplayHeight);
    }


}
