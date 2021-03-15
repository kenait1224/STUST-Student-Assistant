package RecyclerViewController.News;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import Data.StaticData.UserBulletinInfo;
import activity.R;

public class NewsContentRecyclerAdapter extends RecyclerView.Adapter<NewsContentRecyclerAdapter.MyViewHolder> {

    private int Group;
    private int ItemHeight;
    private int TextSize = 14;
    private int DateTextSize = 13;

    private OnRecyclerViewClickListener listener;

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public NewsContentRecyclerAdapter(int itemHeight, float weight, int group) {
        Group = group;
        ItemHeight = itemHeight;
        TextSize *= weight;
        DateTextSize *= weight;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_news_item, parent, false);
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
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.news_card.getLayoutParams().height = ItemHeight;
        holder.news_card.setTag(Group+","+position);
        holder.draweeView.getHierarchy().setFailureImage(R.drawable.image_course_default_image);
        if (UserBulletinInfo.getBulletin(Group).size() >= position) {
            if (Group == 2) {
                holder.draweeView.setBackgroundResource(ChooseIcon(UserBulletinInfo.getBulletin(Group).get(position).getIcon()));
            }
            else{
                try {
                    Uri uri = Uri.parse(UserBulletinInfo.getBulletin(Group).get(position).getIcon());
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setOldController(holder.draweeView.getController())
                            .build();
                    holder.draweeView.setController(controller);
                }catch (Exception e){
                    System.out.println("ScaleRecyclerViewAdapter Error");
                    e.printStackTrace();
                }
            }
            holder.item_sortname.setText(UserBulletinInfo.getBulletin(Group).get(position).getSortName());
            holder.item_title.setText(UserBulletinInfo.getBulletin(Group).get(position).getTitle());
            holder.item_date.setText(UserBulletinInfo.getBulletin(Group).get(position).getDate());
        }
        holder.item_date.setTextSize(DateTextSize);
        holder.item_title.setTextSize(TextSize);
        holder.item_sortname.setTextSize(TextSize);
    }


    private int ChooseIcon(String target) {
        switch (target){
            case "今日公告":
                return R.drawable.ic_news_bulletin_today;
            case"昨日公告":
                return R.drawable.ic_news_bulletin_yesterday;
            default:
                return R.drawable.ic_news_bulletin_normal;
        }
    }

    @Override
    public int getItemCount() {
        return ((UserBulletinInfo.getBulletin(Group).size() > 5) ? 5 : UserBulletinInfo.getBulletin(Group).size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView news_card;
        SimpleDraweeView draweeView;
        TextView item_sortname;
        TextView item_date;
        TextView item_title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            news_card = itemView.findViewById(R.id.News_card);
            draweeView = itemView.findViewById(R.id.news_item_drawee);
            item_sortname = itemView.findViewById(R.id.news_item_sortname);
            item_title = itemView.findViewById(R.id.news_item_title);
            item_date = itemView.findViewById(R.id.news_item_date);

        }
    }
}
