package RecyclerViewController.Curriculum.CurriculumTable;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import FontSet.FontSetter;
import Data.CrawlerData.CurriculumData.Content;
import Data.StaticData.UserCurriculumInfo;
import activity.R;

public class CurriculumTableChildRecyclerAdapter extends RecyclerView.Adapter<CurriculumTableChildRecyclerAdapter.MyViewHoler> {


    ArrayList<String> itemTimeArrayList = new ArrayList<>(Arrays.asList("08:10 ~ 09:00","09:10 ~ 10:00","10:10 ~ 11:00","11:10 ~ 12:00","12:50 ~ 13:40","13:50 ~ 14:40","14:50 ~ 15:40","15:50 ~ 16:40","16:50 ~ 17:40","","18:20 ~ 19:05","19:05 ~ 19:50","20:00 ~ 20:45","20:45 ~ 21:30"));
    ArrayList<String> arrayList;
    private boolean isDialog;
    private double Density;
    private int Width,Height;
    private ArrayList<Content> thisDayCurriculum;
    private HashMap<Integer,Integer> SectionList = new HashMap<>();

    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_curriculum_item,parent,false);
        return new MyViewHoler(view);
    }

    public CurriculumTableChildRecyclerAdapter(ArrayList<String> arrayList, int width, int height, double density , int DayOfWeek ,boolean isDialog ) {
        this.arrayList = arrayList;
        this.Density = density;
        this.Width = width;
        this.Height = height;
        this.isDialog = isDialog;
        this.thisDayCurriculum = UserCurriculumInfo.getMyCurriculum().getContent(DayOfWeek);
        getSectionList();

    }

    private void getSectionList(){
        if (thisDayCurriculum != null){
            for (int i = 0 ; i < thisDayCurriculum.size() ; i++)
                SectionList.put(thisDayCurriculum.get(i).getSection(),i);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {
        holder.item_name.setVisibility(View.VISIBLE);
        holder.item_teacher.setVisibility(View.VISIBLE);
        holder.item_location.setVisibility(View.VISIBLE);
        holder.item_title.setText(arrayList.get(position));
        holder.layout.getLayoutParams().height = (int) (Height);
        if (!isDialog)
            holder.background.getLayoutParams().width = (int) ((Width) - 11 * Density);
        enlargeTextSize(holder);
        if(SectionList.containsKey(position)){
            if(isDialog){
                holder.item_time.setVisibility(View.VISIBLE);
            }
            else{

                holder.item_time.setVisibility(View.GONE);
            }
            holder.item_time.setText(itemTimeArrayList.get(position));
            holder.item_name.setText(thisDayCurriculum.get(SectionList.get(position)).getCourseInfo().getName());
            holder.item_teacher.setText(thisDayCurriculum.get(SectionList.get(position)).getCourseInfo().getTeacher());
            holder.item_location.setText(thisDayCurriculum.get(SectionList.get(position)).getCourseInfo().getLocation());
        }
        else{
            holder.item_name.setVisibility(View.GONE);
            holder.item_time.setText("");
            holder.item_name.setText("");
            holder.item_teacher.setText("");
            holder.item_location.setText("");
        }
        FontSetter.resizeTextfont(holder.layout, (float) Density);
    }

    private void enlargeTextSize(MyViewHoler holder){
        holder.item_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,(isDialog)?17:16);
        holder.item_time.setTextSize(TypedValue.COMPLEX_UNIT_SP,(isDialog)?15:14);
        holder.item_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,(isDialog)?15:14);
        holder.item_teacher.setTextSize(TypedValue.COMPLEX_UNIT_SP,(isDialog)?15:14);
        holder.item_location.setTextSize(TypedValue.COMPLEX_UNIT_SP,(isDialog)?15:14);
    }

    @Override
    public int getItemCount() {
        return arrayList.size(); }

    public  class  MyViewHoler extends RecyclerView.ViewHolder{
        TextView item_title,item_name,item_teacher,item_location,item_time;
        CardView background;
        LinearLayout layout;
        public  MyViewHoler(@NonNull View itemView ){
            super(itemView);
            item_title = itemView.findViewById(R.id.curriculum_item);
            item_name = itemView.findViewById(R.id.curriculum_item_name);
            item_teacher = itemView.findViewById(R.id.curriculum_item_teacher);
            item_location = itemView.findViewById(R.id.curriculum_item_location);
            item_time = itemView.findViewById(R.id.curriculum_item_time);
            background = itemView.findViewById(R.id.child_content);
            layout = itemView.findViewById(R.id.curriculum_item_layout);
        }
    }
}
