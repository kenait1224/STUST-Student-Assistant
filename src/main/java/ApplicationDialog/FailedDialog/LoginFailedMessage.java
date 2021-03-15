package ApplicationDialog.FailedDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import FontRatio.Ratio_Error_Dialog;
import FontSet.FontSetter;
import activity.R;

public class LoginFailedMessage extends Dialog {

    public LoginFailedMessage(@NonNull Context context, Ratio_Error_Dialog ratio_error_dialog, float density) {
        super(context);
        setContentView(R.layout.dialog_login_failed);
        LinearLayout linearLayout = findViewById(R.id.Error_Dialog_Layout);
        Button dismiss = findViewById(R.id.Error_Dialog_Dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        FontSetter.SetLinearLayoutSize(linearLayout,ratio_error_dialog.getErrorDialogWidth(),ratio_error_dialog.getErrorDialogHeight());
        FontSetter.resizeTextfont(linearLayout,density);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

}
