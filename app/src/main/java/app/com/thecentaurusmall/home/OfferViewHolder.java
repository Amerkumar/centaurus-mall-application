package app.com.thecentaurusmall.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.com.thecentaurusmall.Utils.Utils;
import app.com.thecentaurusmall.databinding.OfferItemBinding;
import app.com.thecentaurusmall.model.Offer;

public class OfferViewHolder extends RecyclerView.ViewHolder {



    private static final String VENUE_ID = "Hc1uoaoWwUM1EABgh213";
    private static String DEALS_FOLDER = "deals_thumbnails";

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
        mBinding.setModel(item);
        String url = null;
//        item.getUrl()
        if (item.getUrl() != null) {
            String token = Utils.getTokenByDensity(item.getUrl(), Utils.getDensityName(mContext));
            url = Utils.getDealUrlByToken(DEALS_FOLDER, VENUE_ID , item.getName(),
                    Utils.getDensityName(mContext), token);
        }

//            int resourceId = context.getResources().getIdentifier(imagePath, "drawable", "app.getitt.getitt");
//            holder.brandImageView.setImageResource(resourceId);
//            Picasso.get()
//                    .load(url)
//                    .placeholder(R.drawable.ic_category_placeholder)
//                    .error(R.drawable.ic_add)
//                    .into(holder.brandImageView)
//            ;


//            Glide.with(context).load(url)
//                    .thumbnail(Glide.with(context).load(R.drawable.placeholder) .error(R.drawable.error_placeholder))


        Glide.with(context).load(url)
                .thumbnail(Glide.with(context).load(R.drawable.error_placeholder))
                .centerCrop()
                .error(R.drawable.error_placeholder)
                .into(holder.brandImageView)

        ;
    }
}
