package FontRatio;

public class Ratio_Detail_Dialog extends Ratio {

    private float DialogAttachmentHeight_weight = (float) 0.08;
    private float DetailDialogUnionPadding_weight= (float) 0.15;
    private float DetailDialogAttachmentContentHeight_weight = (float) 0.125;
    private float DetailDialogHtmlViewPadding_weight = (float) 0.04;
    private float DetailDialogFileButtonHeight_weight = (float) 0.05;
    private float DetailDialogFileButtonWidth_weight = (float) 0.5;
    private float DetailDialogFileIconHeight_weight = (float) 0.08;
    private float DetailDialogFileIconWidth_weight = (float) 0.08;
    private float DetailDialogUnionSide_weight = (float) 0.4;
    public Ratio_Detail_Dialog(int width, int height) {
        super(width, height);
    }

    public int getDetailDialogUnionPadding(){return (int) ((int)DisplayHeight*0.2*DetailDialogUnionPadding_weight);}
    public int getDetailDialogAttachmentHeight(){return (int) ((int)DisplayHeight*DialogAttachmentHeight_weight);}
    public int getDetailDialogHtmlViewPadding(){return (int) (DisplayHeight*0.8*DetailDialogHtmlViewPadding_weight);}
    public int getDetailDialogFileButtonHeight(){return (int) (DisplayHeight*DetailDialogFileButtonHeight_weight);}
    public int getDetailDialogFileButtonWidth(){return (int) (DisplayWidth*DetailDialogFileButtonWidth_weight);}
    public int getDetailDialogFileIconHeight(){return (int) (DisplayHeight*DetailDialogFileIconHeight_weight);}
    public int getDetailDialogFileIconWidth(){return (int) (DisplayWidth*DetailDialogFileIconWidth_weight);}
    public int getDetailDialogAttachmentContentHeight(){return (int) (DisplayHeight*DetailDialogAttachmentContentHeight_weight);}
}
