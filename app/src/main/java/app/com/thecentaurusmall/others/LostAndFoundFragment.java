package app.com.thecentaurusmall.others;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.LostAndFoundFragmentBinding;
import app.com.thecentaurusmall.model.LatLng;
import app.com.thecentaurusmall.model.PointOfInterest;

public class LostAndFoundFragment extends Fragment {

    private static final int REQUEST_PHONE_CALL = 1;
    private LostAndFoundFragmentBinding mLostAndFroundFragmentBinding;

    public static LostAndFoundFragment newInstance() {
        return new LostAndFoundFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLostAndFroundFragmentBinding = LostAndFoundFragmentBinding.inflate(inflater, container, false);
        return mLostAndFroundFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mServicesFragmentBinding.collapsingToolbarLayout.setTitle("Services at Centaurus Mall");
        mLostAndFroundFragmentBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
//        mPoiDetailFragmentBinding.toolbarPoiDetail

//        mPoiDetailFragmentBinding.appBarLayout.
        mLostAndFroundFragmentBinding.toolbarServices.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));

        mLostAndFroundFragmentBinding.toolbarServices.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

//        mLostAndFroundFragmentBinding.toolbarServices.setTitle("Lost and Found");
        mLostAndFroundFragmentBinding.collapsingToolbarLayout.setTitle("Lost and Found");
//        mLostAndFroundFragmentBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = true;
//            int scrollRange = -1;
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + i == 0) {
//                    mLostAndFroundFragmentBinding.collapsingToolbarLayout.setTitle("Lost and Found");
//                    isShow = true;
//                } else if(isShow) {
//                    mLostAndFroundFragmentBinding.collapsingToolbarLayout.setTitle(" ");
//                    //careful there should a space between double quote otherwise it wont work
//                    isShow = false;
//                }
//            }
//        });

//        mLostAndFroundFragmentBinding.takeMeThereButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                PointOfInterest pointOfInterest = new PointOfInterest(1, "Reception", "Reception", "Services",
//                        new LatLng(33.7075699, 73.04953605), 0, "Services", "Serving for you",
//                        null);
//
//
//                LostAndFoundFragmentDirections.ActionLostAndFoundFragmentToIndoorMapFragment actionLostAndFoundFragmentToIndoorMapFragment =
//                        LostAndFoundFragmentDirections.actionLostAndFoundFragmentToIndoorMapFragment();z
//                actionLostAndFoundFragmentToIndoorMapFragment.setPointOfInterestObject(pointOfInterest);
//                Navigation.findNavController(v).navigate(actionLostAndFoundFragmentToIndoorMapFragment);
//
//            }
//        });

        mLostAndFroundFragmentBinding.callReceptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + "080078601"));
                startActivity(intent);
            }
        });
    }

}
