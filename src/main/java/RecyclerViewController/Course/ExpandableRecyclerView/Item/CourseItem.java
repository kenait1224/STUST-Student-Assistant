package RecyclerViewController.Course.ExpandableRecyclerView.Item;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseItem implements Parcelable {
    public Boolean isTitle =false;
    public String Name;
    public String Link = "null";
    public String Type;
    public String Html_Content;

    public CourseItem(String name , String type ,String context, Boolean isTitle) {
        this.Name = name;
        this.Type = type;
        this.isTitle = isTitle;
        this.Html_Content = context;
    }

    public CourseItem(String name, String link, String type) {
        this.Name = name;
        this.Link = link;
        this.Type = type;
    }

    public CourseItem(String name) {
        this.Name = name;
        this.Type = "null";
    }

    protected CourseItem(Parcel in) {
        byte tmpIsTitle = in.readByte();
        isTitle = tmpIsTitle == 0 ? null : tmpIsTitle == 1;
        Name = in.readString();
        Link = in.readString();
        Type = in.readString();
        Html_Content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isTitle == null ? 0 : isTitle ? 1 : 2));
        dest.writeString(Name);
        dest.writeString(Link);
        dest.writeString(Type);
        dest.writeString(Html_Content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseItem> CREATOR = new Creator<CourseItem>() {
        @Override
        public CourseItem createFromParcel(Parcel in) {
            return new CourseItem(in);
        }

        @Override
        public CourseItem[] newArray(int size) {
            return new CourseItem[size];
        }
    };
}
