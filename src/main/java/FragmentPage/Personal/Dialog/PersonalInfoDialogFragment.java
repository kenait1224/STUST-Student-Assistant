package FragmentPage.Personal.Dialog;

import android.content.DialogInterface;
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
import FontRatio.Ratio_Detail_Dialog;
import FontSet.FontSetter;
import RecyclerViewController.Common.DetailInfoRecyclerAdapter;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import activity.R;

public class PersonalInfoDialogFragment extends DialogFragment implements DialogInterface.OnDismissListener {
    int Height;
    int Width;
    int WidthPixels;
    float Density;
    Ratio_Detail_Dialog ratio_detail_dialog;
    View view;
    ArrayList<DetailInfoContent> Target;

    public PersonalInfoDialogFragment(int heightPixels, int widthPixels, float density, ArrayList<DetailInfoContent> target) {
        Height = (int) (heightPixels * 0.8);
        Width = (int) (widthPixels * 0.9);
        WidthPixels = widthPixels;
        Density = density;
        Target = target;
        ratio_detail_dialog = new Ratio_Detail_Dialog(Width, Height);
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
        int union_padding = ratio_detail_dialog.getDetailDialogUnionPadding();

        LinearLayout linearLayout = view.findViewById(R.id.detail_dialog_info_layout);
        ImageButton Union_Button = view.findViewById(R.id.detail_dialog_info_union);
        TextView Title = view.findViewById(R.id.detail_dialog_info_text);
        Title.setText("用戶資訊");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.detail_dialog_info);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DetailInfoRecyclerAdapter InfoRecyclerAdapter = new DetailInfoRecyclerAdapter(Target, Density);
        recyclerView.setLayoutManager(linearLayoutManager);
        InfoRecyclerAdapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                TextView ContentTextView = (TextView) view.findViewById(R.id.detail_dialog_info_content);
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.detail_dialog_info_item_layout);
                String target = ContentTextView.getText().toString();
                FullNamePopupWindow fullNamePopupWindow = new FullNamePopupWindow(getContext(), target, (int) (WidthPixels * 0.7), Density);
                int offsetX = Math.abs(fullNamePopupWindow.getWidth() - layout.getWidth()) / 2;
                int offsetY = (int) (-(fullNamePopupWindow.getHeight() + layout.getHeight()) * 1.05);
                PopupWindowCompat.showAsDropDown(fullNamePopupWindow, layout, offsetX, offsetY, Gravity.START);
            }

            @Override
            public void onLongClickListener(View view) {
            }
        });
        recyclerView.setAdapter(InfoRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        InfoRecyclerAdapter.setNotCourseInfo(true);
        InfoRecyclerAdapter.notifyDataSetChanged();
        Union_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        /*調整字形、佈局大小*/

        Union_Button.setPadding(union_padding, union_padding, union_padding, union_padding);
        FontSetter.resizeTextfont(linearLayout,Density);
        Window window = getDialog().getWindow();
        window.setLayout(Width, Height);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.5f);
    }
}
