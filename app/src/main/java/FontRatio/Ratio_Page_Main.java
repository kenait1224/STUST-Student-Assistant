package FontRatio;

public class Ratio_Page_Main extends Ratio {
    private float Logged_background_width_weight = (float) 1.0;
    private float Logged_background_height_weight = (float) 0.78;
    private float Logged_logo_width_weight = (float) 0.17;
    private float Logged_logo_height_weight = (float) 0.165;
    private float Logged_logo_text_height_weight = (float) 0.034;
    private float Logged_button_width_weight = (float) 0.4;
    private float Logged_button_height_weight = (float) 0.07;


    public Ratio_Page_Main(int width, int height) {
        super(width, height);
    }

    public int getBackgroundWidth() {
        return (int) (this.DisplayWidth * this.Logged_background_width_weight);
    }

    public int getBackgroundHeight() {
        return (int) (this.DisplayHeight * this.Logged_background_height_weight);
    }

    public int getLogoWidth() {
        return (int) (this.DisplayHeight * this.Logged_logo_width_weight);
    }

    public int getLogoHeight() {
        return (int) (this.DisplayHeight * this.Logged_logo_height_weight);
    }

    public int getLogoTextHeight() {
        return (int) (this.DisplayHeight * this.Logged_logo_text_height_weight);
    }

    public int getButtonWidth() {
        return (int) (this.DisplayWidth * this.Logged_button_width_weight);
    }

    public int getButtonHeight() {
        return (int) (this.DisplayHeight * this.Logged_button_height_weight);
    }

}
