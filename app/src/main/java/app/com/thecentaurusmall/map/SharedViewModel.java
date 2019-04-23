package app.com.thecentaurusmall.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.com.thecentaurusmall.model.PointOfInterest;

public class SharedViewModel extends ViewModel {






    private final MutableLiveData<Integer> selectedFieldPoiCode = new MutableLiveData<>();

    //    Search Bar Poi Here
    private final MutableLiveData<PointOfInterest> selectedSearchBarPoi = new MutableLiveData<PointOfInterest>();

//    Directions Bar From Poi Here
    private final MutableLiveData<PointOfInterest> selectedDirectionsBarFromPoi = new MutableLiveData<>();
//    Directions Bar To Poi Here
    private final MutableLiveData<PointOfInterest> selectedDirectionsBarToPoi = new MutableLiveData<>();

    public void setSelectedFieldPoiCode(Integer item) {
        selectedFieldPoiCode.setValue(item);
    }

    public LiveData<Integer> getSelectedFieldPoiCode() {
        return selectedFieldPoiCode;
    }


    public void searchBarPoi(PointOfInterest item) {
        selectedSearchBarPoi.setValue(item);
    }

    public LiveData<PointOfInterest> getSelectedSearchBarPoi() {
        return selectedSearchBarPoi;
    }



    public void directionsBarFromPoi(PointOfInterest item) {
        selectedDirectionsBarFromPoi.setValue(item);
    }

    public LiveData<PointOfInterest> getSelectedDirectionsBarFromPoi() {
        return selectedDirectionsBarFromPoi;
    }


    public void directionsBarToPoi(PointOfInterest item) {
        selectedDirectionsBarToPoi.setValue(item);
    }

    public LiveData<PointOfInterest> getSelectedDirectionsBarToPoi() {
        return selectedDirectionsBarToPoi;
    }


}
