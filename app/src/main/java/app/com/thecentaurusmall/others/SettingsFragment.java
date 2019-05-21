package app.com.thecentaurusmall.others;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.Utils.Utils;
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


        mSettingsFragmentBinding.switchDeals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    FirebaseMessaging.getInstance().subscribeToTopic("deals")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed);
                                    }
                                    showSnackbar(msg);
                                }
                            });
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("deals")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success_unsubscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed_unsubscribed);
                                    }
                                    showSnackbar(msg);
                                }
                            });

                }
            }
        });

        mSettingsFragmentBinding.switchEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    FirebaseMessaging.getInstance().subscribeToTopic("events")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed);
                                    }
                                    showSnackbar(msg);
                                }
                            });
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("events")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success_unsubscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed_unsubscribed);
                                    }
                                    showSnackbar(msg);
                                }
                            });

                }
            }
        });

        mSettingsFragmentBinding.switchPromos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    FirebaseMessaging.getInstance().subscribeToTopic("promos")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed);
                                    }
                                    showSnackbar(msg);
                                }
                            });
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("promos")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success_unsubscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed_unsubscribed);
                                    }
                                    showSnackbar(msg);
                                }
                            });

                }
            }
        });

        mSettingsFragmentBinding.switchLostAndFound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    FirebaseMessaging.getInstance().subscribeToTopic("lostAndFound")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed);
                                    }
                                    showSnackbar(msg);
                                }
                            });
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("lostAndFound")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.success_unsubscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.failed_unsubscribed);
                                    }
                                    showSnackbar(msg);
                                }
                            });

                }
            }
        });

        mSettingsFragmentBinding.privacyPolicyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mapin.page.link/centaurus-mall-legal"));
                startActivity(browserIntent);

            }
        });

        mSettingsFragmentBinding.aboutDeveloperTeamContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.aboutUsFragment);
            }
        });
    }


    private void showSnackbar(String msg){
        Snackbar.make(mSettingsFragmentBinding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }
}
