package app.com.thecentaurusmall.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;

import app.com.thecentaurusmall.databinding.PoiItemBinding;
import app.com.thecentaurusmall.model.PointOfInterest;

public class PointOfInterestAdapter extends SortedListAdapter<PointOfInterest> {

    public interface Listener {
        void onPointOfInterestClicked(PointOfInterest poiModel);
    }

    private final Listener mPoiListener;

    public PointOfInterestAdapter(@NonNull Context context,
                                  @NonNull Comparator<PointOfInterest> comparator, Listener listener) {
        super(context, PointOfInterest.class ,comparator);
        mPoiListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder<? extends PointOfInterest> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final PoiItemBinding binding = PoiItemBinding.inflate(inflater, parent, false);
        return new PointOfInterestViewHolder(binding, mPoiListener);
    }
}
