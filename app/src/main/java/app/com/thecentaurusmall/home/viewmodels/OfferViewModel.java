package app.com.thecentaurusmall.home.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import app.com.thecentaurusmall.home.OffersDataSourceFactory;
import io.reactivex.Observable;

public class OfferViewModel extends ViewModel {

    private OffersDataSourceFactory offersDataSourceFactory;
    private PagedList.Config config;

    public OfferViewModel() {
        offersDataSourceFactory = new OffersDataSourceFactory();

        config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build();
    }

    public Observable<PagedList> getPagedListObservable() {
        return new RxPagedListBuilder(offersDataSourceFactory, config).buildObservable();
    }

}
