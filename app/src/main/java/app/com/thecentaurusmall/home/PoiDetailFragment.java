package app.com.thecentaurusmall.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.PoiDetailFragmentBinding;
import app.com.thecentaurusmall.model.PointOfInterest;

public class PoiDetailFragment extends Fragment {

    private PoiDetailViewModel mViewModel;
    private PoiDetailFragmentBinding mPoiDetailFragmentBinding;

    public static PoiDetailFragment newInstance() {
        return new PoiDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mPoiDetailFragmentBinding = PoiDetailFragmentBinding.inflate(inflater, container, false);
        return mPoiDetailFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PointOfInterest pointOfInterest = PoiDetailFragmentArgs.fromBundle(getArguments()).getPoiObject();

//        mPoiDetailFragmentBinding.toolbarPoiDetail.setTitle("Lorem Ipsum");
        mPoiDetailFragmentBinding.collapsingToolbarLayout.setTitle(pointOfInterest.getName());
        mPoiDetailFragmentBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));
//        mPoiDetailFragmentBinding.toolbarPoiDetail

//        mPoiDetailFragmentBinding.appBarLayout.
        mPoiDetailFragmentBinding.toolbarPoiDetail.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

        mPoiDetailFragmentBinding.toolbarPoiDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });


        mPoiDetailFragmentBinding.categoryTextViewPoi.setText(pointOfInterest.getCategory());
        mPoiDetailFragmentBinding.titleTextViewPoi.setText(pointOfInterest.getName());
        mPoiDetailFragmentBinding.contentTextViewPoi.setText(pointOfInterest.getDescription());

        mViewModel = ViewModelProviders.of(this).get(PoiDetailViewModel.class);
        // TODO: Use the ViewModel
    }

}
