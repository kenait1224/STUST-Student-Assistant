package FontRatio;

public class Ratio_Offline_Mode_Dialog extends Ratio{

    private float OfflineModeDialogWidth_weight = (float) 0.7;
    private float OfflineModeDialogHeight_weight = (float) 0.275;
    private float OfflineModeButtonWidth_weight = (float) 0.25;
    private float OfflineModeButtonHeight_weight = (float) 0.14;
    private float OfflineModeIconWidth_weight = (float) 0.18;
    private float OfflineModeIconHeight_weight = (float) 0.2;

    public Ratio_Offline_Mode_Dialog(int width, int height) {
        super(width, height);
    }

    public int getOfflineModeDialogWidth() {
        return (int) (OfflineModeDialogWidth_weight * this.DisplayWidth);
    }

    public int getOfflineModeDialogHeight() {
        return (int) (OfflineModeDialogHeight_weight * this.DisplayHeight);
    }

    public int getOfflineModeButtonWidth() {
        return (int) (OfflineModeButtonWidth_weight * OfflineModeDialogWidth_weight * this.DisplayWidth);
    }

    public int getOfflineModeButtonHeight() {
        return (int) (OfflineModeButtonHeight_weight * OfflineModeDialogHeight_weight * this.DisplayHeight);
    }

    public int getOfflineModeIconWidth() {
        return (int) (OfflineModeIconWidth_weight * OfflineModeDialogWidth_weight * this.DisplayWidth);
    }

    public int getOfflineModeIconHeight() {
        return (int) (OfflineModeIconHeight_weight * OfflineModeDialogHeight_weight * this.DisplayHeight);
    }
}
