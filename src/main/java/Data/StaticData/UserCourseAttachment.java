package Data.StaticData;

import java.util.ArrayList;

public class UserCourseAttachment {

    private static ArrayList<String> AttachmentImageList = new ArrayList<>();

    public static void Clear(){ AttachmentImageList.clear();}

    public static ArrayList<String> getAttachmentImageList() {
        return AttachmentImageList;
    }

    public static void setAttachmentImageList(ArrayList<String> attachmentImageList) {
        AttachmentImageList = attachmentImageList;
    }
}
