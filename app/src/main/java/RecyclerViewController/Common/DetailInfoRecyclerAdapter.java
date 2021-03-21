package RecyclerViewController.Common;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Data.CrawlerData.CourseDetailInfo.DetailInfoContent;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import activity.R;

public class DetailInfoRecyclerAdapter extends RecyclerView.Adapter<DetailInfoRecyclerAdapter.MyViewHolder> {
    ArrayList<DetailInfoContent> Target;
    private OnRecyclerViewClickListener listener;
    private Boolean NotCourseInfo = false;
    float Text_Size = 16;

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public void setNotCourseInfo(Boolean notCourseInfo) {
        NotCourseInfo = notCourseInfo;
    }

    public DetailInfoRecyclerAdapter(ArrayList<DetailInfoContent> target, float density) {
        float weight = density / ((density >= 2.5) ? (float) density : (float) (density + 0.35));
        Target = target;
        Text_Size *= weight;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_detail_info_content, parent, false);
        if (listener != null) {
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
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.info_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, Text_Size);
        holder.info_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, Text_Size);
        if (position != Target.size() - 1 || NotCourseInfo) {
            holder.info_title.setVisibility(View.VISIBLE);
            holder.info_content.setVisibility(View.VISIBLE);
            holder.info_content.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            holder.info_title.setText(Target.get(position).getTitle());
            holder.info_content.setText(Target.get(position).getContent());
        } else {
            holder.info_title.setVisibility(View.VISIBLE);
            holder.info_title.setGravity(Gravity.CENTER);
            holder.info_content.setVisibility(View.GONE);
            holder.info_title.setText(Target.get(position).getTitle());
            holder.info_content.setText(Target.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return Target.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView info_title;
        TextView info_content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.detail_dialog_info_item_layout);
            info_title = itemView.findViewById(R.id.detail_dialog_info_title);
            info_content = itemView.findViewById(R.id.detail_dialog_info_content);
        }
    }
}
