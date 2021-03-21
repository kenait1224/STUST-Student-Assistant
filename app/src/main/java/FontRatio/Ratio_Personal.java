package FontRatio;

public class Ratio_Personal extends Ratio {

    private float PersonalPreferenceHeight_weight = (float) 0.72;

    public Ratio_Personal(int width, int height) {
        super(width, height);
    }

    public int getPersonalPreferenceHeight() {
        return (int) (this.DisplayHeight*PersonalPreferenceHeight_weight);
    }
}
