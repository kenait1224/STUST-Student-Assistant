package RecyclerViewController.Course.ExpandableRecyclerView.Item;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import activity.R;
import FontRatio.Ratio_Course;
import RecyclerViewController.Course.ExpandableRecyclerView.Menu.CourseMenu;
import RecyclerViewController.Course.ExpandableRecyclerView.Menu.CourseMenuViewHolder;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;

public class CourseItemAdapter extends ExpandableRecyclerViewAdapter<CourseMenuViewHolder,CourseItemViewHolder> {

    DisplayMetrics display;
    Ratio_Course ratioTable;

    private OnRecyclerViewClickListener listener;

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public CourseItemAdapter(List<? extends ExpandableGroup> groups, DisplayMetrics display, Ratio_Course ratio_table) {
        super(groups);
        this.display = display;
        ratioTable = ratio_table;
    }

    private int getGroupIndex(String s){
        switch (s){
            case "課程活動":
                return 0;
            case "課程教材":
                return 1;
            case "課程作業":
                return 2;
        }
        return 0;
    }

    @Override
    public CourseMenuViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_course_detail_title,parent,false);
        return new CourseMenuViewHolder(view,ratioTable.getCourseDetailTitleHeight(),display);
    }

    @Override
    public CourseItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_course_detail_content,parent,false);
        if(listener != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(v);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClickListener(v);
                    return true;
                }
            });
        }
        return new CourseItemViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(CourseItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        CourseItem courseItem = (CourseItem) group.getItems().get(childIndex);
        holder.setContent(courseItem,getGroupIndex(group.getTitle()),childIndex,display,ratioTable);
    }
    @Override
    public void onBindGroupViewHolder(CourseMenuViewHolder holder, int flatPosition, ExpandableGroup group) {
        CourseMenu courseMenu = (CourseMenu) group;
        holder.setContent(courseMenu);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.setAdapter(null);
    }
}
