package app.com.thecentaurusmall.others;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.AppBarLayout;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.RideHailingFragmentBinding;

public class RideHailingFragment extends Fragment {


    private RideHailingFragmentBinding mRideHailingFragmentBinding;
    public static RideHailingFragment newInstance() {
        return new RideHailingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRideHailingFragmentBinding = RideHailingFragmentBinding.inflate(inflater, container, false);
        return mRideHailingFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRideHailingFragmentBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        mRideHailingFragmentBinding.toolbarServices.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));


        mRideHailingFragmentBinding.toolbarServices.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mRideHailingFragmentBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    mRideHailingFragmentBinding.collapsingToolbarLayout.setTitle("Ride Hailing Services");
                    isShow = true;
                } else if(isShow) {
                    mRideHailingFragmentBinding.collapsingToolbarLayout.setTitle(" ");
                    //careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

}
