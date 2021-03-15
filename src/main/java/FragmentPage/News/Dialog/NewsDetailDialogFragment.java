package FragmentPage.News.Dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.PopupWindowCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

import ApplicationPopupWindow.FullNamePopupWindow;
import FontRatio.Ratio_Detail_Dialog;
import FontSet.FontSetter;
import RecyclerViewController.Common.DetailRecyclerAdapter;
import Data.CrawlerData.Common.Detail;
import activity.R;

public class NewsDetailDialogFragment extends DialogFragment {

    View view;
    Detail myDetail;
    ImageView attachment_image;
    Ratio_Detail_Dialog ratio_course;
    float Density;
    int DisplayHeight;
    int DisplayWidth;
    int Source;
    int Height;
    int Width;


    public NewsDetailDialogFragment(int source , int width, int height, float density, Detail detail) {
            Source = source;
            Height = (int) (height * 0.8);
            Width = (int) (width * 0.9);
            DisplayHeight = height;
            DisplayWidth = width;
            ratio_course = new Ratio_Detail_Dialog(Width, Height);
            Density = density;
            myDetail = detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_detail, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        int union_padding = ratio_course.getDetailDialogUnionPadding();
        int AttachmentLayout_height = ratio_course.getDetailDialogAttachmentHeight();
        int HtmlContent_padding = ratio_course.getDetailDialogHtmlViewPadding();

        /*--------目標內容 HTML View 建置--------*/
        HtmlTextView htmlTextView = (HtmlTextView) view.findViewById(R.id.detail_html_content);
        htmlTextView.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {
                try {
                    PageIntent(href);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(getResources().getString(R.string.AttachmentIntentFail));
                }
                return true;
            }
        });
        htmlTextView.setHtml(myDetail.getHtml_Content());
        htmlTextView.setPadding(HtmlContent_padding, HtmlContent_padding, HtmlContent_padding, (int) (ratio_course.getDetailDialogAttachmentHeight() * 1.25));
        /*--------目標內容 HTML View 建置--------*/

        /*--------標題狀態建置--------*/
        ImageButton union = (ImageButton) view.findViewById(R.id.detail_dialog_union);
        LinearLayout DetailLayout = (LinearLayout) view.findViewById(R.id.course_detail_layout);
        DetailLayout.setClipToOutline(true);
        final LinearLayout DetailDialogTextTitle = (LinearLayout) view.findViewById(R.id.detail_dialog_text_title);
        TextView post_time = (TextView) view.findViewById(R.id.detail_dialog_post);
        TextView views = (TextView) view.findViewById(R.id.detail_dialog_views);
        final TextView name = (TextView) view.findViewById(R.id.detail_dialog_name);
        name.setText(myDetail.getName());
        post_time.setText(myDetail.getPost_Time());
        views.setVisibility(View.GONE);
        DetailDialogTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String target = name.getText().toString();
                FullNamePopupWindow fullNamePopupWindow = new FullNamePopupWindow(getContext(), target, (int) (DisplayWidth * 0.5), Density);
                int offsetX = (int) Math.abs(DetailDialogTextTitle.getWidth() - 0.5 * fullNamePopupWindow.getWidth() - 0.45 *DisplayWidth);
                PopupWindowCompat.showAsDropDown(fullNamePopupWindow, DetailDialogTextTitle, offsetX, 0, Gravity.START);
            }
        });
        /*--------標題狀態建置--------*/



        /*-------------嵌入式檔案列表-------------*/
        ConstraintLayout file_constraintLayout = (ConstraintLayout) view.findViewById(R.id.detail_file_layout);
        file_constraintLayout.setVisibility(View.GONE);
        /*-------------嵌入式檔案列表-------------*/


        /*-------------上拉式布局設定-------------*/
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.course_detail_sliding_layout);
        LinearLayout AttachmentLayout = (LinearLayout) view.findViewById(R.id.detail_attachment);
        AttachmentLayout.setClipToOutline(true);

        /*--------附件列表 RecyclerView 建置--------*/
        Toast Attachment_toast = new Toast(getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.detail_attachment_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DetailRecyclerAdapter detailRecyclerAdapter = new DetailRecyclerAdapter(Source,Density, ratio_course, myDetail.getAttachmentLink(), getActivity(), getContext());
        recyclerView.setAdapter(detailRecyclerAdapter);
        detailRecyclerAdapter.notifyDataSetChanged();
        /*--------附件列表 RecyclerView 建置--------*/

        AttachmentLayout.setVisibility(View.VISIBLE);
        slidingUpPanelLayout.setScrollableViewHelper(new ScrollableViewHelper());
        slidingUpPanelLayout.setPanelHeight(AttachmentLayout_height);
        /*----------上拉式布局箭號icon動畫----------*/
        attachment_image = (ImageView) view.findViewById(R.id.detail_attachment_image);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                attachment_image.setRotation(slideOffset * 180);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
        /*----------上拉式布局箭號icon動畫----------*/
        /*-------------上拉式布局設定-------------*/


        /*-------------上拉式布局展開列高度-------------*/
        ConstraintLayout attachment_constraintLayout = (ConstraintLayout) view.findViewById(R.id.detail_attachment_layout);
        attachment_constraintLayout.getLayoutParams().height = AttachmentLayout_height;

        /*--------返回鍵(Padding)、文字大小設定--------*/
        union.setPadding(union_padding, union_padding, (int) (0.8 * union_padding), union_padding);
        FontSetter.resizeTextfont(slidingUpPanelLayout, Density);

        union.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Window window = getDialog().getWindow();
        window.setLayout(Width, Height);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.5f);
    }

    private void PageIntent(String href) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(href));
        String title = "請選擇開啟工具";
        Intent chooser = Intent.createChooser(intent, title);
        startActivity(chooser);
    }
}
