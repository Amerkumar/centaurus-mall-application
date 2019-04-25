package app.com.thecentaurusmall.category;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.databinding.CategoryItemBinding;
import app.com.thecentaurusmall.model.Category;

public class CategoryViewHolder extends SortedListAdapter.ViewHolder<Category> {

    private Context mContext;
    private final CategoryItemBinding mBinding;


    public CategoryViewHolder(Context context, CategoryItemBinding binding, CategoryAdapter.Listener listener) {
        super(binding.getRoot());
        mContext = context;
        binding.setListener(listener);
        mBinding = binding;
    }

    @Override
    protected void performBind(@NonNull Category item) {
        mBinding.categoryItemImageview.setBadgeValue(item.getCount());
        mBinding.categoryItemTextView.setText(item.getName());
        String iconResourceName = "ic_" + item.getName().replaceAll(" ", "_").toLowerCase();

        // if not zero then resource is present
        if (mContext.getResources().getIdentifier(iconResourceName, "drawable", mContext.getPackageName()) != 0) {
            mBinding.categoryItemImageview.setBackgroundResource(mContext.getResources().getIdentifier(iconResourceName, "drawable", mContext.getPackageName()));
        }
        // resource not present
        else {
            mBinding.categoryItemImageview.setBackgroundResource(mContext.getResources().getIdentifier("ic_others", "drawable", mContext.getPackageName()));
        }
        mBinding.setModel(item);
    }
}
