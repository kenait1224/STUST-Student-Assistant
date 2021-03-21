package ApplicationDialog.OfflineModeDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import FontRatio.Ratio_Offline_Mode_Dialog;
import FontSet.FontSetter;
import Data.StaticData.UserConfig;
import activity.R;
import activity.UserInterface_Page;

public class OfflineModeAsked extends Dialog {

    public OfflineModeAsked(@NonNull final Context context, Ratio_Offline_Mode_Dialog ratioOfflineModeDialog, float density, int mode) {
        super(context);
        setContentView(R.layout.dialog_offline_mode);
        LinearLayout DialogLayout = findViewById(R.id.offline_mode_dialog_layout);
        ImageView Offline_Mode_Icon = findViewById(R.id.offline_mode_dialog_icon);
        TextView Offline_Mode_Title = findViewById(R.id.offline_mode_dialog_title);
        TextView Offline_Mode_Content= findViewById(R.id.offline_mode_dialog_content);
        Button Cancel_Button = findViewById(R.id.offline_mode_dialog_cancel);
        Button Enter_Button = findViewById(R.id.offline_mode_dialog_enter);
        if(mode == 1){
            Offline_Mode_Title.setText("離線模式");
            Offline_Mode_Content.setText("離線模式已啟用是否要繼續?");
        }
        Cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dismiss();
            }
        });
        Enter_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                UserConfig.setMyConfig("OfflineMode",1);
                Intent intent = new Intent(context, UserInterface_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ActivityID", UserConfig.getMyConfig().get("StartPage"));
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }
        });
        FontSetter.SetLinearLayoutSize(DialogLayout,ratioOfflineModeDialog.getOfflineModeDialogWidth(),ratioOfflineModeDialog.getOfflineModeDialogHeight());
        FontSetter.SetImageViewSize(Offline_Mode_Icon,ratioOfflineModeDialog.getOfflineModeIconWidth(),ratioOfflineModeDialog.getOfflineModeIconHeight());
        FontSetter.SetButtonSize(Cancel_Button,ratioOfflineModeDialog.getOfflineModeButtonWidth(),ratioOfflineModeDialog.getOfflineModeButtonHeight());
        FontSetter.SetButtonSize(Enter_Button,ratioOfflineModeDialog.getOfflineModeButtonWidth(),ratioOfflineModeDialog.getOfflineModeButtonHeight());
        FontSetter.resizeTextfont(DialogLayout,density);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

}
