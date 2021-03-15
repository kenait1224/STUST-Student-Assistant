package RecyclerViewController.Course.ExpandableRecyclerView.Item;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import activity.R;
import FontRatio.Ratio_Course;
import FontSet.FontSetter;

public class CourseItemViewHolder extends ChildViewHolder {
    private LinearLayout linearLayout;
    private FrameLayout frameLayout;
    private TextView textView ;
    private  ImageView icon;
    private String link;
    private float TitleTextSize = 17;
    private float NormalTextSize =16;

    public CourseItemViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.course_detail_content);
        linearLayout = (LinearLayout)itemView.findViewById(R.id.course_detail_linearlayout);
        frameLayout = (FrameLayout) itemView.findViewById(R.id.course_detail_fragmelayout);
        icon = (ImageView)itemView.findViewById(R.id.course_detail_icon);
    }

    public void setContent(CourseItem content, int group_idx,int child_idx ,DisplayMetrics displayMetrics, Ratio_Course ratio_course ){
        int widthPadding =(content.isTitle)?ratio_course.getCourseDetailTitleWidthPadding():ratio_course.getCourseDetailContentWidthPadding();
        textView.setTextColor((content.Type.compareTo("title_content") == 0)?Color.parseColor("#594B26"):Color.parseColor("#000000"));
        frameLayout.setPadding(widthPadding
                ,(content.isTitle)?ratio_course.getCourseDetailTitlePadding():ratio_course.getCourseDetailContentPadding()
                ,widthPadding
                ,ratio_course.getCourseDetailContentPadding());
        linearLayout.setBackgroundResource((content.isTitle)?((content.Type.compareTo("title_content") == 0)?R.drawable.background_xml_course_detail_menu_title_content:R.drawable.background_xml_course_detail_menu_title):R.drawable.background_xml_course_detail_menu_content);
        icon.setImageResource(ChoseIcon(content.Type));
        icon.getLayoutParams().height = (int)(ratio_course.getCourseDetailContentHeight()/1.9);
        textView.setText(content.Name);
        textView.setTag(group_idx+","+child_idx);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,(content.isTitle)?TitleTextSize:NormalTextSize);
        textView.getLayoutParams().height = (content.isTitle)?(int)(ratio_course.getCourseDetailContentHeight()*1.1):ratio_course.getCourseDetailContentHeight();
        FontSetter.resizeTextfont(linearLayout,displayMetrics.density);
    }

    public int ChoseIcon(String type){
        switch (type) {
            case "title_content":
                return R.drawable.ic_course_detail_menu_title_content;
            case "title":
                return R.drawable.ic_course_detail_menu_title;
            case "doc":
                return R.drawable.ic_course_detail_menu_doc;
            case "iframe":
                return R.drawable.ic_course_detail_menu_iframe_ver2;
            case "pdf":
                return R.drawable.ic_course_detail_menu_pdf;
            case "ppt":
                return R.drawable.ic_course_detail_menu_ppt;
            case "mp3":
                return R.drawable.ic_course_detail_menu_mp3;
            case "hyperlink":
                return R.drawable.ic_course_detail_menu_hyperlink;
            case "exercise":
                return R.drawable.ic_course_detail_menu_exercise;
            case "exerciseSuccessful":
                return R.drawable.ic_course_detail_menu_exercise_successful;
            case "poll":
                return R.drawable.ic_course_detail_menu_poll;
            case "embed":
                return R.drawable.ic_course_detail_menu_embed_ver2;
            case "exam":
                return R.drawable.ic_course_detail_menu_exam;
            case "null":
                return R.drawable.ic_course_detail_menu_null;
            default:
                return R.drawable.ic_course_detail_menu_default;
        }
    }
}
