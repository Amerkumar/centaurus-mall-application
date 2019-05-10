package app.com.thecentaurusmall.home.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.google.firebase.firestore.Query;

import app.com.thecentaurusmall.MainRepository;

public class OfferViewModel extends ViewModel {
    //
    private PagedList.Config config;
    private MainRepository mainRepository;

    public OfferViewModel() {
//        offersDataSourceFactory = new OffersDataSourceFactory();
        mainRepository = new MainRepository();
    }

    public Query getOffersQuery() {
        return mainRepository.getOffersQuery();
    }

    public void addDummyOffers() {
        mainRepository.addOffersDummyData();
    }


}
