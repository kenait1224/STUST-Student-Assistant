package FragmentPage.Personal.Dialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Data.StaticData.UserConfig;
import FontRatio.Ratio_Detail_Dialog;
import FontSet.FontSetter;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import RecyclerViewController.Personal.PersonalSettingRecyclerAdapter;
import activity.R;

import static android.content.Context.MODE_PRIVATE;

public class PersonalSettingDialogFragment extends DialogFragment implements DialogInterface.OnDismissListener {
    int Height,Width,WidthPixels;
    float Density;
    String TitleText ,PreferenceName ;
    TextView StateTextView;
    ArrayList<String> Content;
    Ratio_Detail_Dialog ratio_detail_dialog;
    View view;

    public PersonalSettingDialogFragment(int heightPixels, int widthPixels, float density, ArrayList<String> content, String titleText , String preferenceName,TextView stateTextView) {
        Height = (int) (heightPixels * 0.8);
        Width = (int) (widthPixels * 0.9);
        PreferenceName = preferenceName;
        StateTextView = stateTextView;
        TitleText = titleText;
        Content = content;
        WidthPixels = widthPixels;
        Density = density;
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
        Title.setText(TitleText);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.detail_dialog_info);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        PersonalSettingRecyclerAdapter settingRecyclerAdapter = new PersonalSettingRecyclerAdapter(Content, Density);
        settingRecyclerAdapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                int position = (Integer) view.getTag();
                StateTextView.setText(Content.get(position));
                UserConfig.getMyConfig().put(PreferenceName,position);
                SaveConfigToJson();
                dismiss();
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(settingRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        settingRecyclerAdapter.notifyDataSetChanged();
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

    public void SaveConfigToJson(){
        SharedPreferences User_Config = getActivity().getSharedPreferences("UserConfig", MODE_PRIVATE);
        UserConfig.SaveLocalConfig(User_Config);
    }
}
