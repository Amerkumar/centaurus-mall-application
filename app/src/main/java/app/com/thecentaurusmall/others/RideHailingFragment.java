package app.com.thecentaurusmall.others;

import android.content.Intent;
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
import com.uber.sdk.android.rides.RequestDeeplink;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.rides.client.SessionConfiguration;

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

        mRideHailingFragmentBinding.uberCallContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionConfiguration config = new SessionConfiguration.Builder()
                        .setClientId("ka6zVgvX53BFR0T1utr8ve7yYBXlABXg")
                        .setClientSecret("CJcpWqxvmgPzQ5peBaS3I8t7jzQHxpjC3K0vxsKN")
                        .setServerToken("PlE5fwfZQexwqoAjarENf9xYVwBph90BEaVdb-UT")
                        .build();

                RideParameters rideParams = new RideParameters.Builder()
                        .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                        .setPickupLocation(33.707572, 73.050272
                                , "The Centaurus Mall", "F8، The Centaurus, Jinnah Avenue, Islamabad")
                        .build();



                RequestDeeplink deeplink = new RequestDeeplink.Builder(getContext())
                        .setSessionConfiguration(config)
                        .setRideParameters(rideParams)
                        .build();

                deeplink.execute();
            }
        });

        mRideHailingFragmentBinding.careemCallContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.careem.acma");
                startActivity( launchIntent );

            }
        });
    }

}
