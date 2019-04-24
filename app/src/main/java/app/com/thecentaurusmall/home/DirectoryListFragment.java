package app.com.thecentaurusmall.home;

import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.DirectoryListFragmentBinding;
import app.com.thecentaurusmall.model.PointOfInterest;
import app.com.thecentaurusmall.poi.PointOfInterestAdapter;

public class DirectoryListFragment extends Fragment implements SortedListAdapter.Callback {

    private DirectoryListViewModel mViewModel;

    private DirectoryListFragmentBinding mDirectoryListFragmentBinding;

    private static final Comparator<PointOfInterest> COMPARATOR = new SortedListAdapter.ComparatorBuilder<PointOfInterest>()
            .setOrderForModel(PointOfInterest.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();
    private PointOfInterestAdapter mAdapter;
    private Animator mAnimator;

    public static DirectoryListFragment newInstance() {
        return new DirectoryListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mDirectoryListFragmentBinding = DirectoryListFragmentBinding.inflate(inflater, container, false);

        return mDirectoryListFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DirectoryListViewModel.class);


        mAdapter = new PointOfInterestAdapter(getContext(), COMPARATOR, poiModel -> {
            Snackbar.make(mDirectoryListFragmentBinding.getRoot(), poiModel.getName(), Snackbar.LENGTH_SHORT).show();
        });

        mAdapter.addCallback(this);

        mDirectoryListFragmentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        mDirectoryListFragmentBinding.poiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDirectoryListFragmentBinding.poiRecyclerView.setAdapter(mAdapter);
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

        mDirectoryListFragmentBinding.poiRecyclerView.animate().alpha(0.5f);
    }

    @Override
    public void onEditFinished() {
        mDirectoryListFragmentBinding.poiRecyclerView.scrollToPosition(0);
        mDirectoryListFragmentBinding.poiRecyclerView.animate().alpha(1.0f);

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
