package FragmentPage.Curriculum.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import FontRatio.Ratio_Curriculum;
import FragmentPage.Curriculum.Dialog.CurriculumDetailDialogFragment;
import RecyclerViewController.Curriculum.CurriculumSpacesDecoration;
import RecyclerViewController.Curriculum.CurriculumTable.CurriculumLayoutManager;
import RecyclerViewController.Curriculum.CurriculumTable.CurriculumTableParentRecyclerAdapter;
import RecyclerViewController.Listener.RecyclerItemClickListener;
import activity.R;

public class CurriculumFragment extends Fragment {

    private View view;
    private boolean isTouch;
    private int DisplayWidth;
    private int DisplayHeight;
    private float Density;
    private Ratio_Curriculum ratioTable;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> itemsArrayList = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));


    int[] FontID = {R.drawable.background_xml_curriculum_table_title_1,
            R.drawable.background_xml_curriculum_table_title_2,
            R.drawable.background_xml_curriculum_table_title_3,
            R.drawable.background_xml_curriculum_table_title_4,
            R.drawable.background_xml_curriculum_table_title_5,
            R.drawable.background_xml_curriculum_table_title_6,
            R.drawable.background_xml_curriculum_table_title_7};



    public CurriculumFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Density = getArguments().getFloat("Density");
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        ratioTable = new Ratio_Curriculum(DisplayWidth,DisplayHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        // 建立雙向 Scrolling View

        view = inflater.inflate(R.layout.fragment_curriculum, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.Curriculum_Parent_RV);
        layoutManager = new CurriculumLayoutManager(getActivity(), DisplayWidth,ratioTable.getCurriculumWidth());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CurriculumTableParentRecyclerAdapter(itemsArrayList,getActivity(),FontID,ratioTable.getCurriculumWidth(),ratioTable.getCurriculumHeight(),Density);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CurriculumSpacesDecoration((int)(5*Density)));
        adapter.notifyDataSetChanged();

        //自動調整目標焦點(滑動監聽)
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        // 抓取目前時間，將預設焦點定為該日期
        Calendar cal = Calendar.getInstance();
        int Day = cal.get(Calendar.DAY_OF_WEEK);
        recyclerView.smoothScrollToPosition((Day == 1)?7:Day-2);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //自動調整目標焦點(點擊監聽)，點擊目標與焦點一致開啟DialogFragment
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override public void onItemClick(View view, final int position) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int first = linearLayoutManager.findFirstVisibleItemPosition();
                int last = linearLayoutManager.findLastVisibleItemPosition();
                int centerPositionDiffer = (last - first) / 2;
                int centerChildViewPosition = first + centerPositionDiffer;
                if (centerChildViewPosition == position){
                    final View v = recyclerView.getLayoutManager().findViewByPosition(centerChildViewPosition);
                    v.animate().alpha(0).setDuration(400);
                    DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            v.animate().alpha(1).setDuration(200);
                        }
                    };
                    CurriculumDetailDialogFragment dialogFragment = new CurriculumDetailDialogFragment((int)(2.4*v.getWidth()),(int)(0.95 *v.getHeight()),position, (float) Density);
                    dialogFragment.setOnDismissListener(listener);
                    dialogFragment.show(getFragmentManager(),"dialog test");

                }
                else{
                    recyclerView.smoothScrollToPosition(position);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    @Deprecated
    private void autoMiddleFocus(){
        //自動調整目標焦點(滑動監聽)
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int first = linearLayoutManager.findFirstVisibleItemPosition();
                    int last = linearLayoutManager.findLastVisibleItemPosition();
                    if (isTouch) {
                        isTouch = false;
                        int centerPositionDiffer = (last - first) / 2;
                        int centerChildViewPosition = first + centerPositionDiffer;
                        System.out.println("middle item : " + centerChildViewPosition);
                        recyclerView.smoothScrollToPosition(centerChildViewPosition);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouch = true;
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
        recyclerView.setAdapter(null);
        recyclerView = null;
    }
}