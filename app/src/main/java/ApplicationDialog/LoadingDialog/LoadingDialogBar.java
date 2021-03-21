package ApplicationDialog.LoadingDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import FontRatio.Ratio_Loading_Dialog;
import FontSet.FontSetter;
import activity.R;

public class LoadingDialogBar extends Dialog{

    public LoadingDialogBar(@NonNull Context context, Ratio_Loading_Dialog ratio_loading , float density) {
        super(context);
        setContentView(R.layout.dialog_loading);
        this.findViewById(R.id.progressBar).getLayoutParams().width = ratio_loading.getLoadingDialogWidth();
        this.findViewById(R.id.progressBar).getLayoutParams().height = ratio_loading.getLoadingDialogWidth();
        ConstraintLayout constraintLayout = (ConstraintLayout)this.findViewById(R.id.loading_dialog_layout);
        FontSetter.resizeTextfont(constraintLayout,density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

}
