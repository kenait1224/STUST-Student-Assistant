package FragmentPage.Course.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.PopupWindowCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ApplicationPopupWindow.FullNamePopupWindow;
import Data.CrawlerData.CourseDetailInfo.DetailInfoContent;
import FontRatio.Ratio_Course;
import FontSet.FontSetter;
import RecyclerViewController.Common.DetailInfoRecyclerAdapter;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import activity.R;

public class CourseInfoDialogFragment extends DialogFragment implements DialogInterface.OnDismissListener {

    int Height;
    int Width;
    int WidthPixels;
    float Density;
    Ratio_Course ratio_course;
    View view;
    ArrayList<DetailInfoContent> Target;

    public CourseInfoDialogFragment(int heightPixels, int widthPixels, float density, ArrayList<DetailInfoContent> target) {
        Height = (int) (heightPixels * 0.8);
        Width = (int) (widthPixels * 0.9);
        WidthPixels = widthPixels;
        Density = density;
        Target = target;
        ratio_course = new Ratio_Course(Width, Height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_info, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int union_padding = ratio_course.getCourseDialogUnionPadding();

        LinearLayout linearLayout = view.findViewById(R.id.detail_dialog_info_layout);
        ImageButton Union_Button = view.findViewById(R.id.detail_dialog_info_union);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.detail_dialog_info);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DetailInfoRecyclerAdapter courseInfoRecyclerAdapter = new DetailInfoRecyclerAdapter(Target, Density);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseInfoRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        courseInfoRecyclerAdapter.notifyDataSetChanged();
        courseInfoRecyclerAdapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                TextView TitleTextView = (TextView) view.findViewById(R.id.detail_dialog_info_title);
                TextView ContentTextView = (TextView) view.findViewById(R.id.detail_dialog_info_content);
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.detail_dialog_info_item_layout);
                String target = ContentTextView.getText().toString();
                String title = TitleTextView.getText().toString();
                if (title.compareTo("全部資訊...") == 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(target));
                    startActivity(intent);
                } else {
                    FullNamePopupWindow fullNamePopupWindow = new FullNamePopupWindow(getContext(), target, (int) (WidthPixels * 0.7), Density);
                    int offsetX = Math.abs(fullNamePopupWindow.getWidth() - layout.getWidth()) / 2;
                    int offsetY = (int) (-(fullNamePopupWindow.getHeight() + layout.getHeight()) * 1.05);
                    PopupWindowCompat.showAsDropDown(fullNamePopupWindow, layout, offsetX, offsetY, Gravity.START);
                }
            }

            @Override
            public void onLongClickListener(View view) {
            }
        });

        Union_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        /*調整字形、佈局大小*/

        Union_Button.setPadding(union_padding, union_padding, union_padding, union_padding);
        FontSetter.resizeTextfont(linearLayout, Density);
        Window window = getDialog().getWindow();
        window.setLayout(Width, Height);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.5f);
    }
}
