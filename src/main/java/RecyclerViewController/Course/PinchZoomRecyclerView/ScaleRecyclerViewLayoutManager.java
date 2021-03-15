package RecyclerViewController.Course.PinchZoomRecyclerView;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScaleRecyclerViewLayoutManager extends LinearLayoutManager {
    private int mParentWidth;
    private int mItemHeight;
    
    public ScaleRecyclerViewLayoutManager(Context context, int parentHeight, int itemHeight ) {
        super(context , RecyclerView.VERTICAL,false);
        mParentWidth = parentHeight;
        mItemHeight = itemHeight;
    }

    @Override
    public int getPaddingBottom() {
        return Math.round(mParentWidth / 2f - mItemHeight / 2f);
    }
}
