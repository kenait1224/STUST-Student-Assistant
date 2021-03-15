package FragmentPage.Course.Dialog;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

import java.io.File;

import ApplicationPopupWindow.FullNamePopupWindow;
import Data.CrawlerData.Common.Detail;
import Data.StaticData.UserCourseAttachment;
import FontRatio.Ratio_Detail_Dialog;
import FontSet.FontSetter;
import RecyclerViewController.Common.DetailRecyclerAdapter;
import WebCrawler.Downloader.AttachmentDownloader;
import activity.PPT_Viewer;
import activity.R;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Context.NOTIFICATION_SERVICE;


public class CourseDetailDialogFragment extends DialogFragment implements DialogInterface.OnDismissListener {

    View view;
    String Type;
    Detail myDetail;
    ImageView attachment_image;
    Ratio_Detail_Dialog ratio_detail_dialog;
    DisplayMetrics display;
    int Source;
    int Height;
    int Width;


    public CourseDetailDialogFragment(int source,DisplayMetrics displayMetrics, String type, Detail detail) {
        Source = source;
        Height = (int) (displayMetrics.heightPixels * 0.8);
        Width = (int) (displayMetrics.widthPixels * 0.9);
        ratio_detail_dialog = new Ratio_Detail_Dialog(Width, Height);
        display = displayMetrics;
        myDetail = detail;
        Type = type;
        System.out.println(Type);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_detail, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        int union_padding = ratio_detail_dialog.getDetailDialogUnionPadding();
        int AttachmentLayout_height = ratio_detail_dialog.getDetailDialogAttachmentHeight();
        int HtmlContent_padding = ratio_detail_dialog.getDetailDialogHtmlViewPadding();

        /*--------目標內容 HTML View 建置--------*/
        HtmlTextView htmlTextView = (HtmlTextView) view.findViewById(R.id.detail_html_content);
        htmlTextView.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {
                try {
                    PageIntent(href);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(getResources().getString(R.string.AttachmentIntentFail));
                }
                return true;
            }
        });
        if (myDetail.getHtml_Content()!= null)
            htmlTextView.setHtml(myDetail.getHtml_Content());
        htmlTextView.setPadding(HtmlContent_padding, HtmlContent_padding, HtmlContent_padding, (int) (ratio_detail_dialog.getDetailDialogAttachmentHeight()*1.25));
        /*--------目標內容 HTML View 建置--------*/

        /*--------標題狀態建置--------*/
        ImageButton union = (ImageButton) view.findViewById(R.id.detail_dialog_union);
        LinearLayout DetailLayout = (LinearLayout) view.findViewById(R.id.course_detail_layout);
        DetailLayout.setClipToOutline(true);
        final LinearLayout DetailDialogTextTitle = (LinearLayout) view.findViewById(R.id.detail_dialog_text_title);
        LinearLayout DetailDialogTitleState = (LinearLayout) view.findViewById(R.id.detail_title_state_layout);
        TextView post_time = (TextView) view.findViewById(R.id.detail_dialog_post);
        TextView views = (TextView) view.findViewById(R.id.detail_dialog_views);
        final TextView name = (TextView) view.findViewById(R.id.detail_dialog_name);
        name.setText(myDetail.getName());
        DetailDialogTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String target = name.getText().toString();
                FullNamePopupWindow fullNamePopupWindow = new FullNamePopupWindow(getContext(), target, (int) (display.widthPixels*0.5), display.density);
                int offsetX = (int) Math.abs(DetailDialogTextTitle.getWidth()-0.5* fullNamePopupWindow.getWidth() - 0.45*display.widthPixels);
                PopupWindowCompat.showAsDropDown(fullNamePopupWindow, DetailDialogTextTitle, offsetX, 0, Gravity.START);
            }
        });
        if (Type.compareTo("title") == 0 || Type.compareTo("exercise") == 0 || Type.compareTo("exerciseSuccessful") == 0 || Type.compareTo("title_content") == 0) {
            DetailDialogTitleState.setVisibility(View.GONE);
        } else {
            DetailDialogTitleState.setVisibility(View.VISIBLE);
            post_time.setText(myDetail.getPost_Time());
            views.setText(myDetail.getViews());
        }
        /*--------標題狀態建置--------*/



        /*-------------嵌入式檔案列表-------------*/
        ConstraintLayout file_constraintLayout = (ConstraintLayout) view.findViewById(R.id.detail_file_layout);
        if (checkType()) {
            ImageView File_icon = (ImageView) view.findViewById(R.id.detail_file_icon);
            final Button File_button = (Button) view.findViewById(R.id.detail_file_button);
            FontSetter.SetImageViewSize(File_icon, ratio_detail_dialog.getDetailDialogFileIconWidth(), ratio_detail_dialog.getDetailDialogFileIconHeight());
            FontSetter.SetButtonSize(File_button, ratio_detail_dialog.getDetailDialogFileButtonWidth(), ratio_detail_dialog.getDetailDialogFileButtonHeight());
            File_button.setText(getHintType(Type));
            File_icon.setImageResource(ChoseIcon(Type));
            file_constraintLayout.setVisibility(View.VISIBLE);
            File_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Type.compareTo("pdf") == 0 || Type.compareTo("mp3") == 0) {
                        Long MyDownloadID = null;
                        final Toast Start = Toast.makeText(getContext(), getResources().getString(R.string.StartDownload) + myDetail.getName(), Toast.LENGTH_LONG);
                        AttachmentDownloader attachmentDownloader = null;
                        if (checkPermissions()) {
                            try {
                                Start.show();
                                attachmentDownloader = new AttachmentDownloader(Source,myDetail.getHyperLink(), myDetail.getName());
                                DownloadManager manager = (DownloadManager) getActivity().getSystemService(getContext().DOWNLOAD_SERVICE);
                                MyDownloadID = manager.enqueue(attachmentDownloader);
                                // NotifyDownloading(myDetail.getName(), false,null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getResources().getString(R.string.DownloadFail));
                            }
                            final Long finalMyDownloadID = MyDownloadID;
                            final AttachmentDownloader finalAttachmentDownloader = attachmentDownloader;
                            final AttachmentDownloader finalAttachmentDownloader1 = attachmentDownloader;
                            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    Long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                    if (ID.compareTo(finalMyDownloadID) == 0) {
                                        Start.cancel();
                                        Toast.makeText(context, myDetail.getName() + getResources().getString(R.string.DownloadSuccessful), Toast.LENGTH_SHORT).show();
                                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), finalAttachmentDownloader.getFileName());
                                        // NotifyDownloading(myDetail.getName(), true, file);
                                    }
                                }
                            };
                            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.PermissionsDenied), Toast.LENGTH_LONG).show();
                        }
                    } else if (Type.compareTo("iframe") == 0 || Type.compareTo("hyperlink") == 0 || Type.compareTo("embed") == 0) {
                        try {
                            PageIntent(myDetail.getHyperLink());
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(getResources().getString(R.string.AttachmentIntentFail));
                        }
                    } else {
                        /*ppt*/
                        if(myDetail.getImageLinkList()!=null) {
                            UserCourseAttachment.setAttachmentImageList(myDetail.getImageLinkList());
                            Intent intent = new Intent(getContext(), PPT_Viewer.class);
                            intent.putExtra("title", myDetail.getName());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                }
            });


        } else
            file_constraintLayout.setVisibility(View.GONE);
        /*-------------嵌入式檔案列表-------------*/


        /*-------------上拉式布局設定-------------*/
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.course_detail_sliding_layout);
        LinearLayout AttachmentLayout = (LinearLayout) view.findViewById(R.id.detail_attachment);
        AttachmentLayout.setClipToOutline(true);
        if (Type.compareTo("title") == 0 || Type.compareTo("title_content") == 0) {
            AttachmentLayout.setVisibility(View.GONE);
        } else {
            /*--------附件列表 RecyclerView 建置--------*/
            Toast Attachment_toast = new Toast(getContext());
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.detail_attachment_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DetailRecyclerAdapter detailRecyclerAdapter = new DetailRecyclerAdapter(Source,display.density, ratio_detail_dialog, myDetail.getAttachmentLink(), getActivity(), getContext());
            recyclerView.setAdapter(detailRecyclerAdapter);
            detailRecyclerAdapter.notifyDataSetChanged();
            /*--------附件列表 RecyclerView 建置--------*/

            AttachmentLayout.setVisibility(View.VISIBLE);
            slidingUpPanelLayout.setScrollableViewHelper(new ScrollableViewHelper());
            slidingUpPanelLayout.setPanelHeight(AttachmentLayout_height);
            /*----------上拉式布局箭號icon動畫----------*/
            attachment_image = (ImageView) view.findViewById(R.id.detail_attachment_image);
            slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    attachment_image.setRotation(slideOffset * 180);
                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                }
            });
            /*----------上拉式布局箭號icon動畫----------*/
        }
        /*-------------上拉式布局設定-------------*/


        /*-------------上拉式布局展開列高度-------------*/
        ConstraintLayout attachment_constraintLayout = (ConstraintLayout) view.findViewById(R.id.detail_attachment_layout);
        attachment_constraintLayout.getLayoutParams().height = AttachmentLayout_height;

        /*--------返回鍵(Padding)、文字大小設定--------*/
        union.setPadding(union_padding, union_padding, (int) (0.8*union_padding), union_padding);
        FontSetter.resizeTextfont(slidingUpPanelLayout, display.density);

        union.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Window window = getDialog().getWindow();
        window.setLayout(Width, Height);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.5f);
    }


    private String getHintType(String type) {
        if (type.compareTo("pdf") == 0)
            return "下載檔案";
        if (type.compareTo("ppt") == 0)
            return "預覽檔案";
        return "開啟連結";
    }

    private boolean checkPermissions() {
        return (EasyPermissions.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private boolean checkType() {
        return (Type.compareTo("pdf") == 0 || Type.compareTo("ppt") == 0 || Type.compareTo("iframe") == 0 || Type.compareTo("hyperlink") == 0 || Type.compareTo("mp3") == 0 || Type.compareTo("embed") == 0);
    }

    private void PageIntent(String href) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(href));
        String title = "請選擇開啟工具";
        Intent chooser = Intent.createChooser(intent, title);
        startActivity(chooser);
    }

    @Deprecated
    private void NotifyDownloading(String title, boolean Successful ,File file) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,Uri.parse(DownloadManager.ACTION_VIEW_DOWNLOADS)).setType("file/*");
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        if (Build.VERSION.SDK_INT >= 26) {
            //当sdk版本大于26
            String id = "Downloading";
            String description = "0";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
//                     channel.enableVibration(true);//
            manager.createNotificationChannel(channel);
            notification = new Notification.Builder(getContext(), id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_inform_download)
                    .setContentTitle((Successful) ? "下載完成" : "下載中")
                    .setContentText(title)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        } else {
            //当sdk版本小于26
            notification = new NotificationCompat.Builder(getContext())
                    .setContentTitle((Successful) ? "下載完成" : "下載中")
                    .setContentText(title)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_inform_download)
                    .build();
        }
        if (Successful)
            manager.cancel(0);
        manager.notify((Successful) ? 1 : 0, notification);
    }

    public int ChoseIcon(String type) {
        switch (type) {
            case "title":
                return R.drawable.ic_course_detail_menu_title;
            case "doc":
                return R.drawable.ic_course_detail_menu_doc;
            case "iframe":
                return R.drawable.ic_course_detail_menu_iframe_ver2;
            case "pdf":
                return R.drawable.ic_course_detail_menu_pdf;
            case "ppt":
                return R.drawable.ic_course_detail_menu_ppt;
            case "mp3":
                return R.drawable.ic_course_detail_menu_mp3;
            case "hyperlink":
                return R.drawable.ic_course_detail_menu_hyperlink;
            case "exercise":
                return R.drawable.ic_course_detail_menu_exercise;
            case "exerciseSuccessful":
                return R.drawable.ic_course_detail_menu_exercise_successful;
            case "poll":
                return R.drawable.ic_course_detail_menu_poll;
            case "embed":
                return R.drawable.ic_course_detail_menu_embed_ver2;
            case "exam":
                return R.drawable.ic_course_detail_menu_exam;
            case "null":
                return R.drawable.ic_course_detail_menu_null;
            default:
                return R.drawable.ic_course_detail_menu_default;
        }
    }
}
