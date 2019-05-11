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
import app.com.thecentaurusmall.databinding.EventItemBinding;
import app.com.thecentaurusmall.model.Event;

public class EventViewHolder extends RecyclerView.ViewHolder {


    //     wrong venue id
    private static final String VENUE_ID = "Hc1uoaoWwUM1EABgh213";
    private static String EVENTS_FOLDER = "events_thumbnails";

    private Context mContext;
    private final EventItemBinding mBinding;


    public EventViewHolder(Context context, EventItemBinding binding, EventsFragment.Listener onEventItemClick) {
        super(binding.getRoot());
        mContext = context;

        binding.setListener(onEventItemClick);
//        binding.setListener(listener);
        mBinding = binding;
    }

    protected void performBind(@NonNull Event item) {
//        String iconResourceName = "ic_" + item.getName().replaceAll(" ", "_").toLowerCase();
        mBinding.setModel(item);
        String url = null;
        if (item.getUrl() != null) {
            String token = Utils.getTokenByDensity(item.getUrl(), Utils.getDensityName(mContext));
            url = Utils.getUrlByToken(EVENTS_FOLDER, VENUE_ID, item.getName(),
                    Utils.getDensityName(mContext), token);
        }

        Log.d("Events", url);
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.loading_white)
                .error(R.drawable.error_placeholder)
                .into(mBinding.eventImageView);


        mBinding.datesRange.setText(Utils.timestampToSimpleDateFormat(item.getStart_date().toDate())
                + " - " + Utils.timestampToSimpleDateFormat(item.getEnd_date().toDate()));

    }


}
