package FragmentPage.Personal.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Data.StaticData.UserConfig;
import Data.StaticData.UserCourseAttachment;
import Data.StaticData.UserCourseDetail;
import Data.StaticData.UserCourseInfo;
import Data.StaticData.UserCurriculumInfo;
import Data.StaticData.UserLoginCookies;
import Data.StaticData.UserLoginInfo;
import Data.StaticData.UserMessageInfo;
import Data.StaticData.UserPersonalInfo;
import FontRatio.Ratio_Personal;
import FragmentPage.Personal.Dialog.PersonalInfoDialogFragment;
import FragmentPage.Personal.Dialog.PersonalSettingDialogFragment;
import RecyclerViewController.Listener.OnRecyclerViewCheckListener;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import RecyclerViewController.Personal.PersonalContentRecyclerAdapter;
import activity.Main_Page;
import activity.R;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    Ratio_Personal ratioPersonal;
    private Handler PersonalFragmentHandler;
    private int DisplayWidth;
    private int DisplayHeight;
    private float Density;

    public PersonalFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PersonalFragmentHandler = new Handler();
        Density = getArguments().getFloat("Density");
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        ratioPersonal = new Ratio_Personal(DisplayWidth, DisplayHeight);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        recyclerView = view.findViewById(R.id.personal_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        PersonalContentRecyclerAdapter personalContentRecyclerAdapter = new PersonalContentRecyclerAdapter(Density,ratioPersonal.getPersonalPreferenceHeight());
        personalContentRecyclerAdapter.setInfoListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                if(UserPersonalInfo.getMyUserInfo() != null && UserPersonalInfo.getMyUserInfo().getUserDetailInfo().size() != 0) {
                    PersonalInfoDialogFragment personalInfoDialogFragment = new PersonalInfoDialogFragment(DisplayHeight,DisplayWidth,Density,UserPersonalInfo.getMyUserInfo().getUserDetailInfo());
                    personalInfoDialogFragment.show(getActivity().getSupportFragmentManager(), "Personal_Detail_Info");
                }
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });
        personalContentRecyclerAdapter.setNotificationListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                PersonalSettingDialogFragment personalSettingDialogFragment = new PersonalSettingDialogFragment(DisplayHeight,DisplayWidth,Density, UserConfig.getNotificationContent(),"通知提醒" , "NotificationDate" ,(TextView) view.findViewById(R.id.personal_user_notification_state));
                personalSettingDialogFragment.show(getActivity().getSupportFragmentManager(), "Personal_Notification_setting");
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });
        personalContentRecyclerAdapter.setStartPageListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                PersonalSettingDialogFragment personalSettingDialogFragment = new PersonalSettingDialogFragment(DisplayHeight,DisplayWidth,Density,UserConfig.getStartPageContent(),"初始畫面","StartPage",(TextView) view.findViewById(R.id.personal_user_start_page_state));
                personalSettingDialogFragment.show(getActivity().getSupportFragmentManager(), "Personal_StartPage_setting");
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });

        personalContentRecyclerAdapter.setSynchronizeListener(new OnRecyclerViewCheckListener() {
            @Override
            public void isCheck() {
                SaveUserConfig();
            }
        });

        personalContentRecyclerAdapter.setOfflineListener(new OnRecyclerViewCheckListener() {
            @Override
            public void isCheck() {
                SaveUserConfig();
            }
        });
        personalContentRecyclerAdapter.setLogoutListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClickListener(View view) {
                SharedPreferences User_Data = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences User_Personal = getActivity().getSharedPreferences("UserPersonalInfo", MODE_PRIVATE);
                SharedPreferences User_Permissions = getActivity().getSharedPreferences("PermissionsChecked", MODE_PRIVATE);
                SharedPreferences User_Curriculum = getActivity().getSharedPreferences("UserCurriculum", MODE_PRIVATE);
                User_Data.edit().clear().commit();
                User_Personal.edit().clear().commit();
                User_Permissions.edit().clear().commit();
                User_Curriculum.edit().clear().commit();
                DataClear();
                Intent intent = new Intent(getActivity(), Main_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }

            @Override
            public void onLongClickListener(View view) {

            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(personalContentRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        personalContentRecyclerAdapter.notifyDataSetChanged();
        return view;
    }

    public void SaveUserConfig(){
        SharedPreferences User_Config = getActivity().getSharedPreferences("UserConfig", MODE_PRIVATE);
        UserConfig.SaveLocalConfig(User_Config);
    }

    public void  DataClear(){
        UserCourseAttachment.Clear();
        UserCurriculumInfo.Clear();
        UserLoginInfo.Clear();
        UserLoginCookies.Clear();
        UserCourseDetail.Clear();
        UserCourseInfo.Clear();
        UserMessageInfo.Clear();
        UserPersonalInfo.Clear();
        UserLoginInfo.Clear();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
