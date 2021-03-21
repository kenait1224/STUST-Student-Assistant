package FragmentPage.Course.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.PopupWindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import FontRatio.Ratio_Course;
import FontRatio.Ratio_Loading_Dialog;
import FragmentPage.Course.Dialog.CourseDetailDialogFragment;
import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItem;
import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItemAdapter;
import ApplicationPopupWindow.FullNamePopupWindow;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import Data.CrawlerData.Common.Detail;
import Data.StaticData.UserCourseDetail;
import WebCrawler.Thread.CoursePage.CourseDialogCrawlerInterface;
import WebCrawler.Thread.CoursePage.CourseDialogCrawlerThread;
import activity.R;

public class CourseListFragment extends Fragment {
    private View view;
    RecyclerView recyclerView;
    DisplayMetrics display;
    Ratio_Course ratio_table;
    Handler CourseDetailHandler;
    String CourseName;
    int Source;

    public CourseListFragment(DisplayMetrics display, Ratio_Course ratio_table, String courseName, int source) {
        this.display = display;
        this.ratio_table = ratio_table;
        CourseName = courseName;
        Source = source;
        CourseDetailHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_list_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.course_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CourseItemAdapter adapter = new CourseItemAdapter(UserCourseDetail.getCourseDetail(Source).get(CourseName), display, ratio_table);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                String target = (view.findViewById(R.id.course_detail_content)).getTag().toString();
                int Group_idx = Integer.parseInt(target.split(",")[0]);
                int Child_idx = Integer.parseInt(target.split(",")[1]);
                final CourseItem courseItem = UserCourseDetail.getCourseDetail(Source).get(CourseName).get(Group_idx).getItems().get(Child_idx);
                if (courseItem.Type != "null" && courseItem.Type != "title" ) {
                    try {
                        final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(getContext(), new Ratio_Loading_Dialog(display.widthPixels, display.heightPixels), display.density);
                        loadingDialogBar.show();
                        CourseDialogCrawlerThread courseDialogCrawlerThread = new CourseDialogCrawlerThread(Source,new CourseDialogCrawlerInterface() {
                            @Override
                            public void CrawlerLoading(final int text) {
                                CourseDetailHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                                        LoadingText.setText(text);
                                    }
                                });
                            }

                            @Override
                            public void CrawlerSuccessful(final Detail detail) {
                                CourseDetailHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialogBar.dismiss();
                                        CourseDetailDialogFragment courseDetailDialogFragment = new CourseDetailDialogFragment(Source,display, courseItem.Type, detail);
                                        courseDetailDialogFragment.show(getFragmentManager(), "test");
                                    }
                                });
                            }

                            @Override
                            public void CrawlerFailed() {
                                loadingDialogBar.dismiss();
                            }
                        }, courseItem);
                        courseDialogCrawlerThread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.print("Course Detail Dialog Crawler Failed");
                    }
                }
            }

            @Override
            public void onLongClickListener(View view) {
                TextView textView = (TextView) view.findViewById(R.id.course_detail_content);
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.course_detail_linearlayout);
                String target = textView.getText().toString();
                FullNamePopupWindow fullNamePopupWindow = new FullNamePopupWindow(getContext(), target, (int) (display.widthPixels * 0.7), display.density);
                int offsetX = Math.abs(fullNamePopupWindow.getWidth() - layout.getWidth()) / 2;
                int offsetY = (int) (-(fullNamePopupWindow.getHeight() + layout.getHeight()) * 1.05);
                PopupWindowCompat.showAsDropDown(fullNamePopupWindow, layout, offsetX, offsetY, Gravity.START);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        System.out.println("detail destory");
        super.onDestroy();

    }
}
