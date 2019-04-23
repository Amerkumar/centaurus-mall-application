package app.com.thecentaurusmall.map;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import app.com.thecentaurusmall.databinding.CategoryItemBinding;
import app.com.thecentaurusmall.databinding.PoiItemBinding;
import app.com.thecentaurusmall.model.Category;
import app.com.thecentaurusmall.model.PointOfInterest;

public class CategoryViewHolder extends SortedListAdapter.ViewHolder<Category> {

    private final CategoryItemBinding mBinding;


    public CategoryViewHolder(CategoryItemBinding binding, CategoryAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);
        mBinding = binding;
    }

    @Override
    protected void performBind(@NonNull Category item) {
        mBinding.setModel(item);
    }
}
