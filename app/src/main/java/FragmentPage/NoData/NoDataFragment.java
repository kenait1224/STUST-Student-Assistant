package FragmentPage.NoData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import activity.R;

public class NoDataFragment extends Fragment {
    View view;
    private float Density;
    private int DisplayWidth;
    private int DisplayHeight;
    private int TextSize = 18;
    private float Weight;


    public NoDataFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Density = getArguments().getFloat("Density", (float) 2.5);
        DisplayWidth = getArguments().getInt("DisplayWidth");
        DisplayHeight = getArguments().getInt("DisplayHeight");
        Weight = Density / ((Density >= 2.5) ? (float) Density : (float) (Density + 0.35));
        TextSize *= Weight;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nodata,container,false);
        ((TextView)view.findViewById(R.id.nodata_fragment_text)).setTextSize(TextSize);
        return view;
    }
}
