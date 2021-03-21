package FontRatio;

public class Ratio_Message extends  Ratio {

    private float MessageContentHeight_Weight = (float) 0.12;

    public Ratio_Message(int width, int height) {
        super(width, height);
    }

    public int getMessageContentHeight() {
        return (int) (MessageContentHeight_Weight * this.DisplayHeight);
    }
}
