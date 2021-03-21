package FontRatio;

public class Ratio_News extends Ratio{

    private float NewsContentHeight_weight = (float) 0.11;

    public Ratio_News(int width, int height) {
        super(width, height);
    }

    public int getNewsContentHeight() {
        return (int) (NewsContentHeight_weight * this.DisplayHeight);
    }
}
