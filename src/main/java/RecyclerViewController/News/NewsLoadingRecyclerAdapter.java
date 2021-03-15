package RecyclerViewController.News;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import Data.StaticData.UserBulletinInfo;
import activity.R;

public class NewsLoadingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int TextSize = 14;
    private int DateTextSize = 13;
    private int ItemHeight;
    private int Group;

    private OnRecyclerViewClickListener listener;


    public NewsLoadingRecyclerAdapter(int itemHeight , int group , float density) {
        float weight =  density/((density >= 2.5)?  (float) density : (float) (density + 0.35));
        Group = group;
        TextSize *= weight;
        DateTextSize *= weight;
        ItemHeight = itemHeight;
    }

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return UserBulletinInfo.getBulletin(Group) == null ? 0 : UserBulletinInfo.getBulletin(Group).size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_news_item, parent, false);
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
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }


    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
        viewHolder.constraintLayout.getLayoutParams().height = ItemHeight;

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        viewHolder.news_card.getLayoutParams().height = ItemHeight;
        viewHolder.news_card.setTag(Group+","+position);
            if (Group == 2) {
                viewHolder.draweeView.setBackgroundResource(ChooseIcon(UserBulletinInfo.getBulletin(Group).get(position).getIcon()));
            }
            else{
                try {
                    Uri uri = Uri.parse(UserBulletinInfo.getBulletin(Group).get(position).getIcon());
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            .setOldController(viewHolder.draweeView.getController())
                            .build();
                    viewHolder.draweeView.setController(controller);
                }catch (Exception e){
                    viewHolder.draweeView.getHierarchy().setPlaceholderImage(R.drawable.image_course_default_image);
                    System.out.println("NewsLoadingRecyclerAdapter Error");
                    e.printStackTrace();
                }
            }
        viewHolder.item_sortname.setText(UserBulletinInfo.getBulletin(Group).get(position).getSortName());
        viewHolder.item_title.setText(UserBulletinInfo.getBulletin(Group).get(position).getTitle());
        viewHolder.item_date.setText(UserBulletinInfo.getBulletin(Group).get(position).getDate());
        viewHolder.item_date.setTextSize(DateTextSize);
        viewHolder.item_title.setTextSize(TextSize);
        viewHolder.item_sortname.setTextSize(TextSize);
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
    public int getItemViewType(int position) {
        return UserBulletinInfo.getBulletin(Group).get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView news_card;
        SimpleDraweeView draweeView;
        TextView item_sortname;
        TextView item_date;
        TextView item_title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            news_card = itemView.findViewById(R.id.News_card);
            draweeView = itemView.findViewById(R.id.news_item_drawee);
            item_sortname = itemView.findViewById(R.id.news_item_sortname);
            item_title = itemView.findViewById(R.id.news_item_title);
            item_date = itemView.findViewById(R.id.news_item_date);
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
