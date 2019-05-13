package app.com.thecentaurusmall.others;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.ServicesFragmentBinding;

public class ServicesFragment extends Fragment {

    private ServicesViewModel mViewModel;
    private ServicesFragmentBinding mServicesFragmentBinding;

    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       mServicesFragmentBinding = ServicesFragmentBinding.inflate(inflater, container, false);
       return mServicesFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mServicesFragmentBinding.collapsingToolbarLayout.setTitle("Services at Centaurus Mall");
        mServicesFragmentBinding.collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));

        mServicesFragmentBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
//        mPoiDetailFragmentBinding.toolbarPoiDetail

//        mPoiDetailFragmentBinding.appBarLayout.
        mServicesFragmentBinding.toolbarServices.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));

        mServicesFragmentBinding.toolbarServices.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mServicesFragmentBinding.servicesContainer.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.serviceDetailFragment));
        mServicesFragmentBinding.lostAndFoundContainer.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.lostAndFoundFragment));
        mServicesFragmentBinding.rideHailingContainer.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.rideHailingFragment));
        mServicesFragmentBinding.mallInfoContainer.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.mallInfoFragment));

        mViewModel = ViewModelProviders.of(this).get(ServicesViewModel.class);
        // TODO: Use the ViewModel
    }

}
