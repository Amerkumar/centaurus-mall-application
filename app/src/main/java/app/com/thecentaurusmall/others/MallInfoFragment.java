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
import app.com.thecentaurusmall.databinding.MallInfoFragmentBinding;

public class MallInfoFragment extends Fragment {

    private MallInfoFragmentBinding mMallInfoFragmentBinding;
    public static MallInfoFragment newInstance() {
        return new MallInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mMallInfoFragmentBinding = MallInfoFragmentBinding.inflate(inflater, container, false);
        return mMallInfoFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMallInfoFragmentBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        mMallInfoFragmentBinding.toolbarServices.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));


        mMallInfoFragmentBinding.toolbarServices.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mMallInfoFragmentBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    mMallInfoFragmentBinding.collapsingToolbarLayout.setTitle("Mall Information");
                    isShow = true;
                } else if(isShow) {
                    mMallInfoFragmentBinding.collapsingToolbarLayout.setTitle(" ");
                    //careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

    }

}
