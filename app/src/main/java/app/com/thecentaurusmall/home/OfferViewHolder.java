package app.com.thecentaurusmall.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import app.com.thecentaurusmall.category.CategoryAdapter;
import app.com.thecentaurusmall.databinding.CategoryItemBinding;
import app.com.thecentaurusmall.databinding.OfferItemBinding;
import app.com.thecentaurusmall.model.Category;
import app.com.thecentaurusmall.model.Offer;

public class OfferViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private final OfferItemBinding mBinding;


    public OfferViewHolder(Context context, OfferItemBinding binding) {
        super(binding.getRoot());
        mContext = context;
//        binding.setListener(listener);
        mBinding = binding;
    }

    protected void performBind(@NonNull Offer item) {
//        String iconResourceName = "ic_" + item.getName().replaceAll(" ", "_").toLowerCase();
          mBinding.offerTitle.setText(item.getName());
        // if not zero then resource is present
//        mBinding.setModel(item);
//        mBinding.setModel(item);
    }
}
