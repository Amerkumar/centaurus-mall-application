package app.com.thecentaurusmall.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.com.thecentaurusmall.MainRepository;
import app.com.thecentaurusmall.model.PointOfInterest;

public class DirectoryListViewModel extends AndroidViewModel {

    private MainRepository mainRepository;

    public DirectoryListViewModel(@NonNull Application application) {
        super(application);
        this.mainRepository = new MainRepository(application);
    }

    LiveData<List<PointOfInterest>> getAllPoisBy


}
