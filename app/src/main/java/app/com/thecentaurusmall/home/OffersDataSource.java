package app.com.thecentaurusmall.home;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import app.com.thecentaurusmall.MainRepository;
import app.com.thecentaurusmall.model.Offer;

public class OffersDataSource extends ItemKeyedDataSource<Integer, Offer> {

    private MainRepository mainRepository;

    public OffersDataSource() {
        mainRepository = new MainRepository();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Offer> callback) {
        mainRepository.getOffersByPagination(0, params.requestedLoadSize, callback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Offer> callback) {
        mainRepository.getOffersByPagination(params.key, params.requestedLoadSize, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Offer> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull Offer item) {
        return item.getRank();
    }
}
