package app.com.thecentaurusmall.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.OfferFragmentBinding;
import app.com.thecentaurusmall.home.viewmodels.OfferViewModel;
import app.com.thecentaurusmall.model.Offer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OfferFragment extends Fragment {

    private OfferFragmentBinding mOfferFragmentBinding;
    private OffersAdapter mOffersAdapter;
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
        mOffersAdapter = new OffersAdapter(getContext());
        mOfferFragmentBinding.offerRecyclerView.setAdapter(mOffersAdapter);

        mViewModel.getPagedListObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pagedList -> mOffersAdapter.submitList(pagedList));

        mOfferFragmentBinding.offerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mOfferFragmentBinding.offerRecyclerView.getContext(),
                        LinearLayoutManager.VERTICAL);
        mOfferFragmentBinding.offerRecyclerView.addItemDecoration(dividerItemDecoration);

    }

}
