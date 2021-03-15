package RecyclerViewController;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.recyclerview.widget.RecyclerView;

@Deprecated
public class ResizeWidthAnimation extends Animation {
    private int mWidth;
    private int mStartWidth;
    private int DisplayWidth;
    private RecyclerView rView;
    private View mView;

    public ResizeWidthAnimation(View view, int width , int displayWidth ,RecyclerView recyclerView) {
        mView = view;
        mWidth = width;
        DisplayWidth = displayWidth;
        rView = recyclerView;
        mStartWidth = view.getWidth();
    }

    public int getPaddingLeft(int mParentWidth , int mItemWidth) {
        return Math.round(mParentWidth / 2f - mItemWidth / 2f);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);
        mView.getLayoutParams().width = newWidth;
        mView.requestLayout();
    }


    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(0, height, 0, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
