package app.com.thecentaurusmall.home;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import app.com.thecentaurusmall.R;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    public HomeViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new DirectoryFragment();
        } else if (i == 1) {
            return new OfferFragment();
        } else {
            return new EventsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return context.getString(R.string.directory_fragment);
            case 1:
                return context.getString(R.string.offers_fragment);
            case 2:
                return context.getString(R.string.events_fragment);
                default:
                    return null;
        }
    }
}
