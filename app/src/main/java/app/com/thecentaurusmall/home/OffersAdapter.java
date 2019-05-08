package app.com.thecentaurusmall.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.category.CategoryViewHolder;
import app.com.thecentaurusmall.databinding.CategoryItemBinding;
import app.com.thecentaurusmall.databinding.OfferItemBinding;
import app.com.thecentaurusmall.model.Offer;

public class OffersAdapter extends PagedListAdapter<Offer,
                                    OfferViewHolder> {

    private Context mContext;

    public OffersAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    public static DiffUtil.ItemCallback<Offer> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Offer>() {
                @Override
                public boolean areItemsTheSame(@NonNull Offer rank,
                                               @NonNull Offer rankTwo) {
                    return rank.getRank() == rankTwo.getRank();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Offer rank,
                                                  @NonNull Offer rankTwo) {
                    return rank.equals(rankTwo);
                }
            };

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final OfferItemBinding binding = OfferItemBinding.inflate(layoutInflater, parent, false);
//        return new CategoryViewHolder(mContext,binding, mCategoryListener);
//        View view = layoutInflater.inflate(R.layout.offer_item, parent, false);
        return new OfferViewHolder(mContext, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = getItem(position);
        if (offer != null) {
            holder.performBind(offer);
        }
    }
}
