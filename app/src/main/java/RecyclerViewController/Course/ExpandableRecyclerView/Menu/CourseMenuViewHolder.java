package RecyclerViewController.Course.ExpandableRecyclerView.Menu;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import FontSet.FontSetter;
import activity.R;

public class CourseMenuViewHolder extends GroupViewHolder {
    private TextView textView;
    private ImageView arrow;
    private float TextSize = 17;
    private int Height;


    public CourseMenuViewHolder(View itemView , int height ,DisplayMetrics displayMetrics) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.course_detail_title);
        textView.setTextSize(TextSize);
        arrow = (ImageView)itemView.findViewById(R.id.course_detail_arrow);
        LinearLayout linearLayout = (LinearLayout)itemView.findViewById(R.id.course_detail_title_layout);
        FontSetter.resizeTextfont(linearLayout,displayMetrics.density);
        Height = height;
    }

    public void setContent(CourseMenu content){
        textView.setText(content.getTitle());
        textView.getLayoutParams().height = Height;
        arrow.getLayoutParams().height = Height/2;
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        arrow.setImageResource(R.drawable.ic_course_detail_list_expan_anim);
        if(arrow.getDrawable() instanceof  AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) arrow.getDrawable();
            animatedVectorDrawable.start();
        }
        else{
            Animatable animatable = (Animatable) arrow.getDrawable();
            animatable.start();
        }
    }

    private void animateCollapse() {
        arrow.setImageResource(R.drawable.ic_course_detail_list_reduce_anim);
        if(arrow.getDrawable() instanceof  AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) arrow.getDrawable();
            animatedVectorDrawable.start();
        }
        else{
            Animatable animatable = (Animatable) arrow.getDrawable();
            animatable.start();
        }
    }
}
