package RecyclerViewController.Personal;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import Data.StaticData.UserConfig;
import Data.StaticData.UserPersonalInfo;
import FontSet.FontSetter;
import RecyclerViewController.Listener.OnRecyclerViewCheckListener;
import RecyclerViewController.Listener.OnRecyclerViewClickListener;
import activity.R;

public class PersonalContentRecyclerAdapter extends RecyclerView.Adapter<PersonalContentRecyclerAdapter.MyViewHolder> {

    float Density;
    int ItemHeight;
    OnRecyclerViewClickListener InfoListener,NotificationListener,StartPageListener,LogoutListener;
    OnRecyclerViewCheckListener SynchronizeListener,OfflineListener;

    public void setSynchronizeListener(OnRecyclerViewCheckListener synchronizeListener) {
        SynchronizeListener = synchronizeListener;
    }

    public void setOfflineListener(OnRecyclerViewCheckListener offlineListener) {
        OfflineListener = offlineListener;
    }

    public void setInfoListener(OnRecyclerViewClickListener infoListener) {
        InfoListener = infoListener;
    }

    public void setNotificationListener(OnRecyclerViewClickListener notificationListener) {
        NotificationListener = notificationListener;
    }

    public void setStartPageListener(OnRecyclerViewClickListener startPageListener) {
        StartPageListener = startPageListener;
    }

    public void setLogoutListener(OnRecyclerViewClickListener logoutListener) {
        LogoutListener = logoutListener;
    }

    public PersonalContentRecyclerAdapter(float density, int itemHeight) {
        Density = density;
        ItemHeight = itemHeight;
    }

    public void Initialization(MyViewHolder holder){
        holder.UserNotificationState.setText(UserConfig.getNotificationContent().get(UserConfig.getMyConfig().get("NotificationDate")));
        holder.UserStartPageState.setText(UserConfig.getStartPageContent().get(UserConfig.getMyConfig().get("StartPage")));
        holder.UserSynchronize.setChecked((UserConfig.getMyConfig().get("CurriculumSynchronize")==1)?true:false);
        holder.UserOffline.setChecked((UserConfig.getMyConfig().get("OfflineMode")==1)?true:false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_personal_table, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Initialization(holder);
        holder.itemView.getLayoutParams().height = ItemHeight;
        holder.UserPhoto.getHierarchy().setFailureImage(R.drawable.image_personal_default_image);
        holder.UserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InfoListener != null) {
                    InfoListener.onClickListener(v);
                }
            }
        });
        holder.UserNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NotificationListener != null) {
                    NotificationListener.onClickListener(v);
                }
            }
        });
        holder.UserStartPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StartPageListener != null) {
                    StartPageListener.onClickListener(v);
                }
            }
        });
        holder.UserLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LogoutListener != null) {
                    LogoutListener.onClickListener(v);
                }
            }
        });
        holder.UserSynchronize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserConfig.getMyConfig().put("CurriculumSynchronize",(isChecked)?1:0);
                SynchronizeListener.isCheck();
            }
        });
        holder.UserOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserConfig.getMyConfig().put("OfflineMode",(isChecked)?1:0);
                OfflineListener.isCheck();
            }
        });
        if(UserPersonalInfo.getMyUserInfo() != null) {
            try {
                holder.UserName.setText(UserPersonalInfo.getMyUserInfo().getName());
                System.out.println(UserPersonalInfo.getMyUserInfo().getPhotoHref());
                Uri uri = Uri.parse(UserPersonalInfo.getMyUserInfo().getPhotoHref());
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setOldController(holder.UserPhoto.getController())
                        .build();
                holder.UserPhoto.setController(controller);
            } catch (Exception e) {
                System.out.println("PersonalContentRecyclerAdapter Error");
                e.printStackTrace();
            }
        }

        FontSetter.resizeTextfont((ViewGroup) holder.itemView,Density);
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView UserPhoto;
        LinearLayout UserInfo,UserNotification,UserStartPage;
        SwitchCompat UserSynchronize,UserOffline;
        TextView UserName,UserInfoState,UserNotificationState,UserStartPageState,UserLogout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            UserInfo = itemView.findViewById(R.id.personal_user_info);
            UserName = itemView.findViewById(R.id.personal_user_name);
            UserPhoto = itemView.findViewById(R.id.personal_user_photo);
            UserLogout=itemView.findViewById(R.id.personal_user_logout);
            UserNotification = itemView.findViewById(R.id.personal_user_notification);
            UserStartPage = itemView.findViewById(R.id.personal_user_start_page);
            UserSynchronize = itemView.findViewById(R.id.personal_user_synchronize);
            UserOffline = itemView.findViewById(R.id.personal_user_offline);
            UserInfoState = itemView.findViewById(R.id.personal_user_info_state);
            UserNotificationState = itemView.findViewById(R.id.personal_user_notification_state);
            UserStartPageState= itemView.findViewById(R.id.personal_user_start_page_state);
        }
    }
}
