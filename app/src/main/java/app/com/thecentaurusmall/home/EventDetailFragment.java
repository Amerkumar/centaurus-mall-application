package app.com.thecentaurusmall.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.Utils.Utils;
import app.com.thecentaurusmall.databinding.EventsFragmentBinding;
import app.com.thecentaurusmall.databinding.FragmentEventDetailBinding;
import app.com.thecentaurusmall.databinding.FragmentOfferDetailBinding;
import app.com.thecentaurusmall.model.Event;
import app.com.thecentaurusmall.model.Offer;


public class EventDetailFragment extends Fragment {


    private static final String VENUE_ID = "Hc1uoaoWwUM1EABgh213";
    private static String EVENTS_FOLDER = "events_thumbnails";
    private FragmentEventDetailBinding mFragmentEventDetailBinding;

    public EventDetailFragment() {
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
        mFragmentEventDetailBinding = FragmentEventDetailBinding.inflate(inflater, container, false);
        return mFragmentEventDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Event event = EventDetailFragmentArgs.fromBundle(getArguments()).getEventObj();
//        Offer offer = OfferDetailFragmentArgs.fromBundle(getArguments()).getOfferObj();

//        mFragmentOfferDetailBinding.collapsingToolbarLayout.setTitle(offer.getName());
//        mFragmentOfferDetailBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));
//        mPoiDetailFragmentBinding.toolbarPoiDetail

//        mPoiDetailFragmentBinding.appBarLayout.
//        mFragmentOfferDetailBinding.toolbarOfferDetail.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));

//        mFragmentOfferDetailBinding.toolbarOfferDetail.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigateUp();
//            }
//        });


        String url = null;
        if (event.getUrl() != null) {
            String token = Utils.getTokenByDensity(event.getUrl(), Utils.getDensityName(getContext()));
            url = Utils.getUrlByToken(EVENTS_FOLDER, VENUE_ID, event.getName(),
                    Utils.getDensityName(getContext()), token);

            Log.d("Poi Detail", url);
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.error_placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(mFragmentEventDetailBinding.image);

        }


        mFragmentEventDetailBinding.categoryTextViewEvent.setText(event.getCategory());
        mFragmentEventDetailBinding.titleTextViewEvent.setText(event.getName());
        mFragmentEventDetailBinding.datesTextViewEvent.setText(Utils.timestampToSimpleDateFormat(event.getStart_date().toDate()) + " - " +
                Utils.timestampToSimpleDateFormat(event.getEnd_date().toDate()));
        mFragmentEventDetailBinding.contentTextViewEvent.setText(event.getDescription());

    }
}
