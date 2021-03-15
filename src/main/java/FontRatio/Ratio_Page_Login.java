package FontRatio;

public class Ratio_Page_Login extends Ratio{
    private float Login_page_rectangle_width_weight = (float) 0.7;
    private float Login_page_rectangle_height_weight = (float) 0.2;
    private float Login_page_submit_width_weight = (float) 0.7;
    private float Login_page_submit_height_weight = (float) 0.13;
    private float Login_page_logintext_width_weight = (float) 0.36;
    private float Login_page_logintext_height_weight = (float) 0.1;
    private float Login_page_union_width_weight = (float) 0.11;
    private float Login_page_union_height_weight = (float) 0.06;


    public Ratio_Page_Login(int width, int height){
        super(width,height);
    }

    public int getRectangleWidth(){return (int)(this.DisplayWidth*this.Login_page_rectangle_width_weight);}
    public int getRectangleHeight(){return (int)(this.DisplayHeight*this.Login_page_rectangle_height_weight);}
    public int getSubmitWidth(){return (int)(this.DisplayWidth*this.Login_page_submit_width_weight);}
    public int getSubmitHeight(){return (int)(this.DisplayHeight*this.Login_page_submit_height_weight);}
    public int getLoginTextWidth(){return (int)(this.DisplayWidth*this.Login_page_logintext_width_weight);}
    public int getLoginTextHeight(){return (int)(this.DisplayHeight*this.Login_page_logintext_height_weight);}
    public int getUnionWidth(){return (int)(this.DisplayWidth*this.Login_page_union_width_weight);}
    public int getUnionHeight(){return (int)(this.DisplayHeight*this.Login_page_union_height_weight);}


}
