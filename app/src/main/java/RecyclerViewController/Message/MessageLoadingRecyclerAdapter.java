package RecyclerViewController.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import Data.CrawlerData.MessageData.MessageInfo;
import Data.StaticData.UserMessageInfo;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import activity.R;

public class MessageLoadingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int SortNameTextSize = 17;
    private int OtherTextSize = 14;
    private int ItemHeight;
    private int Group;

    private OnRecyclerViewClickListener listener;


    public MessageLoadingRecyclerAdapter(int itemHeight , int group , float density) {
        float weight =  density/((density >= 2.5)?  (float) density : (float) (density + 0.35));
        Group = group;
        SortNameTextSize *= weight;
        OtherTextSize *= weight;
        ItemHeight = itemHeight;
    }

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MessageLoadingRecyclerAdapter.ItemViewHolder) {
            populateItemRows((MessageLoadingRecyclerAdapter.ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof MessageLoadingRecyclerAdapter.LoadingViewHolder) {
            showLoadingView((MessageLoadingRecyclerAdapter.LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return UserMessageInfo.getMessage(Group) == null ? 0 : UserMessageInfo.getMessage(Group).size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_message_item, parent, false);
            if(listener!= null){
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
            return new MessageLoadingRecyclerAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_loading, parent, false);
            return new MessageLoadingRecyclerAdapter.LoadingViewHolder(view);
        }
    }


    private void showLoadingView(MessageLoadingRecyclerAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
        viewHolder.constraintLayout.getLayoutParams().height = ItemHeight;

    }

    private void populateItemRows(MessageLoadingRecyclerAdapter.ItemViewHolder holder, int position) {
        MessageInfo messageInfo = UserMessageInfo.getMessage(Group).get(position);
        holder.itemView.setTag(Group+","+position);
        holder.itemView.getLayoutParams().height = ItemHeight;
        holder.SortName.setText(messageInfo.getSortName());
        holder.Title.setText(messageInfo.getTitle());
        holder.Deadline.setText(messageInfo.getDeadLine());
        holder.SortName.setTextSize(SortNameTextSize);
        holder.Deadline.setTextSize(OtherTextSize);
        holder.Title.setTextSize(OtherTextSize);
    }


    @Override
    public int getItemViewType(int position) {
        return UserMessageInfo.getMessage(Group).get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Layout;
        TextView SortName;
        TextView Deadline;
        TextView Title;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Layout = itemView.findViewById(R.id.message_item_layout);
            SortName = itemView.findViewById(R.id.message_item_sortname);
            Deadline = itemView.findViewById(R.id.message_item_date);
            Title = itemView.findViewById(R.id.message_item_title);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.news_loading_layout);
        }
    }
}
