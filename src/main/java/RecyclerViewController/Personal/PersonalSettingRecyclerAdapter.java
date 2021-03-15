package RecyclerViewController.Personal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import activity.R;

public class PersonalSettingRecyclerAdapter extends RecyclerView.Adapter<PersonalSettingRecyclerAdapter.MyViewHolder>{

    ArrayList<String> Content;
    int Text_Size = 17;
    OnRecyclerViewClickListener listener;

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public PersonalSettingRecyclerAdapter(ArrayList<String> content,float density) {
        float weight = density / ((density >= 2.5) ? (float) density : (float) (density + 0.35));
        Text_Size *= weight;
        Content = content;
    }

    @NonNull
    @Override
    public PersonalSettingRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_personal_setting_content,parent,false);
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
                    return false;
                }
            });
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.textView.setText(Content.get(position));
        holder.textView.setTextSize(Text_Size);
    }


    @Override
    public int getItemCount() {
        return Content.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.personal_setting_text);
        }
    }
}
