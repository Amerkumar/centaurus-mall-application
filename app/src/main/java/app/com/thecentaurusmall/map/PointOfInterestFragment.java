package app.com.thecentaurusmall.map;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.model.PointOfInterest;

public class PointOfInterestFragment extends Fragment {

    private PointOfInterestViewModel mViewModel;
    private TextView testTextView;

    public static PointOfInterestFragment newInstance() {
        return new PointOfInterestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.point_of_interest_fragment, container, false);
        testTextView = rootView.findViewById(R.id.test_text_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PointOfInterestViewModel.class);
        mViewModel.getAllPois().observe(this, new Observer<List<PointOfInterest>>() {
            @Override
            public void onChanged(List<PointOfInterest> pointOfInterests) {
                for (PointOfInterest pointOfInterest : pointOfInterests) {
                    testTextView.append(pointOfInterest.getName() + "\n");
                }
            }
        });
    }

}
