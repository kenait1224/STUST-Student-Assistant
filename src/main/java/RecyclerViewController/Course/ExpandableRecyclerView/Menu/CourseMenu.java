package RecyclerViewController.Course.ExpandableRecyclerView.Menu;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import RecyclerViewController.Course.ExpandableRecyclerView.Item.CourseItem;

public class CourseMenu extends ExpandableGroup<CourseItem> {

    public CourseMenu (String title, List<CourseItem> items) {
        super(title, items);
    }

}
