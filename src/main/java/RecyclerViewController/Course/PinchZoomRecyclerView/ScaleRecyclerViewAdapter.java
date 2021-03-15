package RecyclerViewController.Course.PinchZoomRecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import FontRatio.Ratio_PPT_Viewer;
import activity.R;

public class ScaleRecyclerViewAdapter extends RecyclerView.Adapter<ScaleRecyclerViewAdapter.MyViewHolder> {
    ArrayList<String> pptImageList;
    Ratio_PPT_Viewer ratio_ppt_viewer;


    public ScaleRecyclerViewAdapter(ArrayList<String> pptImageList , Ratio_PPT_Viewer ratio_ppt_viewer) {
        this.ratio_ppt_viewer = ratio_ppt_viewer;
        this.pptImageList = pptImageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_detail_ppt_image,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            Uri uri = Uri.parse(pptImageList.get(position));
//            holder.draweeView.getLayoutParams().height = ratio_ppt_viewer.getPPTViewrSimpleDraweeViewHeight();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setOldController(holder.draweeView.getController())
                    .build();
//            holder.draweeView.setImageURI(uri);
            holder.draweeView.setController(controller);
        }catch (Exception e){
            System.out.println("ScaleRecyclerViewAdapter Error");
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pptImageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView draweeView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView)itemView.findViewById(R.id.course_detail_attachment_ppt);
        }
    }


}
