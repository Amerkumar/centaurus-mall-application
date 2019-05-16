package app.com.thecentaurusmall.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.com.thecentaurusmall.category.CategoryAdapter;
import app.com.thecentaurusmall.databinding.PointOfInterestFragmentBinding;
import app.com.thecentaurusmall.model.Category;
import app.com.thecentaurusmall.model.PointOfInterest;
import app.com.thecentaurusmall.poi.PointOfInterestAdapter;

public class PointOfInterestFragment extends Fragment implements SortedListAdapter.Callback {



    private PointOfInterestViewModel mViewModel;
    private PointOfInterestAdapter mPointOfInterestAdapter;
    private PointOfInterestFragmentBinding mPointOfInterestFragmentBinding;
    private Animator mAnimator;
    private static final Comparator<Category> COMPARATOR_CATEGORY = new SortedListAdapter.ComparatorBuilder<Category>()
            .setOrderForModel(Category.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();

    private static final Comparator<PointOfInterest> COMPARATOR_POI = new SortedListAdapter.ComparatorBuilder<PointOfInterest>()
            .setOrderForModel(PointOfInterest.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();
    private List<PointOfInterest> mPointOfInterestModels;
    private SharedViewModel sharedViewModel;
    private CategoryAdapter mCategoryAdapter;
    private NavController mNavController;

    public static PointOfInterestFragment newInstance() {
        return new PointOfInterestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mPointOfInterestFragmentBinding = PointOfInterestFragmentBinding.inflate(inflater, container, false);

        //View rootView  = inflater.inflate(R.layout.point_of_interest_fragment, container, false);
//        testTextView = rootView.findViewById(R.id.test_text_view);
        return mPointOfInterestFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNavController = Navigation.findNavController(mPointOfInterestFragmentBinding.getRoot());
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        mCategoryAdapter = new CategoryAdapter(getContext(), COMPARATOR_CATEGORY, categoryModel -> {
//            Snackbar.make(mPointOfInterestFragmentBinding.getRoot(), categoryModel.getName(), Snackbar.LENGTH_SHORT).show();
            mPointOfInterestFragmentBinding.poiEditText.setText(categoryModel.getName());
        });

        mPointOfInterestAdapter = new PointOfInterestAdapter(getContext(), COMPARATOR_POI, poiModel -> {

            sharedViewModel.searchBarPoi(poiModel);
//            sharedViewModel.setSelectedFieldPoiCode(IndoorMapFragment.);
            mNavController.navigateUp();
//            Snackbar.make(mPointOfInterestFragmentBinding.getRoot(), poiModel.getName(), Snackbar.LENGTH_SHORT).show();
        });

        mPointOfInterestAdapter.addCallback(this);

        mPointOfInterestFragmentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedViewModel.setSelectedFieldPoiCode(SharedViewModel.TYPE_NONE);
                mNavController.navigateUp();
            }
        });

        mPointOfInterestFragmentBinding.categoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mPointOfInterestFragmentBinding.categoryRecyclerView.setAdapter(mCategoryAdapter);

        mPointOfInterestFragmentBinding.poiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPointOfInterestFragmentBinding.poiRecyclerView.setAdapter(mPointOfInterestAdapter);

        mViewModel = ViewModelProviders.of(this).get(PointOfInterestViewModel.class);
        mViewModel.getAllPois().observe(this, new Observer<List<PointOfInterest>>() {
            @Override
            public void onChanged(List<PointOfInterest> pointOfInterests) {
                mPointOfInterestModels = pointOfInterests;
//                Log.d(PointOfInterestFragment.class.getSimpleName(), String.valueOf(pointOfInterests.size()));
                mPointOfInterestAdapter.edit()
                        .replaceAll(pointOfInterests)
                        .commit();
            }
        });

        mViewModel.getAllCategoriesByQuickSearch().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
//                categories.get(0).getName();
                mCategoryAdapter.edit()
                        .replaceAll(categories)
                        .commit();
            }
        });

        mPointOfInterestFragmentBinding.poiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final List<PointOfInterest> filteredModelList = filter(mPointOfInterestModels, s.toString());
                mPointOfInterestAdapter.edit()
                        .replaceAll(filteredModelList)
                        .commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private static List<PointOfInterest> filter(List<PointOfInterest> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<PointOfInterest> filteredModelList = new ArrayList<>();
        for (PointOfInterest model : models) {
            final String name = model.getName().toLowerCase();
            final String category = model.getCategory().toLowerCase();
            final String rank = String.valueOf(model.getRank());
            if (name.contains(lowerCaseQuery) || category.contains(lowerCaseQuery) || rank.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    @Override
    public void onEditStarted() {
        if (mPointOfInterestFragmentBinding.poiEditProgressBar.getVisibility() != View.VISIBLE) {
            mPointOfInterestFragmentBinding.poiEditProgressBar.setVisibility(View.VISIBLE);
            mPointOfInterestFragmentBinding.poiEditProgressBar.setAlpha(0.0f);
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mPointOfInterestFragmentBinding.poiEditProgressBar, View.ALPHA, 1.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();

        mPointOfInterestFragmentBinding.poiRecyclerView.animate().alpha(0.5f);
    }

    @Override
    public void onEditFinished() {
        mPointOfInterestFragmentBinding.poiRecyclerView.scrollToPosition(0);
        mPointOfInterestFragmentBinding.poiRecyclerView.animate().alpha(1.0f);

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mPointOfInterestFragmentBinding.poiEditProgressBar, View.ALPHA, 0.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {

            private boolean mCanceled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!mCanceled) {
                    mPointOfInterestFragmentBinding.poiEditProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mAnimator.start();
    }

}
