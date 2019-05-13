package app.com.thecentaurusmall.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.potyvideo.slider.library.Animations.DescriptionAnimation;
import com.potyvideo.slider.library.SliderLayout;
import com.potyvideo.slider.library.SliderTypes.BaseSliderView;
import com.potyvideo.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.home.viewmodels.DirectoryViewModel;

public class DirectoryFragment extends Fragment implements
        BaseSliderView.OnSliderClickListener {

    private DirectoryViewModel mViewModel;
    private SliderLayout mFeaturedSlider;

    public static DirectoryFragment newInstance() {
        return new DirectoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.directory_fragment, container, false);

//        ImageView backButton = rootView.findViewById(R.id.back_button);
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigateUp();
//            }
//        });

        LinearLayout mShopCardViewContainer = rootView.findViewById(R.id.shop_card_view_container);
        mShopCardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HomeViewPagerFragmentDirections.ActionHomeViewPagerFragmentToDirectoryListFragment actionHomeViewPagerFragmentToDirectoryListFragment =
//                        a
                Log.d(DirectoryFragment.class.getSimpleName(), "Shop");
                HomeViewPagerFragmentDirections.ActionHomeViewPagerFragmentToDirectoryListFragment actionHomeViewPagerFragmentToDirectoryListFragment =
                        HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToDirectoryListFragment("Shop");
                Navigation.findNavController(v).navigate(actionHomeViewPagerFragmentToDirectoryListFragment);
            }
        });

        LinearLayout mDineCardViewContainer = rootView.findViewById(R.id.dine_card_view_container);
        mDineCardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeViewPagerFragmentDirections.ActionHomeViewPagerFragmentToDirectoryListFragment actionHomeViewPagerFragmentToDirectoryListFragment =
                        HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToDirectoryListFragment("Dine");
                Navigation.findNavController(v).navigate(actionHomeViewPagerFragmentToDirectoryListFragment);
            }
        });

        LinearLayout mEntertainmentCardViewContainer = rootView.findViewById(R.id.entertainment_card_view_container);
        mEntertainmentCardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeViewPagerFragmentDirections.ActionHomeViewPagerFragmentToDirectoryListFragment actionHomeViewPagerFragmentToDirectoryListFragment =
                        HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToDirectoryListFragment("Entertainment");
                Navigation.findNavController(v).navigate(actionHomeViewPagerFragmentToDirectoryListFragment);
            }
        });

        LinearLayout mServicesCardViewContainer = rootView.findViewById(R.id.services_card_view_conatiner);
        mServicesCardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeViewPagerFragmentDirections.ActionHomeViewPagerFragmentToDirectoryListFragment actionHomeViewPagerFragmentToDirectoryListFragment =
                        HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToDirectoryListFragment("Services");
                Navigation.findNavController(v).navigate(actionHomeViewPagerFragmentToDirectoryListFragment);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Shop and Dine Click Listeners

//        DirectoryFragmentDirection


        mViewModel = ViewModelProviders.of(this).get(DirectoryViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFeaturedSlider = (SliderLayout) view.findViewById(R.id.slider);

        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://assets1.ignimgs.com/2014/09/13/hannibal0117141280jpg-3a4be4_1280w.jpg");
        url_maps.put("Big Bang Theory", "https://media.comicbook.com/2017/04/big-bang-theory-cast-kaley-cuoco-jim-parsons-992959.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "https://ksassets.timeincuk.net/wp/uploads/sites/55/2017/08/2017_GameOfThrones_HBO_220817-920x584.jpg");

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .empty(android.R.color.darker_gray)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this)
            ;

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mFeaturedSlider.addSlider(textSliderView);
        }
        mFeaturedSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mFeaturedSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mFeaturedSlider.setCustomAnimation(new DescriptionAnimation());
        mFeaturedSlider.setDuration(5000);
        mFeaturedSlider.startAutoCycle();


    }

    @Override
    public void onStop() {
        mFeaturedSlider.stopAutoCycle();
        super.onStop();

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getContext(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        Log.d("Slider Demo", "Page Changed: " + position);
//    }

}


