package RecyclerViewController.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import Data.StaticData.UserBulletinInfo;
import activity.R;

public class NewsGroupRecyclerAdapter extends RecyclerView.Adapter<NewsGroupRecyclerAdapter.MyViewHolder> {

    Context context;
    private String[] bulletin = {"課程公告 Flip", "課程公告 Flip Class", "學校公告"};
    private float Weight;
    private int ItemHeight;
    private int TitleTextSize = 18;
    private int NoDataTextSize = 16;
    private int ShowAllTextSize = 14;

    private OnRecyclerViewClickListener Item_Listener;
    private OnRecyclerViewClickListener Show_Listener;

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        Item_Listener = itemClickListener;
    }

    public void setShowAllListener(OnRecyclerViewClickListener showClickListener) {
        Show_Listener = showClickListener;
    }

    public NewsGroupRecyclerAdapter(Context context, float density, int itemHeight) {
        this.context = context;
        Weight = density / ((density >= 2.5) ? (float) density : (float) (density + 0.35));
        TitleTextSize *= Weight;
        ShowAllTextSize *= Weight;
        NoDataTextSize *= Weight;
        ItemHeight = itemHeight;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_news_group, parent, false);
        TextView showAll = view.findViewById(R.id.news_group_show_all);
        if (Show_Listener != null) {
            showAll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Show_Listener.onClickListener(v);
                }
            });
            showAll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Show_Listener.onLongClickListener(v);
                    return true;
                }
            });
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ShowAll.setVisibility((UserBulletinInfo.getBulletin(position) != null && UserBulletinInfo.getBulletin(position).size() > 5) ? View.VISIBLE : View.INVISIBLE);
        holder.NoDataHint.setVisibility((UserBulletinInfo.getBulletin(position) == null || UserBulletinInfo.getBulletin(position).size() == 0) ? View.VISIBLE: View.GONE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        NewsContentRecyclerAdapter newsContentRecyclerAdapter = new NewsContentRecyclerAdapter(ItemHeight, Weight, position);
        newsContentRecyclerAdapter.setItemClickListener(Item_Listener);
        holder.ShowAll.setTag(position);
        holder.NewsGroupRV.setLayoutManager(layoutManager);
        holder.NoDataHint.getLayoutParams().height = ItemHeight;
        holder.NoDataHint.setTextSize(NoDataTextSize);
        holder.NewsGroupRV.setHasFixedSize(true);
        holder.NewsGroupRV.setAdapter(newsContentRecyclerAdapter);
        holder.GroupTitle.setText(bulletin[position]);
        holder.GroupTitle.setTextSize(TitleTextSize);
        holder.ShowAll.setTextSize(ShowAllTextSize);
    }


    @Override
    public int getItemCount() {
        return bulletin.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Layout;
        TextView GroupTitle;
        TextView NoDataHint;
        Button ShowAll;
        RecyclerView NewsGroupRV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Layout = itemView.findViewById(R.id.news_group_layout);
            GroupTitle = itemView.findViewById(R.id.news_group_title);
            NoDataHint = itemView.findViewById(R.id.news_no_data);
            ShowAll = itemView.findViewById(R.id.news_group_show_all);
            NewsGroupRV = itemView.findViewById(R.id.news_item_RV);
        }
    }
}
