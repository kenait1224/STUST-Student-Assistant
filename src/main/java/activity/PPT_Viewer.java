package activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

import FontRatio.Ratio_PPT_Viewer;
import FontSet.FontSetter;
import RecyclerViewController.Course.PinchZoomRecyclerView.ScaleRecyclerView;
import RecyclerViewController.Course.PinchZoomRecyclerView.ScaleRecyclerViewAdapter;
import RecyclerViewController.Course.PinchZoomRecyclerView.ScaleRecyclerViewLayoutManager;
import Data.StaticData.UserCourseAttachment;

public class PPT_Viewer extends ComponentActivity {
    ScaleRecyclerView recyclerView;
    private boolean first_show = false;
    private int last_page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_ppt_viewer);
        Composing();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        this.finish();
    }

    private void Composing() {
        final int total = UserCourseAttachment.getAttachmentImageList().size();
        DisplayMetrics Display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Display);
        Ratio_PPT_Viewer Font_Std = new Ratio_PPT_Viewer(Display.widthPixels, Display.heightPixels);
        final Toast toast = Toast.makeText(this, "1/"+UserCourseAttachment.getAttachmentImageList().size(), Toast.LENGTH_LONG);
        recyclerView = (ScaleRecyclerView) findViewById(R.id.course_detail_attachment_ppt_recyclerview);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int first = linearLayoutManager.findFirstVisibleItemPosition();
                int last = linearLayoutManager.findLastVisibleItemPosition();
                int page;
                if (last == 0 && last - first == 1){
                    page = 1;
                }
                else if(last == total-1 && last - first == 1){
                    page = total;
                }
                else{
                    page = (int) Math.ceil((last+first)/2)+1;
                }
                if(last_page != page) {
                    toast.cancel();
                    toast.setText(page + "/" + total);
                    toast.show();
                    first_show = false;
                }
                else if (!first_show){
                    toast.setText(page + "/" + total);
                    toast.show();
                    first_show = true;
                }
                last_page = page;
            }
        });
        RecyclerView.LayoutManager layoutManager = new ScaleRecyclerViewLayoutManager(this,Display.heightPixels,Font_Std.getPPTViewrSimpleDraweeViewHeight());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = (RecyclerView.Adapter) new ScaleRecyclerViewAdapter(UserCourseAttachment.getAttachmentImageList(),Font_Std);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ppt_viewer_title_layout);
        TextView textView = (TextView) findViewById(R.id.ppt_viewer_title_text);
        textView.setText(getIntent().getStringExtra("title"));
        int Union_Padding = Font_Std.getPPTViewerUnionPadding();
        ImageButton union_button = (ImageButton)findViewById(R.id.ppt_viewer_title_union);
        union_button.setPadding(Union_Padding,Union_Padding,Union_Padding,Union_Padding);
        FontSetter.resizeTextfont(linearLayout,Display.density);
    }


    public void returnPage(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        System.out.println("PPT Viewer Destroy");
        super.onDestroy();
        System.gc();
        recyclerView.setAdapter(null);
    }
}
