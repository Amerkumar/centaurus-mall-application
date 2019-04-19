package app.com.thecentaurusmall.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.potyvideo.slider.library.Animations.DescriptionAnimation;
import com.potyvideo.slider.library.SliderLayout;
import com.potyvideo.slider.library.SliderTypes.BaseSliderView;
import com.potyvideo.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import app.com.thecentaurusmall.MainActivity;
import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.home.viewmodels.DirectoryViewModel;

public class DirectoryFragment extends Fragment implements
        BaseSliderView.OnSliderClickListener
{

    private DirectoryViewModel mViewModel;
    private SliderLayout mFeaturedSlider;

    public static DirectoryFragment newInstance() {
        return new DirectoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.directory_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DirectoryViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFeaturedSlider = (SliderLayout) view.findViewById(R.id.slider);



        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
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


