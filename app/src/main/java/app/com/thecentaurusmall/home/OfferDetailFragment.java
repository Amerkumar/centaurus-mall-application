package app.com.thecentaurusmall.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.Utils.Utils;
import app.com.thecentaurusmall.databinding.FragmentOfferDetailBinding;
import app.com.thecentaurusmall.model.Offer;


public class OfferDetailFragment extends Fragment {

    private static final String VENUE_ID = "SZfhUYddmjU6nJoX3PqC";
    private static String DEALS_FOLDER = "deals_thumbnails";

    private FragmentOfferDetailBinding mFragmentOfferDetailBinding;

    public OfferDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mFragmentOfferDetailBinding = FragmentOfferDetailBinding.inflate(inflater, container, false);
        return mFragmentOfferDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Offer offer = OfferDetailFragmentArgs.fromBundle(getArguments()).getOfferObj();


        mFragmentOfferDetailBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
//        mPoiDetailFragmentBinding.toolbarPoiDetail

        mFragmentOfferDetailBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    mFragmentOfferDetailBinding.collapsingToolbarLayout.setTitle(offer.getName());
                    isShow = true;
                } else if(isShow) {
                    mFragmentOfferDetailBinding.collapsingToolbarLayout.setTitle(" ");
                    //careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
//        mPoiDetailFragmentBinding.toolbarPoiDetail

//        mPoiDetailFragmentBinding.appBarLayout.
        mFragmentOfferDetailBinding.toolbarOfferDetail.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));

        mFragmentOfferDetailBinding.toolbarOfferDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });


        String url = null;
        if (offer.getUrl() != null) {
            String token = Utils.getTokenByDensity(offer.getUrl(), Utils.getDensityName(getContext()));
            url = Utils.getUrlByToken(DEALS_FOLDER, VENUE_ID, offer.getName(),
                    Utils.getDensityName(getContext()), token);

            Log.d("Poi Detail", url);
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.error_placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(mFragmentOfferDetailBinding.image);

        }

        mFragmentOfferDetailBinding.categoryTextViewOffer.setText(offer.getCategory());
        mFragmentOfferDetailBinding.titleTextViewOffer.setText(offer.getName());
        mFragmentOfferDetailBinding.datesTextViewOffer.setText(Utils.timestampToSimpleDateFormat(offer.getStart_date().toDate()) +
                    " - " + Utils.timestampToSimpleDateFormat(offer.getEnd_date().toDate()));
        mFragmentOfferDetailBinding.contentTextViewOffer.setText(offer.getDescription());

    }
}
