package app.com.thecentaurusmall.others;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.AboutUsFragmentBinding;

public class AboutUsFragment extends Fragment {

    private AboutUsFragmentBinding mAboutUsFragmentBinding;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mAboutUsFragmentBinding = AboutUsFragmentBinding.inflate(inflater, container, false);
        return mAboutUsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mAboutUsFragmentBinding.backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigateUp();
//            }
//        });
        mAboutUsFragmentBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

    }

}
