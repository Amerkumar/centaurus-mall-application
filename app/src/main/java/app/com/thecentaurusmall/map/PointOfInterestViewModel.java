package app.com.thecentaurusmall.map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.com.thecentaurusmall.MainRepository;
import app.com.thecentaurusmall.model.PointOfInterest;

public class PointOfInterestViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private MainRepository mainRepository;
    private LiveData<List<PointOfInterest>> mAllPointOfInterest;

    public PointOfInterestViewModel(@NonNull Application application) {
        super(application);
        this.mainRepository = new MainRepository(application);
        this.mAllPointOfInterest = mainRepository.getAllPois();
    }

//    public void fetchAllPoisByDirectoryTag(String )
    LiveData<List<PointOfInterest>> getAllPois() {
        return mAllPointOfInterest;
    }

}
