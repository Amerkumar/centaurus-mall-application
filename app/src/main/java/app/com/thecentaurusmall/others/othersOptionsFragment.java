package app.com.thecentaurusmall.others;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.OthersOptionsFragmentBinding;

public class othersOptionsFragment extends Fragment {

    private OthersOptionsFragmentBinding mOthersOptionsFragmentBinding;

    private OthersOptionsViewModel mViewModel;


    public static othersOptionsFragment newInstance() {
        return new othersOptionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mOthersOptionsFragmentBinding = OthersOptionsFragmentBinding.inflate(inflater, container, false);
        return mOthersOptionsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mOthersOptionsFragmentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mOthersOptionsFragmentBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(R.id.homeViewPagerFragment, false);
            }
        });
//        mOthersOptionsFragmentBinding.home.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.homeViewPagerFragment));
        mOthersOptionsFragmentBinding.services.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.servicesFragment));
        mOthersOptionsFragmentBinding.account.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.accountFragment));
        mOthersOptionsFragmentBinding.settings.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.settingsFragment));
//        remove account item if the users is not logged in.

        mViewModel = ViewModelProviders.of(this).get(OthersOptionsViewModel.class);
        // TODO: Use the ViewModel
    }

}
