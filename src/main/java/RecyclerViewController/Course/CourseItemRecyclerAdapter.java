package RecyclerViewController.Course;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import FontSet.FontSetter;
import Data.CrawlerData.CourseData.CourseInfo;
import Data.StaticData.UserCourseInfo;
import activity.R;

public class CourseItemRecyclerAdapter extends RecyclerView.Adapter<CourseItemRecyclerAdapter.MyViewHolder> {

    ArrayList<CourseInfo> Content;
    float Density;
    private int Source;
    private int ItemHeight;
    private float TextSize = 17;
    private float SourceTextSize = 20;


    public CourseItemRecyclerAdapter(int itemHeight, float density, int source, ArrayList<CourseInfo> content) {
        float weight = density / ((density >= 2.5) ? (float) density : (float) (density + 0.35));
        ItemHeight = itemHeight;
        Density = density;
        TextSize *= weight;
        SourceTextSize *= weight;
        Source = source;
        Content = content;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_course_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.background.getLayoutParams().height = ItemHeight;
//        holder.course_source.setVisibility((position == 0) ? View.VISIBLE : View.GONE);
        holder.course_source.setText((Source == 0 )?"Flip":"Flip Class");
        holder.course_name.setText(Content.get(position).getCourseName());
        holder.course_teachers.setText(Content.get(position).getTeachersString());
        holder.course_source.setTextSize(SourceTextSize);
        holder.course_title.setTextSize(TextSize);
        holder.course_name.setTextSize(TextSize);
        holder.course_teachers.setTextSize(TextSize);
        FontSetter.resizeTextfont(holder.layout, Density);
        holder.course_image.getHierarchy().setFailureImage(R.drawable.image_course_default_image);
//        if(Content.get(position).getCoursePhoto() != null)
        try {
            Uri uri = Uri.parse(Content.get(position).getCoursePhoto());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setOldController(holder.course_image.getController())
                    .build();
            holder.course_image.setController(controller);

        } catch (Exception e) {
            System.out.println("CourseItemRecyclerAdapter Error");
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return UserCourseInfo.getCourseInfo(Source).size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView background;
        SimpleDraweeView course_image;
        TextView course_name;
        TextView course_teachers;
        TextView course_title;
        TextView course_source;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.course_content_layout);
            course_source = itemView.findViewById(R.id.course_item_source);
            course_image = itemView.findViewById(R.id.Course_image);
            course_name = itemView.findViewById(R.id.Course_name);
            course_title = itemView.findViewById(R.id.Course_Itme_Title);
            course_teachers = itemView.findViewById(R.id.Course_teachers);
            background = itemView.findViewById(R.id.course_item_content);
        }
    }
}
