package app.com.thecentaurusmall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    }


    public void hideToolbar() {
        mAppBarLayout.setVisibility(View.GONE);
    }

    public void showToolbar() {
        mAppBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
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
                hideToolbar();
                showBottomNavigationView();
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
                hideToolbar();
                hideBottomNavigationView();
                break;

        }
    }
}
