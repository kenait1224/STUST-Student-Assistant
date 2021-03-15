package RecyclerViewController.Common;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Data.CrawlerData.Common.Attachment;
import FontRatio.Ratio_Detail_Dialog;
import FontSet.FontSetter;
import WebCrawler.Downloader.AttachmentDownloader;
import activity.R;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerAdapter.MyViewHolder> {

    Ratio_Detail_Dialog ratioDetail;
    Activity myActivity;
    Context myContext;
    String STUST_URL = "https://flip.stust.edu.tw";
    private float Density;
    private ArrayList<Attachment> Attachments;
    int Source;

    public DetailRecyclerAdapter(int source,float density, Ratio_Detail_Dialog ratio_course, ArrayList<Attachment> attachments, Activity activity, Context context) {
        Source = source;
        Density = density;
        ratioDetail = ratio_course;
        Attachments = attachments;
        myContext = context;
        myActivity =  activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_detail_attachment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.attachment_icon.getLayoutParams().width = ratioDetail.getDetailDialogFileIconWidth();
        holder.attachment_icon.getLayoutParams().height = ratioDetail.getDetailDialogFileIconHeight();
        holder.attachment_button.getLayoutParams().width = ratioDetail.getDetailDialogFileButtonWidth();
        holder.attachment_button.getLayoutParams().height = ratioDetail.getDetailDialogFileButtonHeight();
        holder.attachment_icon.setImageResource(getFileIcon((Source == 0)?Attachments.get(position).Link:Attachments.get(position).Name.replace(" ","")));
        holder.attachment_button.setText(Attachments.get(position).Name);
        holder.attachment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(Attachments.get(position).Link);
                if (checkPermissions()) {
                    String Link = ((Attachments.get(position).Link.indexOf("http") == -1)?STUST_URL:"")+Attachments.get(position).Link;
                    Long MyDownloadID = null;
                    final Toast Start = Toast.makeText(myContext, myContext.getResources().getString(R.string.StartDownload) + Attachments.get(position).Name, Toast.LENGTH_LONG);
                        try {
                            Start.show();
                            AttachmentDownloader attachmentDownloader = new AttachmentDownloader(Source,Link,Attachments.get(position).Name);
                            DownloadManager manager = (DownloadManager) myActivity.getSystemService(myContext.DOWNLOAD_SERVICE);
                            MyDownloadID = manager.enqueue(attachmentDownloader);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(myContext.getResources().getString(R.string.DownloadFail));
                        }
                        final Long finalMyDownloadID = MyDownloadID;
                        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                if (ID.compareTo(finalMyDownloadID) == 0) {
                                    Start.cancel();
                                    Toast.makeText(context, Attachments.get(position).Name + myContext.getResources().getString(R.string.DownloadSuccessful), Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        myActivity.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                } else {
                    Toast.makeText(myContext,myContext.getResources().getString(R.string.PermissionsDenied),Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.layout.getLayoutParams().height = ratioDetail.getDetailDialogAttachmentContentHeight();
        FontSetter.resizeTextfont(holder.layout, Density);
    }


    @Override
    public int getItemCount() {
        return Attachments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView attachment_icon;
        Button attachment_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.course_detail_attachment_list_layout);
            attachment_icon = itemView.findViewById(R.id.course_detail_attachment_file_icon);
            attachment_button = itemView.findViewById(R.id.course_detail_attachment_file_button);
        }
    }

    private boolean checkPermissions(){
        return (EasyPermissions.hasPermissions(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(myContext, Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private int getFileIcon(String type) {
            type = type.substring(type.lastIndexOf(".") + 1, type.length()).toLowerCase();
        switch (type) {
            case "pdf":
                return R.drawable.ic_course_detail_menu_pdf;
            case "pptx":
            case "ppt":
                return R.drawable.ic_course_detail_menu_ppt;
            case "mp3":
                return R.drawable.ic_course_detail_menu_mp3;
            case "docx":
            case "doc":
            case "txt":
                return R.drawable.ic_course_detail_menu_doc;
            case "zip":
            case "rar":
            case "7z":
            case "tar":
                return R.drawable.ic_attachment;
            case "mp4":
            case "avi":
            case "wmv":
            case "fly":
            case "mov":
                return R.drawable.ic_course_detail_menu_embed_ver2;
            default:
                return R.drawable.ic_course_detail_menu_default;
        }
    }
}
