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

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.SettingsFragmentBinding;

public class SettingsFragment extends Fragment {

    private SettingsFragmentBinding mSettingsFragmentBinding;
    private SettingsViewModel mViewModel;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mSettingsFragmentBinding = SettingsFragmentBinding.inflate(inflater, container, false);
        return mSettingsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mSettingsFragmentBinding.backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigateUp();
//            }
//        });


        mSettingsFragmentBinding.toolbar.setTitle("Settings");

        mSettingsFragmentBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mSettingsFragmentBinding.aboutDeveloperTeamContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.aboutUsFragment);
            }
        });
    }

}
