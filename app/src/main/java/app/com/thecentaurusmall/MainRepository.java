package app.com.thecentaurusmall;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import app.com.thecentaurusmall.model.PointOfInterest;

public class MainRepository {

    public MainRepository(Application application) {

    }

    public LiveData<List<PointOfInterest>> getAllPois() {
        MutableLiveData<List<PointOfInterest>> pointOfInterestList = new MutableLiveData<>();
        return pointOfInterestList;
    }

}
