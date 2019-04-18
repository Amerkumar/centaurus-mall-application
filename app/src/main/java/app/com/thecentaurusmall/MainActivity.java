package app.com.thecentaurusmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
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

public class MainActivity extends AppCompatActivity {

    NavController mNavController;
    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView mBottomNavigationView;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBottomNavigationView = findViewById(R.id.bottom_navigation);
//        mDrawerLayout = findViewById(R.id.drawer_layout);
        mAppBarLayout = findViewById(R.id.appBar);
        mToolbar = findViewById(R.id.toolbar);
        mNavController = Navigation.findNavController(MainActivity.this,
                R.id.nav_host_fragment);

        Set<Integer> topLevelDestinationsSet = new HashSet<>(Arrays.asList(
                R.id.homeViewPagerFragment,
                R.id.indoorMapFragment
        ));
        mAppBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinationsSet)
                .setDrawerLayout(mDrawerLayout)
                .build();

        NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);
        NavigationUI.setupWithNavController(mToolbar, mNavController, mAppBarConfiguration);

    }


    public void hideToolbar() {
        mAppBarLayout.setVisibility(View.GONE);
    }

    public void showToolbar() {
        mAppBarLayout.setVisibility(View.VISIBLE);
    }
}
