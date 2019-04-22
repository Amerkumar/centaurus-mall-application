package app.com.thecentaurusmall.map;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.com.thecentaurusmall.model.PointOfInterest;

public class SharedViewModel extends ViewModel {


    private final MutableLiveData<PointOfInterest> selected = new MutableLiveData<PointOfInterest>();

    public void select(PointOfInterest item) {
        selected.setValue(item);
    }

    public LiveData<PointOfInterest> getSelected() {
        return selected;
    }
}
