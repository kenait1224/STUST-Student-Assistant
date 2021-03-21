package RecyclerViewController.Curriculum.CurriculumTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import FontSet.FontSetter;
import activity.R;

public class CurriculumTableParentRecyclerAdapter extends RecyclerView.Adapter<CurriculumTableParentRecyclerAdapter.MyViewHolder>{

    Context context;
    ArrayList<String> parentArrayList;
    ArrayList<String> itemArrayList = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5","6","7","8","9","10","11","12","13","14"));
    float Density;
    int Dialog_position = -1;
    int[] BackgroundFontID;
    int ItemWidth,ItemHeight;


    public CurriculumTableParentRecyclerAdapter(ArrayList<String> parentArrayList, Context context, int[] backgroundFontID, int itemWidth , int itemHeight , float density ) {
        this.parentArrayList = parentArrayList;
        this.context = context;
        this.BackgroundFontID = backgroundFontID;
        this.ItemWidth = itemWidth;
        this.ItemHeight = itemHeight;
        this.Density = density;
    }

    public CurriculumTableParentRecyclerAdapter(ArrayList<String> parentArrayList, Context context, int[] backgroundFontID, int itemWidth , int itemHeight , float density , int dialog_position) {
        this.parentArrayList = parentArrayList;
        this.context = context;
        this.BackgroundFontID = backgroundFontID;
        this.ItemWidth = itemWidth;
        this.ItemHeight = itemHeight;
        this.Density = density;
        this.Dialog_position = dialog_position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_curriculum_group,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.ItemName.setText(parentArrayList.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.ChildRV.setLayoutManager(layoutManager);
        holder.ChildRV.getLayoutParams().width = ItemWidth;
        holder.ChildRV.setHasFixedSize(true);
        holder.ItemName.setBackgroundResource(BackgroundFontID[position]);
        CurriculumTableChildRecyclerAdapter childRecyclerAdapter = new CurriculumTableChildRecyclerAdapter(itemArrayList,ItemWidth,ItemHeight,Density,(Dialog_position != -1)?Dialog_position:position,(Dialog_position != -1)?true:false );
        holder.ChildRV.setAdapter(childRecyclerAdapter);
        childRecyclerAdapter.notifyDataSetChanged();
        FontSetter.resizeTextfont(holder.linearLayout,Density);
    }

    @Override
    public int getItemCount() {
        return parentArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ItemName;
        RecyclerView ChildRV;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView){
            super((itemView));
            linearLayout = itemView.findViewById(R.id.Curriculum_table_layout);
            ItemName = itemView.findViewById(R.id.itemname);
            ChildRV = itemView.findViewById(R.id.Curriculum_Child_RV);
        }
    }

}
