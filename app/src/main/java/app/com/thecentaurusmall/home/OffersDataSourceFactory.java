package app.com.thecentaurusmall.home;

import androidx.paging.DataSource;

import app.com.thecentaurusmall.model.Offer;

public class OffersDataSourceFactory extends DataSource.Factory<Integer, Offer> {

    @Override
    public DataSource<Integer, Offer> create() {
        return new OffersDataSource();
    }
}
