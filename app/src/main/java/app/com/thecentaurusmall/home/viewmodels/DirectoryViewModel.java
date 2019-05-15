package app.com.thecentaurusmall.home.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.com.thecentaurusmall.MainRepository;
import app.com.thecentaurusmall.model.FeaturedSlideModel;

public class DirectoryViewModel extends ViewModel {

    private MainRepository mainRepository;

    public DirectoryViewModel() {
        mainRepository = new MainRepository();
    }

    public LiveData<List<FeaturedSlideModel>> getFeaturedSlides() {
        return mainRepository.getFeaturedSlides();
    }
}
