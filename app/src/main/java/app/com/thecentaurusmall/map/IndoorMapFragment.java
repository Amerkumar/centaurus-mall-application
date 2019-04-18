package app.com.thecentaurusmall.map;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.thecentaurusmall.MainActivity;
import app.com.thecentaurusmall.R;

public class IndoorMapFragment extends Fragment {

    private IndoorMapViewModel mViewModel;

    public static IndoorMapFragment newInstance() {
        return new IndoorMapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.indoor_map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(IndoorMapViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).hideToolbar();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).showToolbar();
    }
}
