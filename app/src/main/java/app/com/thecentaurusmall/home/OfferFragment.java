package app.com.thecentaurusmall.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.home.viewmodels.OfferViewModel;

public class OfferFragment extends Fragment {

    private OfferViewModel mViewModel;

    public static OfferFragment newInstance() {
        return new OfferFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OfferViewModel.class);
        // TODO: Use the ViewModel
    }

}
