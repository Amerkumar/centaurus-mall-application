package app.com.thecentaurusmall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import app.com.thecentaurusmall.Utils.Utils;
import app.com.thecentaurusmall.map.FloorSelectionDialog;
import app.com.thecentaurusmall.map.IndoorMapFragment;

public class MainActivity extends AppCompatActivity implements
    NavController.OnDestinationChangedListener
{

    NavController mNavController;
    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView mBottomNavigationView;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private NavOptions navOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Utils.hideKeyboard(MainActivity.this);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)‌​;
//        Button crashButton = new Button(this);
//        crashButton.setText("Crash!");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Crashlytics.getInstance().crash(); // Force a crash
//            }
//        });

//        addContentView(crashButton, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
        navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build();

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mAppBarLayout = findViewById(R.id.appBar);
        mToolbar = findViewById(R.id.toolbar);
        mNavController = Navigation.findNavController(MainActivity.this,
                R.id.nav_host_fragment);

        Set<Integer> topLevelDestinationsSet = new HashSet<>(Arrays.asList(
                R.id.homeViewPagerFragment,
                R.id.indoorMapFragment,
                R.id.parkingFragment,
                R.id.shareOnboardingFragment
        ));
        mAppBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinationsSet)
                .build();

        NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);
        NavigationUI.setupWithNavController(mToolbar, mNavController, mAppBarConfiguration);

        mNavController.addOnDestinationChangedListener(this);

//        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                int id = menuItem.getItemId();
//                switch (id) {
//                    case R.id.home_nav_item:
//                       mNavController.navigate(R.id.homeViewPagerFragment, null,navOptions);
//                       break;
//                    case R.id.indoor_nav_item:
//                        mNavController.navigate(R.id.indoorMapFragment, null, navOptions);
//                        break;
//                    case R.id.parking_nav_item:
//                        mNavController.navigate(R.id.parkingFragment, null, navOptions);
//                        break;
//                    case R.id.others_nav_item:
//                        mNavController.navigate(R.id.othersOptionsFragment, null, navOptions);
//                        break;
//                }
//                return true;
//            }
//        });
    }


    public void hideToolbar() {
        mAppBarLayout.setVisibility(View.GONE);
    }

    public void showToolbar() {
        mAppBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        hideView(mBottomNavigationView);
    }


    public void showBottomNavigationView() {
        showView(mBottomNavigationView);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        switch (destination.getId()) {
            case R.id.homeViewPagerFragment:
                showToolbar();
                showBottomNavigationView();
                break;
            case R.id.directoryListFragment:
                hideToolbar();
                hideBottomNavigationView();
                break;
            case R.id.indoorMapFragment:
                showBottomNavigationView();
                hideToolbar();
                break;
            case R.id.pointOfInterestFragment:
                hideToolbar();
                hideBottomNavigationView();
                break;
            case R.id.parkingFragment:
                hideToolbar();
                showBottomNavigationView();
                break;
            case R.id.shareOnboardingFragment:
                hideToolbar();
                showBottomNavigationView();
                break;

            case R.id.othersOptionsFragment:
            case R.id.servicesFragment:
            case R.id.accountFragment:
            case R.id.settingsFragment:
            case R.id.offerDetailFragment:
            case R.id.eventDetailFragment:
            case R.id.serviceDetailFragment:
            case R.id.lostAndFoundFragment:
            case R.id.rideHailingFragment:
            case R.id.mallInfoFragment:
                hideToolbar();
                hideBottomNavigationView();
                break;

        }
    }


    private void hideView(View view) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });

    }

    private void showView (View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                    }
                });

    }
}
