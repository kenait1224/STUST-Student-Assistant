package FragmentPage.Message.Dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
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

import ApplicationPopupWindow.FullNamePopupWindow;
import Data.CrawlerData.Common.Detail;
import FontRatio.Ratio_Detail_Dialog;
import FontSet.FontSetter;
import RecyclerViewController.Common.DetailRecyclerAdapter;
import activity.R;

public class MessageDetailDialogFragment extends DialogFragment {
    View view;
    Detail myDetail;
    ImageView attachment_image;
    Ratio_Detail_Dialog ratio_detail_dialog;
    float Density;
    int DisplayHeight;
    int DisplayWidth;
    int Source;
    int Height;
    int Width;

    public MessageDetailDialogFragment(int source, int width, int height, float density, Detail detail) {
        Height = (int) (height * 0.8);
        Width = (int) (width * 0.9);
        DisplayHeight = height;
        DisplayWidth = width;
        Source = source;
        ratio_detail_dialog = new Ratio_Detail_Dialog(Width, Height);
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
        int union_padding = ratio_detail_dialog.getDetailDialogUnionPadding();
        int AttachmentLayout_height = ratio_detail_dialog.getDetailDialogAttachmentHeight();
        int HtmlContent_padding = ratio_detail_dialog.getDetailDialogHtmlViewPadding();

        /*--------???????????? HTML View ??????--------*/
        WebView htmlTextView = (WebView) view.findViewById(R.id.detail_html_content);
        if (myDetail != null && myDetail.getHtml_Content() != null)
            htmlTextView.loadDataWithBaseURL(null, myDetail.getHtml_Content(), "text/html", "utf-8", null);
        htmlTextView.setPadding(HtmlContent_padding, HtmlContent_padding, HtmlContent_padding, (int) (ratio_detail_dialog.getDetailDialogAttachmentHeight() * 2));
        /*--------???????????? HTML View ??????--------*/

//        /*--------???????????? HTML View ??????--------*/
//        HtmlTextView htmlTextView = (HtmlTextView) view.findViewById(R.id.detail_html_content);
//        htmlTextView.setOnClickATagListener(new OnClickATagListener() {
//            @Override
//            public boolean onClick(View widget, String spannedText, @Nullable String href) {
//                try {
//                    PageIntent(href);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println(getResources().getString(R.string.AttachmentIntentFail));
//                }
//                return true;
//            }
//        });
//        htmlTextView.setHtml(myDetail.getHtml_Content());
//        htmlTextView.setPadding(HtmlContent_padding, HtmlContent_padding, HtmlContent_padding, (int) (ratio_detail_dialog.getDetailDialogAttachmentHeight() * 1.25));
//        /*--------???????????? HTML View ??????--------*/

        /*--------??????????????????--------*/
        ImageButton union = (ImageButton) view.findViewById(R.id.detail_dialog_union);
        LinearLayout DetailLayout = (LinearLayout) view.findViewById(R.id.course_detail_layout);
        DetailLayout.setClipToOutline(true);
        final LinearLayout DetailDialogTextTitle = (LinearLayout) view.findViewById(R.id.detail_dialog_text_title);
        LinearLayout DetailDialogTitleState = (LinearLayout) view.findViewById(R.id.detail_title_state_layout);
        TextView post_time = (TextView) view.findViewById(R.id.detail_dialog_post);
        TextView views = (TextView) view.findViewById(R.id.detail_dialog_views);
        final TextView name = (TextView) view.findViewById(R.id.detail_dialog_name);
        name.setText(myDetail.getName());
        DetailDialogTitleState.setVisibility(View.GONE);
        DetailDialogTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String target = name.getText().toString();
                FullNamePopupWindow fullNamePopupWindow = new FullNamePopupWindow(getContext(), target, (int) (DisplayWidth * 0.5), Density);
                int offsetX = (int) Math.abs(DetailDialogTextTitle.getWidth() - 0.5 * fullNamePopupWindow.getWidth() - 0.45 * DisplayWidth);
                PopupWindowCompat.showAsDropDown(fullNamePopupWindow, DetailDialogTextTitle, offsetX, 0, Gravity.START);
            }
        });
        /*--------??????????????????--------*/



        /*-------------?????????????????????-------------*/
        ConstraintLayout file_constraintLayout = (ConstraintLayout) view.findViewById(R.id.detail_file_layout);
        file_constraintLayout.setVisibility(View.GONE);
        /*-------------?????????????????????-------------*/


        /*-------------?????????????????????-------------*/
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.course_detail_sliding_layout);
        LinearLayout AttachmentLayout = (LinearLayout) view.findViewById(R.id.detail_attachment);
        AttachmentLayout.setClipToOutline(true);

        /*--------???????????? RecyclerView ??????--------*/
        Toast Attachment_toast = new Toast(getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.detail_attachment_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DetailRecyclerAdapter detailRecyclerAdapter = new DetailRecyclerAdapter(Source, Density, ratio_detail_dialog, myDetail.getAttachmentLink(), getActivity(), getContext());
        recyclerView.setAdapter(detailRecyclerAdapter);
        detailRecyclerAdapter.notifyDataSetChanged();
        /*--------???????????? RecyclerView ??????--------*/

        AttachmentLayout.setVisibility(View.VISIBLE);
        slidingUpPanelLayout.setScrollableViewHelper(new ScrollableViewHelper());
        slidingUpPanelLayout.setPanelHeight(AttachmentLayout_height);
        /*----------?????????????????????icon??????----------*/
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
        /*----------?????????????????????icon??????----------*/
        /*-------------?????????????????????-------------*/


        /*-------------??????????????????????????????-------------*/
        ConstraintLayout attachment_constraintLayout = (ConstraintLayout) view.findViewById(R.id.detail_attachment_layout);
        attachment_constraintLayout.getLayoutParams().height = AttachmentLayout_height;

        /*--------?????????(Padding)?????????????????????--------*/
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
        String title = "?????????????????????";
        Intent chooser = Intent.createChooser(intent, title);
        startActivity(chooser);
    }
}
