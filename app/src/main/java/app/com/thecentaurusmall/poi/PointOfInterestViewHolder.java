package app.com.thecentaurusmall.poi;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import app.com.thecentaurusmall.databinding.PoiItemBinding;
import app.com.thecentaurusmall.model.PointOfInterest;

public class PointOfInterestViewHolder extends SortedListAdapter.ViewHolder<app.com.thecentaurusmall.model.PointOfInterest> {

    private final PoiItemBinding mBinding;


    public PointOfInterestViewHolder(PoiItemBinding binding, PointOfInterestAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);
        mBinding = binding;
    }

    @Override
    protected void performBind(@NonNull PointOfInterest item) {
        mBinding.setModel(item);
    }
}
