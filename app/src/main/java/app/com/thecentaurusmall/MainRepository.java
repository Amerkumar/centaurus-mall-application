package app.com.thecentaurusmall;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import app.com.thecentaurusmall.Utils.Utils;
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
                            if (categoryHashMap.containsKey(category)) {
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
                            if (categoryHashMap.containsKey(category)) {
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

    public Query getOffersQuery() {
        String path = "indoors/" + VENUE_ID + "/deals";
        Query collectionReference = mFirestoredb.collection(path);

        return collectionReference;
    }

    // get offers by pagination
    public void getOffersByPagination(final int startRank, final int size,
                                      @NonNull final ItemKeyedDataSource.LoadCallback<Offer> callback) {

        String path = "indoors/" + VENUE_ID + "/deals";
        Query collectionReference = mFirestoredb.collection(path);

        return;
//        collectionReference
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot snapshots,
//                                        @Nullable FirebaseFirestoreException e) {
//
//                        if (e != null) {
//                            Log.w("", "exception in fetching from firestore", e);
//                            return;
//                        }
//                        List<Offer> offersList = new ArrayList<>();
//                        int rank = 1;
//                        Log.d(TAG, String.valueOf(snapshots.size()));
//                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
//                            HashMap<String, Double> geoPoint = (HashMap<String, Double>) doc.get("_geoloc");
//                            LatLng latLng = new LatLng(geoPoint.get("lat"), geoPoint.get("lng"));
//
//
//                            HashMap<String, String> url = (HashMap<String, String>) doc.get("url");
//
////                            Log.d(TAG, doc.getId());
////                            Log.d(TAG, doc.getTimestamp("start_date").toString());
////                            Log.d(TAG, String.valueOf(doc.getLong("floor")));
//                            Log.d(TAG, doc.getString("name"));
////                            Log.d(TAG, String.valueOf(doc.getLong("percentage")));
////                            Log.d(TAG, url.get("mdpi"));
//
//                            try {
//                                Offer offer = new Offer(rank, doc.getId(),
//                                        doc.getString("category"), latLng,
//                                        doc.getTimestamp("end_date"), doc.getLong("floor"),
//                                        doc.getString("name"), doc.getLong("percentage"),
//                                        doc.getTimestamp("start_date"), url,
//                                        doc.getString("description"));
//
//                                offersList.add(offer);
//                                rank++;
//                            } catch (NullPointerException e1) {
//                                e1.printStackTrace();
//                            }
//                        }
//                    }
//                });
    }


    public void addOffersDummyData() {


        String[] brands = {"KFC", "Hardess", "Burger King","Howdy", "Johny Rockets",
                            "Kim Mun", "Magnum", "OPTP", "Pizza Hut", "Rendevenous",
                            "TGI Friday", "Tayto"};


//        String brand = "test";
        for (String brand : brands) {
            String path = "indoors/" + VENUE_ID + "/deals";
            CollectionReference collectionReference = mFirestoredb.collection(path);


            app.com.thecentaurusmall.model.LatLng _geoloc = new app.com.thecentaurusmall.model.LatLng();
            _geoloc.setLatitude(0.0);
            _geoloc.setLongitude(0.0);

            Timestamp date = Timestamp.now();

            HashMap<String, String> url = new HashMap<>();
            url.put("hdpi", "");
            url.put("ldpi","");
            url.put("mdpi", "");
            url.put("xhdpi", "");
            url.put("xxhdpi", "");
            url.put("xxxhdpi", "");

            Offer offer = new Offer(1, brand, brand, _geoloc, date, 4,
                    brand, 30, date, url, brand);
            collectionReference
                    .add(offer)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

    }

    public Query getNextMonthEvents() {

        String path = "indoors/" + VENUE_ID + "/events";

        return  mFirestoredb.collection(path)
                .whereGreaterThan("start_date", new Date())
                .whereLessThanOrEqualTo("start_date", Utils.addOrSubtractDaysFromCurrent(30))
                .orderBy("start_date")
                ;
    }


    public Query getNextWeekEvents() {

        String path = "indoors/" + VENUE_ID + "/events";

        return  mFirestoredb.collection(path)
                .whereGreaterThan("start_date", new Date())
                .whereLessThanOrEqualTo("start_date", Utils.addOrSubtractDaysFromCurrent(7))
                .orderBy("start_date")
                ;
    }

    public Query getTodayEvents() {

        String path = "indoors/" + VENUE_ID + "/events";

        return  mFirestoredb.collection(path)
                .whereEqualTo("start_date", new Date())
                .orderBy("start_date")
                ;
    }

    public Query getLastWeekEvents() {


        String path = "indoors/" + VENUE_ID + "/events";
        return mFirestoredb.collection(path)
                .whereGreaterThanOrEqualTo("start_date", Utils.addOrSubtractDaysFromCurrent(-7))
                .whereLessThan("start_date", new Date());

    }

    public Query getLastMonthEvents() {

        String path = "indoors/" + VENUE_ID + "/events";
        return mFirestoredb.collection(path)
                .whereGreaterThanOrEqualTo("start_date", Utils.addOrSubtractDaysFromCurrent(-30))
                .whereLessThan("start_date", new Date());
    }
}
