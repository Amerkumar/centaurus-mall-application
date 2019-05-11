package app.com.thecentaurusmall.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.category.CategoryAdapter;
import app.com.thecentaurusmall.databinding.DirectoryListFragmentBinding;
import app.com.thecentaurusmall.model.Category;
import app.com.thecentaurusmall.model.PointOfInterest;
import app.com.thecentaurusmall.poi.PointOfInterestAdapter;

public class DirectoryListFragment extends Fragment implements SortedListAdapter.Callback {

    private DirectoryListViewModel mViewModel;

    private DirectoryListFragmentBinding mDirectoryListFragmentBinding;


    private static final Comparator<Category> COMPARATOR_CATEGORY = new SortedListAdapter.ComparatorBuilder<Category>()
            .setOrderForModel(Category.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();

    private static final Comparator<PointOfInterest> COMPARATOR_POI = new SortedListAdapter.ComparatorBuilder<PointOfInterest>()
            .setOrderForModel(PointOfInterest.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();
    private PointOfInterestAdapter mPointOfInterestAdapter;
    private Animator mAnimator;
    private List<PointOfInterest> mPointOfInterestModels;
    private CategoryAdapter mCategoryAdapter;


    public static DirectoryListFragment newInstance() {
        return new DirectoryListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mDirectoryListFragmentBinding = DirectoryListFragmentBinding.inflate(inflater, container, false);
        mDirectoryListFragmentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        return mDirectoryListFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DirectoryListViewModel.class);


        mCategoryAdapter = new CategoryAdapter(getContext(), COMPARATOR_CATEGORY, categoryModel -> {
            Snackbar.make(mDirectoryListFragmentBinding.getRoot(), categoryModel.getName(), Snackbar.LENGTH_SHORT).show();
            mDirectoryListFragmentBinding.poiEditText.setText(categoryModel.getName());
        });

        mDirectoryListFragmentBinding.poiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPointOfInterestModels != null) {
                    final List<PointOfInterest> filteredModelList = filter(mPointOfInterestModels, s.toString());
                    mPointOfInterestAdapter.edit()
                            .replaceAll(filteredModelList)
                            .commit();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPointOfInterestAdapter = new PointOfInterestAdapter(getContext(), COMPARATOR_POI, poiModel -> {
//            Snackbar.make(mDirectoryListFragmentBinding.getRoot(), poiModel.getName(), Snackbar.LENGTH_SHORT).show();
            mDirectoryListFragmentBinding.poiEditText.setText("");
            DirectoryListFragmentDirections.ActionDirectoryListFragmentToPoiDetailFragment actionDirectoryListFragmentToPoiDetailFragment =
                    DirectoryListFragmentDirections.actionDirectoryListFragmentToPoiDetailFragment(poiModel);
            Navigation.findNavController(mDirectoryListFragmentBinding.getRoot()).navigate(actionDirectoryListFragmentToPoiDetailFragment);
        });

        mPointOfInterestAdapter.addCallback(this);


        mDirectoryListFragmentBinding.categoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mDirectoryListFragmentBinding.categoryRecyclerView.setAdapter(mCategoryAdapter);

        mDirectoryListFragmentBinding.poiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDirectoryListFragmentBinding.poiRecyclerView.setAdapter(mPointOfInterestAdapter);


        String directoryTag = DirectoryListFragmentArgs.fromBundle(getArguments()).getDirectoryTag();

        mViewModel.getAllPoisByDirectoryTag(directoryTag).observe(this, new Observer<List<PointOfInterest>>() {
            @Override
            public void onChanged(List<PointOfInterest> pointOfInterests) {
                mPointOfInterestModels = pointOfInterests;

                mPointOfInterestAdapter.edit()
                        .replaceAll(pointOfInterests)
                        .commit();
            }
        });

        mViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
//                categories.get(0).getName();
                mCategoryAdapter.edit()
                        .replaceAll(categories)
                        .commit();
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
        if (mDirectoryListFragmentBinding.poiEditProgressBar.getVisibility() != View.VISIBLE) {
            mDirectoryListFragmentBinding.poiEditProgressBar.setVisibility(View.VISIBLE);
            mDirectoryListFragmentBinding.poiEditProgressBar.setAlpha(0.0f);
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mDirectoryListFragmentBinding.poiEditProgressBar, View.ALPHA, 1.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();

        mDirectoryListFragmentBinding.poiRecyclerView.setAlpha(0.5f);
    }

    @Override
    public void onEditFinished() {
        mDirectoryListFragmentBinding.poiRecyclerView.scrollToPosition(0);
        mDirectoryListFragmentBinding.poiRecyclerView.setAlpha(1.0f);

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mDirectoryListFragmentBinding.poiEditProgressBar, View.ALPHA, 0.0f);
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
                    mDirectoryListFragmentBinding.poiEditProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mAnimator.start();
    }

}
