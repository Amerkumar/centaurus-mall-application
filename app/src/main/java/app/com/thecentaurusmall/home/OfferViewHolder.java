package app.com.thecentaurusmall.home;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.Utils.Utils;
import app.com.thecentaurusmall.databinding.OfferItemBinding;
import app.com.thecentaurusmall.model.Offer;

public class OfferViewHolder extends RecyclerView.ViewHolder {


    //     wrong venue id
    private static final String VENUE_ID = "SZfhUYddmjU6nJoX3PqC";
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
            url = Utils.getUrlByToken(DEALS_FOLDER, VENUE_ID, item.getName(),
                    Utils.getDensityName(mContext), token);
        }

        Log.d("Url", url);
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.logo_mapin)
                .error(R.drawable.error_placeholder)
                .into(mBinding.offerImageView);

        mBinding.daysLeftTextView.setText(String.format("Only %d Days left!", getNumberOfDaysLeft(item.getEnd_date().toDate().toString())));
    }


    private long getNumberOfDaysLeft(String endDate) {
        String[] parts = endDate.split("T");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        long result = 0;
        try {
            Date date1 = targetFormat.parse(parts[0]);
            Log.d("Deal Adapter", date1.toString());
            long diff = date1.getTime() - Calendar.getInstance().getTime().getTime();
            result = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return result;
    }

}
