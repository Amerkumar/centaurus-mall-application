package app.com.thecentaurusmall.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.Query;

import app.com.thecentaurusmall.databinding.OfferFragmentBinding;
import app.com.thecentaurusmall.databinding.OfferItemBinding;
import app.com.thecentaurusmall.home.viewmodels.OfferViewModel;
import app.com.thecentaurusmall.model.Offer;

public class OfferFragment extends Fragment {

    private OfferFragmentBinding mOfferFragmentBinding;
    private OfferViewModel mViewModel;
    private FirestorePagingAdapter<Offer, OfferViewHolder> adapter;
    private View mRootView;

    public static OfferFragment newInstance() {
        return new OfferFragment();
    }


    public interface Listener {
        void onOfferItemClicked(Offer offerModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mOfferFragmentBinding = OfferFragmentBinding.inflate(inflater, container, false);
            mRootView = mOfferFragmentBinding.getRoot();
            mOfferFragmentBinding.getRoot();
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OfferViewModel.class);
//        mOffersAdapter = new OffersAdapter(getContext());

//        mViewModel.addDummyOffers();

        if (adapter == null){
            mOfferFragmentBinding.offerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            setUpAdapter();
        }

    }

    private void setUpAdapter() {
        Query baseQuery = mViewModel.getOffersQuery();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build();


//        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
//                .addSharedElement(, "header_image")
//                .build();
//        Navigation.findNavController(view).navigate(R.id.details,
//                null, // Bundle of args
//                null, // NavOptions
//                extras);

        Listener onOfferItemClickedListener = new Listener() {
            @Override
            public void onOfferItemClicked(Offer offerModel) {

//                HomeViewPagerFragmentDirections.ActionHomeViewPagerFragmentToOfferDetailFragment actionHomeViewPagerFragmentToOfferDetailFragment =
//                        HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToOfferDetailFragment();
//                Log.d("Offer Fragment", String.valueOf(offerModel.get_geoloc().getLat()));
                HomeViewPagerFragmentDirections.ActionHomeViewPagerFragmentToOfferDetailFragment actionHomeViewPagerFragmentToOfferDetailFragment =
                        HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToOfferDetailFragment(offerModel);
                Navigation.findNavController(mOfferFragmentBinding.getRoot()).navigate(actionHomeViewPagerFragmentToOfferDetailFragment);
            }
        };


        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.
        FirestorePagingOptions<Offer> options = new FirestorePagingOptions.Builder<Offer>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, Offer.class)
                .build();


        adapter =
                new FirestorePagingAdapter<Offer, OfferViewHolder>(options) {


                    @NonNull
                    @Override
                    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
                        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                        final OfferItemBinding binding = OfferItemBinding.inflate(layoutInflater, parent, false);
//        return new CategoryViewHolder(mContext,binding, mCategoryListener);
//        View view = layoutInflater.inflate(R.layout.offer_item, parent, false);
                        return new OfferViewHolder(getContext(), binding, onOfferItemClickedListener);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull OfferViewHolder holder,
                                                    int position,
                                                    @NonNull Offer model) {

                        holder.performBind(model);

                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull LoadingState state) {
                        switch (state) {
                            case LOADING_INITIAL:
                            case LOADING_MORE:
//                                mProgressBar.setVisibility(View.VISIBLE);
//                                showToast("Loading More");
                                break;
                            case LOADED:
//                                mProgressBar.setVisibility(View.GONE);
//                                showToast("Loaded");

                                break;
                            case FINISHED:
//                                mProgressBar.setVisibility(View.GONE);
//                                showToast("Reached end of data set.");
                                break;
                            case ERROR:
//                                showToast("An error occurred.");
                                retry();
                                break;
                        }
                    }
                };

        mOfferFragmentBinding.offerRecyclerView.setAdapter(adapter);

    }


    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

}
