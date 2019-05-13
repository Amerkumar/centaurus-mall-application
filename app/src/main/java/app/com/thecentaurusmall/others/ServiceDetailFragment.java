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
import app.com.thecentaurusmall.databinding.ServiceDetailFragmentBinding;

public class ServiceDetailFragment extends Fragment {

    private ServiceDetailFragmentBinding mServicesDetailFragmentBinding;

    public static ServiceDetailFragment newInstance() {
        return new ServiceDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mServicesDetailFragmentBinding = ServiceDetailFragmentBinding.inflate(inflater, container, false);
        return mServicesDetailFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mServicesFragmentBinding.collapsingToolbarLayout.setTitle("Services at Centaurus Mall");
        mServicesDetailFragmentBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
//        mPoiDetailFragmentBinding.toolbarPoiDetail

//        mPoiDetailFragmentBinding.appBarLayout.
        mServicesDetailFragmentBinding.toolbarServices.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));

        mServicesDetailFragmentBinding.toolbarServices.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mServicesDetailFragmentBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    mServicesDetailFragmentBinding.collapsingToolbarLayout.setTitle("Services");
                    isShow = true;
                } else if(isShow) {
                    mServicesDetailFragmentBinding.collapsingToolbarLayout.setTitle(" ");
                    //careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

    }

}
