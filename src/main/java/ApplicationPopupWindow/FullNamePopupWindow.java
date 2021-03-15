package ApplicationPopupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import activity.R;

import FontSet.FontSetter;

public class FullNamePopupWindow extends PopupWindow {
    private int width;
    private int height;
    String content;
    public TextView textView;

    public FullNamePopupWindow(Context context, String target, int MaxWidth , float density) {
        super(context);
        this.content = target;
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_full_name,
                null, false);
        ConstraintLayout layout = (ConstraintLayout) contentView.findViewById(R.id.popup_layout);
        textView = (TextView) contentView.findViewById(R.id.textdd);
        textView.setText(target);
        textView.setMaxWidth(MaxWidth);
        FontSetter.resizeTextfont(layout,density);
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        height = layout.getMeasuredHeight();
        width = layout.getMeasuredWidth();
        setContentView(contentView);
    }


    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
