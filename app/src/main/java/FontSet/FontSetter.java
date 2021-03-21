package FontSet;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


public class FontSetter {

    public static void SetImageViewSize(ImageView Target, int width, int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }

    public static void SetViewSize(View Target, int width, int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }

    public static void SetButtonSize(Button Target , int width , int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }

    public static void SetImageButtonSize(ImageButton Target , int width , int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }


    public static void SetEditTextSize(EditText Target , int width , int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }

    public static void SetTextViewSize(TextView Target , int width , int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }

    public static void SetLinearLayoutSize(LinearLayout Target , int width , int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }

    public static void SetConstrainLayoutSize(ConstraintLayout Target , int width , int height){
        Target.getLayoutParams().width = width;
        Target.getLayoutParams().height = height;
    }

    public static void resizeTextfont(ViewGroup viewGroup ,float density){
        float weight = (density >= 2.5)?  (float) density : (float) (density + 0.35);
        for(int i = 0 ; i < viewGroup.getChildCount() ; i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup)
                resizeTextfont((ViewGroup) v ,density);
            else if (v instanceof TextView)
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (((TextView) v).getTextSize() / weight));
            else if(v instanceof Button)
                ((Button) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(((Button) v).getTextSize() / weight ));
            else if( v instanceof EditText)
                ((EditText) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(((EditText) v).getTextSize() / weight ));
        }
    }

}
