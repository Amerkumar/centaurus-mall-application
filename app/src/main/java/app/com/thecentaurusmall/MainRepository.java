package app.com.thecentaurusmall;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import app.com.thecentaurusmall.category.CategoryAdapter;
import app.com.thecentaurusmall.model.Category;
import app.com.thecentaurusmall.model.Offer;
import app.com.thecentaurusmall.model.PointOfInterest;

public class MainRepository {


    private static final String VENUE_ID = "Hc1uoaoWwUM1EABgh213";
    private static final String TAG = MainRepository.class.getSimpleName();
    private final FirebaseFirestore mFirestoredb;

    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();


    public MainRepository() {

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

                int rank = 1;
                List<PointOfInterest> pointOfInterests = new ArrayList<>();
                HashMap<String, Category> categoryHashMap = new HashMap<>();
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(queryDocumentSnapshots)) {
                    if (documentSnapshot.exists()) {

                        try {

                            HashMap<String, Double> geoPoint = (HashMap<String, Double>) documentSnapshot.get("_geoloc");
                            LatLng latLng = new LatLng(geoPoint.get("lat"), geoPoint.get("lng"));
                            PointOfInterest pointOfInterest = new PointOfInterest(
                                    rank, documentSnapshot.getId(),
                                    documentSnapshot.getString("name"),
                                    documentSnapshot.getString("category"),
                                    latLng,
                                    (Long) documentSnapshot.get("floor_num"),
                                    documentSnapshot.getString("directory_tag"),
                                    documentSnapshot.getString("description")
                                    );
                            pointOfInterests.add(pointOfInterest);

                            // if some category is present in hash map
                            // then we can increase the category count
                            String category = documentSnapshot.getString("category");
                            if (categoryHashMap.containsKey(category)){
                                Category categoryObj = categoryHashMap.get(category);
                                categoryObj.incrementCount();
                                Log.d(TAG, categoryObj.getName() + " " + categoryObj.getCount());
                            }
                            // else category is not present in hashmap
                            else {
                                categoryHashMap.put(category, new Category(rank,
                                        category,
                                        1));
                            }

                            rank++;
                        } catch (NullPointerException el) {
                            el.printStackTrace();
                        } catch (ClassCastException cle) {
                            cle.printStackTrace();
                        }

                    }
                }

                categoryList.postValue(new ArrayList<>(categoryHashMap.values()));
                pointOfInterestList.postValue(pointOfInterests);
            }
        });
        return pointOfInterestList;
    }

    public LiveData<List<PointOfInterest>> getAllPoisByDirectoryTag(String directoryTag) {

        String path = "indoors/" + VENUE_ID + "/pois";

        MutableLiveData<List<PointOfInterest>> pointOfInterestList = new MutableLiveData<>();


        Query collectionReference = mFirestoredb.collection(path)
                .whereEqualTo("directory_tag", directoryTag)
                .orderBy("name");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Listen failed");
                    return;
                }

                int rank = 1;
                List<PointOfInterest> pointOfInterests = new ArrayList<>();
                HashMap<String, Category> categoryHashMap = new HashMap<>();
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(queryDocumentSnapshots)) {
                    if (documentSnapshot.exists()) {

                        try {

                            HashMap<String, Double> geoPoint = (HashMap<String, Double>) documentSnapshot.get("_geoloc");
                            LatLng latLng = new LatLng(geoPoint.get("lat"), geoPoint.get("lng"));
                            PointOfInterest pointOfInterest = new PointOfInterest(
                                    rank, documentSnapshot.getId(),
                                    documentSnapshot.getString("name"),
                                    documentSnapshot.getString("category"),
                                    latLng,
                                    (Long) documentSnapshot.get("floor_num"),
                                    documentSnapshot.getString("directory_tag"),
                                    documentSnapshot.getString("description")
                            );
                            pointOfInterests.add(pointOfInterest);

                            // if some category is present in hash map
                            // then we can increase the category count
                            String category = documentSnapshot.getString("category");
                            if (categoryHashMap.containsKey(category)){
                                Category categoryObj = categoryHashMap.get(category);
                                categoryObj.incrementCount();
                                Log.d(TAG, categoryObj.getName() + " " + categoryObj.getCount());
                            }
                            // else category is not present in hashmap
                            else {
                                categoryHashMap.put(category, new Category(rank,
                                        category,
                                        1));
                            }

                            rank++;
                        } catch (NullPointerException el) {
                            el.printStackTrace();
                        } catch (ClassCastException cle) {
                            cle.printStackTrace();
                        }

                    }
                }


                categoryList.postValue(new ArrayList<>(categoryHashMap.values()));
                pointOfInterestList.postValue(pointOfInterests);
            }
        });
        return pointOfInterestList;
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryList;
    }



    // get offers by pagination
    public void getOffersByPagination(final int startRank, final int size,
                            @NonNull final ItemKeyedDataSource.LoadCallback<Offer> callback){

        String path = "indoors/" + VENUE_ID + "/offers";
        Query collectionReference = mFirestoredb.collection(path);
                collectionReference.limit(size)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w("", "exception in fetching from firestore", e);
                    return;
                }
                List<Offer> offersList = new ArrayList<>();
                for(DocumentSnapshot doc : snapshots.getDocuments()){
                    offersList.add(doc.toObject(Offer.class));
                }

                if(offersList.size() == 0){
                    return;
                }
                if(callback instanceof ItemKeyedDataSource.LoadInitialCallback){
                    //initial load
                    ((ItemKeyedDataSource.LoadInitialCallback)callback)
                            .onResult(offersList, 0, offersList.size());
                }else{
                    //next pages load
                    callback.onResult(offersList);
                }
            }
        });
    }
}
