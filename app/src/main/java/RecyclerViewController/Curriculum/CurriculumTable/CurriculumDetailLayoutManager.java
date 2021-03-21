package RecyclerViewController.Curriculum.CurriculumTable;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CurriculumDetailLayoutManager extends LinearLayoutManager{
    private boolean isScrollEnabled = false;

    public CurriculumDetailLayoutManager(Context context) {
        super(context,LinearLayoutManager.VERTICAL,false);
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
