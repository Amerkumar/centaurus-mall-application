package app.com.thecentaurusmall.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.Utils.Utils;
import app.com.thecentaurusmall.databinding.PoiDetailFragmentBinding;
import app.com.thecentaurusmall.model.PointOfInterest;

public class PoiDetailFragment extends Fragment {

    private static final String VENUE_ID = "SZfhUYddmjU6nJoX3PqC";
    private static String DEALS_FOLDER = "deals_thumbnails";
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


        String url = null;
        if (pointOfInterest.getUrl() != null) {
            String token = Utils.getTokenByDensity(pointOfInterest.getUrl(), Utils.getDensityName(getContext()));
            url = Utils.getUrlByToken(DEALS_FOLDER, VENUE_ID, pointOfInterest.getName(),
                    Utils.getDensityName(getContext()), token);

            Log.d("Poi Detail", url);
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error_placeholder)
                    .into(mPoiDetailFragmentBinding.image);
        }



        mPoiDetailFragmentBinding.categoryTextViewPoi.setText(pointOfInterest.getCategory());
        mPoiDetailFragmentBinding.titleTextViewPoi.setText(pointOfInterest.getName());
        mPoiDetailFragmentBinding.contentTextViewPoi.setText(pointOfInterest.getDescription());

        // TODO: Use the ViewModel
    }

}
