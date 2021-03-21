package FragmentPage.Loading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import activity.R;

import FontRatio.Ratio_Loading_Dialog;
@Deprecated
public class LoadingFragment extends Fragment {
    View view;
    Ratio_Loading_Dialog ratio_loading;
    public LoadingFragment(Ratio_Loading_Dialog ratio_loading) {
        this.ratio_loading =ratio_loading;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loading,container,false);
        ImageView imageView = view.findViewById(R.id.fragment_loading_layout);
        imageView.getLayoutParams().height = ratio_loading.getLoadingDialogWidth();
        imageView.getLayoutParams().width = ratio_loading.getLoadingDialogWidth();
        return view;
    }
}
