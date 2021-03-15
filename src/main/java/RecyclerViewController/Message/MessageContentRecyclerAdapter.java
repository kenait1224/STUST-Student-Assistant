package RecyclerViewController.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import Data.CrawlerData.MessageData.MessageInfo;
import Data.StaticData.UserMessageInfo;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import activity.R;

public class MessageContentRecyclerAdapter extends RecyclerView.Adapter<MessageContentRecyclerAdapter.MyViewHolder> {

    private int SortNameTextSize = 17;
    private int OtherTextSize = 14;
    private int ItemHeight;

    OnRecyclerViewClickListener listener;

    public MessageContentRecyclerAdapter(float density, int itemHeight) {
        float Weight = density / ((density >= 2.5) ? (float) density : (float) (density + 0.35));
        SortNameTextSize *= Weight;
        OtherTextSize *= Weight;
        ItemHeight = itemHeight;
    }

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_message_item, parent, false);
        if (listener != null){
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
        MessageInfo messageInfo = UserMessageInfo.getMessage(0).get(position);
        holder.itemView.setTag(position);
        holder.itemView.getLayoutParams().height = ItemHeight;
        holder.SortName.setText(messageInfo.getSortName());
        holder.Title.setText(messageInfo.getTitle());
        holder.Deadline.setText(messageInfo.getDeadLine());
        holder.SortName.setTextSize(SortNameTextSize);
        holder.Deadline.setTextSize(OtherTextSize);
        holder.Title.setTextSize(OtherTextSize);
    }

    @Override
    public int getItemCount() {
        return UserMessageInfo.getMessage(0).size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView SortName;
        TextView Deadline;
        TextView Title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            SortName = itemView.findViewById(R.id.message_item_sortname);
            Deadline = itemView.findViewById(R.id.message_item_date);
            Title = itemView.findViewById(R.id.message_item_title);
        }
    }
}
