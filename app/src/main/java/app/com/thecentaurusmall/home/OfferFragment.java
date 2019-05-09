package app.com.thecentaurusmall.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
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

    public static OfferFragment newInstance() {
        return new OfferFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mOfferFragmentBinding = OfferFragmentBinding.inflate(inflater, container, false);
        return mOfferFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OfferViewModel.class);
//        mOffersAdapter = new OffersAdapter(getContext());

//        mViewModel.addDummyOffers();
        mOfferFragmentBinding.offerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setUpAdapter();

    }

    private void setUpAdapter() {
        Query baseQuery = mViewModel.getOffersQuery();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build();


        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.
        FirestorePagingOptions<Offer> options = new FirestorePagingOptions.Builder<Offer>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, Offer.class)
                .build();


        FirestorePagingAdapter<Offer, OfferViewHolder> adapter =
                new FirestorePagingAdapter<Offer, OfferViewHolder>(options) {
                    @NonNull
                    @Override
                    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
                        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                        final OfferItemBinding binding = OfferItemBinding.inflate(layoutInflater, parent, false);
//        return new CategoryViewHolder(mContext,binding, mCategoryListener);
//        View view = layoutInflater.inflate(R.layout.offer_item, parent, false);
                        return new OfferViewHolder(getContext(), binding);
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
                                break;
                            case LOADED:
//                                mProgressBar.setVisibility(View.GONE);
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

}
