package app.com.thecentaurusmall;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import app.com.thecentaurusmall.model.PointOfInterest;

public class MainRepository {


    private static final String VENUE_ID = "Hc1uoaoWwUM1EABgh213";
    private static final String TAG = MainRepository.class.getSimpleName();
    private final FirebaseFirestore mFirestoredb;

    public MainRepository(Application application) {

        mFirestoredb = FirebaseFirestore.getInstance();

    }

    public LiveData<List<PointOfInterest>> getAllPois() {

        String path = "indoors/" + VENUE_ID + "/pois";

        MutableLiveData<List<PointOfInterest>> pointOfInterestList = new MutableLiveData<>();
        Query collectionReference = mFirestoredb.collection(path).orderBy("name");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Listen failed");
                    return;
                }

                List<PointOfInterest> pointOfInterests = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(queryDocumentSnapshots)) {
                    if (documentSnapshot.exists()) {

                        try {

                            HashMap<String, Double> geoPoint  = (HashMap<String, Double>) documentSnapshot.get("_geoloc");

                            LatLng latLng = new LatLng(geoPoint.get("lat"), geoPoint.get("lng"));
                            PointOfInterest pointOfInterest = new PointOfInterest(
                                    documentSnapshot.getId(),
                                    documentSnapshot.getString("name"),
                                    documentSnapshot.getString("category"),
                                    latLng,
                                    (Long) documentSnapshot.get("floor_num"));
                            pointOfInterests.add(pointOfInterest);

                        } catch (NullPointerException el) {
                            el.printStackTrace();
                        } catch (ClassCastException cle) {
                            cle.printStackTrace();
                        }

                    }
                }

                pointOfInterestList.postValue(pointOfInterests);
            }
        });
        return pointOfInterestList;
    }

}
