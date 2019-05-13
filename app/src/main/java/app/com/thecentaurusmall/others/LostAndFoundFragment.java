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
import app.com.thecentaurusmall.databinding.LostAndFoundFragmentBinding;

public class LostAndFoundFragment extends Fragment {

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

        mLostAndFroundFragmentBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    mLostAndFroundFragmentBinding.collapsingToolbarLayout.setTitle("Lost and Found");
                    isShow = true;
                } else if(isShow) {
                    mLostAndFroundFragmentBinding.collapsingToolbarLayout.setTitle(" ");
                    //careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

}
