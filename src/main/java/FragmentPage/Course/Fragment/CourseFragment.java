package FragmentPage.Course.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ApplicationDialog.LoadingDialog.LoadingDialogBar;
import FontRatio.Ratio_Course;
import FontRatio.Ratio_Loading_Dialog;
import RecyclerViewController.Course.CourseItemRecyclerAdapter;
import RecyclerViewController.Listener.RecyclerItemClickListener;
import Data.StaticData.UserCourseInfo;
import WebCrawler.Thread.CoursePage.CourseDetailCrawlerInterface;
import WebCrawler.Thread.CoursePage.CourseDetailCrawlerThread;
import activity.Course_Page;
import activity.R;


public class CourseFragment extends Fragment {


    private View view;
    float Density;
    int DisplayWidth;
    int DisplayHeight;
    int Source;
    Handler CourseFragmentHandler;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager ;
    RecyclerView.Adapter adapter;
    Ratio_Course ratio_course;
    Ratio_Loading_Dialog ratio_loading;

    public CourseFragment() {
//        Source = source;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Source = getArguments().getInt("Source",0);
        Density = getArguments().getFloat("Density");
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        ratio_course = new Ratio_Course(DisplayWidth,DisplayHeight);
        ratio_loading = new Ratio_Loading_Dialog(DisplayWidth,DisplayHeight);
        CourseFragmentHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.Course_RV);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CourseItemRecyclerAdapter(ratio_course.getCourseItemHeight(),Density,Source,UserCourseInfo.getCourseInfo(Source));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override public void onItemClick(final View view, final int position) {
                System.out.println(UserCourseInfo.getCourseInfo(Source).get(position).getCourseName());
                System.out.println(UserCourseInfo.getCourseInfo(Source).get(position).getCourseHref());
                try {
                    final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(getContext(),ratio_loading,Density);
                    loadingDialogBar.show();
                    CourseDetailCrawlerThread courseDetailCrawlerThread = new CourseDetailCrawlerThread(new CourseDetailCrawlerInterface() {
                        @Override
                        public void CrawlerLoading(final int text) {
                            CourseFragmentHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    TextView LoadingText = (TextView) loadingDialogBar.getWindow().findViewById(R.id.loading_dialog_text);
                                    LoadingText.setText(text);
                                }
                            });
                        }

                        @Override
                        public void CrawlerSuccessful() {
                            CourseFragmentHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialogBar.dismiss();
                                    Intent intent = new Intent(getActivity(), Course_Page.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("Target_Index",position);
                                    intent.putExtra("Source",Source);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                }
                            });
                        }

                        @Override
                        public void CrawlerFailed() {
                            loadingDialogBar.dismiss();
                        }
                    },Source,UserCourseInfo.getCourseInfo(Source).get(position).getCourseName(),UserCourseInfo.getCourseInfo(Source).get(position).getCourseHref());
                     //UserCourseInfo.getCourseInfo(Source).get(position).getCourseHref()
                     //"https://flipclass.stust.edu.tw/course/5071"
                    courseDetailCrawlerThread.start();
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.print("Course Detail Crawler Failed");
                }
// 這裡我也忘了在幹嘛 但好像沒用到
//                final LoadingDialogBar loadingDialogBar = new LoadingDialogBar(getContext());
//                final LoginFailedMessage failedMessage = new LoginFailedMessage(getContext());
//                loadingDialogBar.show();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
        recyclerView.setAdapter(null);
        recyclerView = null;
    }
}