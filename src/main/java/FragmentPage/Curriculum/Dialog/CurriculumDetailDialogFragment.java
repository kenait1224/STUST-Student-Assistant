package FragmentPage.Curriculum.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import RecyclerViewController.Curriculum.CurriculumTable.CurriculumDetailLayoutManager;
import RecyclerViewController.Curriculum.CurriculumTable.CurriculumTableParentRecyclerAdapter;
import activity.R;

public class CurriculumDetailDialogFragment extends DialogFragment  implements DialogInterface.OnDismissListener {

    private int Position;
    private DialogInterface.OnDismissListener mOnClickListener;
    private int width;
    private int height;
    private float density;
    private ArrayList<String> ItemArray = new ArrayList<>();
    private int[] TargetFontID = new int[1];
    RecyclerView recyclerView;

    int[] FontID = {R.drawable.background_xml_curriculum_table_title_1,
            R.drawable.background_xml_curriculum_table_title_2,
            R.drawable.background_xml_curriculum_table_title_3,
            R.drawable.background_xml_curriculum_table_title_4,
            R.drawable.background_xml_curriculum_table_title_5,
            R.drawable.background_xml_curriculum_table_title_6,
            R.drawable.background_xml_curriculum_table_title_7};

    public CurriculumDetailDialogFragment(int width, int height, int position,float density){
        this.width = width;
        this.height = height;
        this.density = density;
        this.Position = position;
        this.ItemArray.add(String.valueOf(position+1));
        this.TargetFontID[0]= FontID[position];
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener){
        this.mOnClickListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mOnClickListener != null)
            mOnClickListener.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //設定 Dialog Fragment 頁面的滾動清單

        View view = inflater.inflate(R.layout.dialog_curriculum, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.detail);
        RecyclerView.LayoutManager layoutManager = new CurriculumDetailLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new CurriculumTableParentRecyclerAdapter(ItemArray,getActivity(),TargetFontID,width,(int)((float)height*0.3),density,Position);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //調整 Dialog Fragment 位置

//        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
//        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
//        p.y = 5;
//        getDialog().getWindow().setAttributes(p);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //TODO:
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.CurriculumDetailDialog);
    }


}
