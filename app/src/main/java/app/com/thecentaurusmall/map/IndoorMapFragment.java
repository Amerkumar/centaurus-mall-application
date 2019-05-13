package app.com.thecentaurusmall.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IAOrientationListener;
import com.indooratlas.android.sdk.IAOrientationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.IARoute;
import com.indooratlas.android.sdk.IAWayfindingListener;
import com.indooratlas.android.sdk.IAWayfindingRequest;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.Utils.Utils;
import app.com.thecentaurusmall.databinding.IndoorMapFragmentBinding;
import app.com.thecentaurusmall.model.PointOfInterest;

public class IndoorMapFragment extends Fragment implements
        OnMapReadyCallback,
        FloorSelectionDialog.FloorSelectDialogListener,
        IALocationListener,
        IARegion.Listener {

    /* used to decide when bitmap should be downscaled */
    private static final int MAX_DIMENSION = 2048;


    private static final String TAG = IndoorMapFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private SharedViewModel mViewModel;
    private GoogleMap mMap;
    private MapView mapView;
    private Circle mCircle;
    private Marker mHeadingMarker;
    private IALocationManager mIALocationManager;
    private boolean fabFlag;
    private MaterialButton floorButton;
    private boolean mCameraPositionNeedsUpdating;
    private GroundOverlay mGroundOverlay;
    private IARegion mOverlayFloorPlan;
    private IAWayfindingRequest mWayfindingDestination;
    private IARoute mCurrentRoute;
    private Target mLoadTarget;
    private View rootView;
    private List<Polyline> mPolylines = new ArrayList<>();
    private LatLng mUserLocation;
    private PointOfInterest mSelectedPoi;
    private IndoorMapFragmentBinding mIndoorMapFragmentBinding;


    private static final int TYPE_SEARCH_BAR_POI = 0;
    private static final int TYPE_DIRECTION_BAR_FROM_POI = 1;
    private static final int TYPE_DIRECTION_BAR_TO_POI = 2;
    private static final int TYPE_NONE = 3;
    private SharedViewModel msharedViewModel;
    private Marker mDestinationMarker;

    public static IndoorMapFragment newInstance() {
        return new IndoorMapFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here, getActivity is the current activity
        // check for location permission
        // 1. If Given, On the GPS Location Services
        // 2. else ask for location permission.
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted
            getLocationSettings();
        } else {
            requestPermissions(new String[]{

                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE
                    },
                    PERMISSIONS_REQUEST_LOCATION);
        }
        // instantiate IALocationManager
        mIALocationManager = IALocationManager.create(getContext());
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


    private void getLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());


        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {

                Log.d(TAG, "ON Request Permission Result");

                // If request is cancelled, the result arrays are empty.
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(getContext())
                            .setTitle("Location Permission")
                            .setMessage("We need your location for finding your location in The Centaurus Mall.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(
                                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                            PERMISSIONS_REQUEST_LOCATION);
                                }
                            }).show();

                } else {
                    //                 Log.d(TAG, "Permission granted");
                    getLocationSettings();

                }
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    /**
     * Fragment Lifecycle Methods
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (rootView == null) {

            mIndoorMapFragmentBinding = IndoorMapFragmentBinding.inflate(inflater, container, false);

            mapView = mIndoorMapFragmentBinding.map;
            mapView.onCreate(savedInstanceState);
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mIndoorMapFragmentBinding.floorMaterialButton
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence[] floors = new CharSequence[]{
                                    "Auto Floor (Enabled By Default)",
                                    "Fourth Floor",
                                    "Third Floor",
                                    "Second Floor",
                                    "First Floor",
                                    "Ground Floor"};

                            // Use the Builder class for convenient dialog construction
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Floor")
                                    .setItems(floors, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // The 'which' argument contains the index position
                                            // of the selected item
                                            onDialogFloorClick(which);
                                        }
                                    })
                                    .show();

                        }
                    });

            mIndoorMapFragmentBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUserLocation != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mUserLocation, 19.0f));
                    } else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.707991, 73.050229), 16.0f));
                    }
                }
            });
            rootView = mIndoorMapFragmentBinding.getRoot();

            mapView.getMapAsync(this);
        }
        return mIndoorMapFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);



//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        msharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

//        This is for search bar poi here
        msharedViewModel.getSelectedSearchBarPoi().observe(this, new Observer<PointOfInterest>() {
            @Override
            public void onChanged(PointOfInterest pointOfInterest) {

                switch (msharedViewModel.getSelectedFieldPoiCode().getValue()) {
                    case TYPE_SEARCH_BAR_POI:
                        mIndoorMapFragmentBinding.poiSearchBarTextview.setText(pointOfInterest.getName());
                        mIndoorMapFragmentBinding.poiSearchBarClearImageview.setVisibility(View.VISIBLE);
                        onPoiClick(pointOfInterest);
                        break;
                    case TYPE_DIRECTION_BAR_FROM_POI:
                        mIndoorMapFragmentBinding.poiDirectionsFrom.setText(pointOfInterest.getName());
                        break;
                    case TYPE_DIRECTION_BAR_TO_POI:
                        mIndoorMapFragmentBinding.poiDirectionsTo.setText(pointOfInterest.getName());
                        break;
                    case TYPE_NONE:
                        break;
                }
                mSelectedPoi = pointOfInterest;
            }
        });



        //            Point of Interest Search Bar TextView
        mIndoorMapFragmentBinding
                .poiSearchBarTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msharedViewModel.setSelectedFieldPoiCode(TYPE_SEARCH_BAR_POI);

                Navigation.findNavController(v).navigate(R.id.pointOfInterestFragment);
            }
        });

        mIndoorMapFragmentBinding
                .poiSearchBarClearImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIndoorMapFragmentBinding
                        .poiSearchBarTextview.setText(getString(R.string.poi_search_bar_hint));
                mSelectedPoi = null;
                mIndoorMapFragmentBinding
                        .poiSearchBarClearImageview.setVisibility(View.GONE);
                msharedViewModel.setSelectedFieldPoiCode(TYPE_NONE);
                clearWayFindingRoute();
            }
        });


        // Directions toolbar must be shown here
        // 1.Search Bar must be gone
        // 2.If search bar text field is not empty then transfer it to directions bar destination
        // 3.Put my location in from field in directions bar
        // 4.Put Navigate Button


        mIndoorMapFragmentBinding
                .poiSearchBarDirectionsImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndoorMapFragmentBinding.poiSearchBarContainer.setVisibility(View.GONE);
                if (mSelectedPoi != null) {
                    mIndoorMapFragmentBinding.poiDirectionsTo.setText(mSelectedPoi.getName());
                }
                mIndoorMapFragmentBinding.poiDirectionsBarContainer.setVisibility(View.VISIBLE);
            }
        });


        mIndoorMapFragmentBinding
                .poiDirectionsBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndoorMapFragmentBinding.poiDirectionsBarContainer.setVisibility(View.GONE);
                mIndoorMapFragmentBinding.poiSearchBarContainer.setVisibility(View.VISIBLE);
            }
        });

        mIndoorMapFragmentBinding
                .poiDirectionsFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msharedViewModel.setSelectedFieldPoiCode(TYPE_DIRECTION_BAR_FROM_POI);
                Navigation.findNavController(v).navigate(R.id.pointOfInterestFragment);
            }
        });


        mIndoorMapFragmentBinding
                .poiDirectionsTo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        msharedViewModel.setSelectedFieldPoiCode(TYPE_DIRECTION_BAR_TO_POI);
                        Navigation.findNavController(v).navigate(R.id.pointOfInterestFragment);
                    }
                }
        );

//        This is for directions bar from poi here

//        This is for directions bar to poi here

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.getUiSettings().setCompassEnabled(true);
        ViewGroup parent = (ViewGroup) mapView.findViewById(Integer.parseInt("1")).getParent();
        View compassButton = parent.getChildAt(4);
        /* now set position compass */
//        int padding = (int) Utils.convertDpToPixel(8, getContext());
//        compassButton.setPadding(padding, padding, padding, padding);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_END);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        Resources r = getContext().getResources();
        rlp.setMargins(0, (int) Utils.convertDpToPixel(112.0f, getContext()), 0, 0); // 160 la truc y , 30 la  truc x
//        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
//        rlp.((int)Utils.convertDpToPixel(160,getContext()));
        compassButton.setLayoutParams(rlp);

        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_style));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        mMap.setPadding(64, 64, 64, 64);

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng centaurus = new LatLng(33.707991, 73.050229
        );
//        mMap.addMarker(new MarkerOptions().position(centaurus).title("The Centaurus Mall"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centaurus, 16.0f));

        PointOfInterest pointOfInterestDestination = IndoorMapFragmentArgs.fromBundle(getArguments()).getPointOfInterestObject();
        if (pointOfInterestDestination != null) {
            mIndoorMapFragmentBinding.poiSearchBarTextview.setText(pointOfInterestDestination.getName());
            mIndoorMapFragmentBinding.poiSearchBarClearImageview.setVisibility(View.VISIBLE);
            onPoiClick(pointOfInterestDestination);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
//        ((MainActivity) getActivity()).hideToolbar();
        mapView.onResume();
        startLocationUpdates();


    }

    @Override
    public void onPause() {
        super.onPause();
//        ((MainActivity) getActivity()).showToolbar();
        mapView.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        stopLocationUpdates();

        msharedViewModel.setSelectedFieldPoiCode(TYPE_NONE);
        mIALocationManager.destroy();
    }


//    Location Methods


    private IAWayfindingListener mWayfindingListener = new IAWayfindingListener() {
        @Override
        public void onWayfindingUpdate(IARoute route) {
            mCurrentRoute = route;
            if (hasArrivedToDestination(route)) {
                // stop wayfinding
                showInfo("You're there!");
                mCurrentRoute = null;
                mWayfindingDestination = null;
                mIALocationManager.removeWayfindingUpdates();
                mIndoorMapFragmentBinding
                        .poiSearchBarTextview.setText(getString(R.string.poi_search_bar_hint));
                mSelectedPoi = null;
                mIndoorMapFragmentBinding
                        .poiSearchBarClearImageview.setVisibility(View.GONE);
                msharedViewModel.setSelectedFieldPoiCode(TYPE_NONE);
                clearWayFindingRoute();
            }
            updateRouteVisualization();
        }
    };

    private IAOrientationListener mOrientationListener = new IAOrientationListener() {
        @Override
        public void onHeadingChanged(long timestamp, double heading) {
            updateHeading(heading);
        }

        @Override
        public void onOrientationChange(long timestamp, double[] quaternion) {
            // we do not need full device orientation in this example, just the heading
        }
    };

    private void showLocationCircle(LatLng center, double accuracyRadius) {
        if (mCircle == null) {
            // location can received before map is initialized, ignoring those updates
            if (mMap != null) {
                mCircle = mMap.addCircle(new CircleOptions()
                        .center(center)
                        .radius(accuracyRadius)
                        .fillColor(0x201681FB)
                        .strokeColor(0x500A78DD)
                        .zIndex(1.0f)
                        .visible(true)
                        .strokeWidth(5.0f));
                mHeadingMarker = mMap.addMarker(new MarkerOptions()
                        .position(center)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_blue_dot))
                        .anchor(0.5f, 0.5f)
                        .flat(true));
            }
        } else {
            // move existing markers position to received location
            mCircle.setCenter(center);
            mHeadingMarker.setPosition(center);
            mCircle.setRadius(accuracyRadius);
        }
    }


    private void updateHeading(double heading) {
        if (mHeadingMarker != null) {
            mHeadingMarker.setRotation((float) heading);
        }
    }


    private void showMyLocation() {
        if (mCircle != null && mHeadingMarker != null) {
            mCircle.setVisible(true);
            mHeadingMarker.setVisible(true);
        }
    }

    private void hideMyLocation() {
        if (mCircle != null && mHeadingMarker != null) {
            mCircle.setVisible(false);
            mHeadingMarker.setVisible(false);
        }
    }


    private int mFloor;

    /**
     * Listener that handles location change events.
     */
//    private IALocationListener mListener = new IALocationListenerSupport() {
//
//
//
//    };
//
//    /**
//     * Listener that changes overlay if needed
//     */
//    private IARegion.Listener mRegionListener = new IARegion.Listener() {
//
//
//    };
    public void onDialogFloorClick(int which) {
        switch (which) {
            case 0:
                mIALocationManager.unlockFloor();
                mIndoorMapFragmentBinding.floorMaterialButton.setText("A");
//                showMyLocation();
                mIALocationManager.unregisterRegionListener(this);
                startLocationUpdates();
                break;
            case 1:
                mIALocationManager.lockFloor(4);
                mIndoorMapFragmentBinding.floorMaterialButton.setText("4");
//                mIALocationManager.removeLocationUpdates(this);
                mIndoorMapFragmentBinding.poiSearchBarContainer.setClickable(false);
                hideMyLocation();
                break;
            case 2:
                mIALocationManager.lockFloor(3);
                mIndoorMapFragmentBinding.floorMaterialButton.setText("3");
//                mIALocationManager.removeLocationUpdates(this);

                mIndoorMapFragmentBinding.poiSearchBarContainer.setClickable(false);
                hideMyLocation();
                break;
            case 3:
                mIALocationManager.lockFloor(2);
                mIndoorMapFragmentBinding.floorMaterialButton.setText("2");
//                mIALocationManager.removeLocationUpdates(this);

                mIndoorMapFragmentBinding.poiSearchBarContainer.setClickable(false);
                hideMyLocation();
                break;
            case 4:
                mIALocationManager.lockFloor(1);
                mIndoorMapFragmentBinding.floorMaterialButton.setText("1");
                mIndoorMapFragmentBinding.poiSearchBarContainer.setClickable(false);
                hideMyLocation();
                break;
            case 5:
                mIALocationManager.lockFloor(0);
                mIndoorMapFragmentBinding.floorMaterialButton.setText("G");
                mIndoorMapFragmentBinding.poiSearchBarContainer.setClickable(false);
                hideMyLocation();
                break;

        }
    }

    private void startLocationUpdates() {
        // start receiving location updates & monitor region changes
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), this);
        mIALocationManager.registerRegionListener(this);
        mIALocationManager.registerOrientationListener(
                // update if heading changes by 1 degrees or more
                new IAOrientationRequest(1, 0),
                mOrientationListener);

        if (mWayfindingDestination != null) {
            mIALocationManager.requestWayfindingUpdates(mWayfindingDestination, mWayfindingListener);
        }

        showMyLocation();
    }


    private void stopLocationUpdates() {
        // unregister location & region changes
        mIALocationManager.removeLocationUpdates(this);
        mIALocationManager.unregisterRegionListener(this);
        mIALocationManager.unregisterOrientationListener(mOrientationListener);

        if (mWayfindingDestination != null) {
            mIALocationManager.removeWayfindingUpdates();
        }

        hideMyLocation();
    }


    /**
     * Sets bitmap of floor plan as ground overlay on Google Maps
     */
    private void setupGroundOverlay(IAFloorPlan floorPlan, Bitmap bitmap) {

        if (mGroundOverlay != null) {
            mGroundOverlay.remove();
        }

        if (mMap != null) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            IALatLng iaLatLng = floorPlan.getCenter();
            LatLng center = new LatLng(iaLatLng.latitude, iaLatLng.longitude);
            GroundOverlayOptions fpOverlay = new GroundOverlayOptions()
                    .image(bitmapDescriptor)
                    .zIndex(0.0f)
                    .position(center, floorPlan.getWidthMeters(), floorPlan.getHeightMeters())
                    .bearing(floorPlan.getBearing());

            mGroundOverlay = mMap.addGroundOverlay(fpOverlay);
        }
    }

    /**
     * Download floor plan using Picasso library.
     */
    private void fetchFloorPlanBitmap(final IAFloorPlan floorPlan) {

        if (floorPlan == null) {
            Log.e(TAG, "null floor plan in fetchFloorPlanBitmap");
            return;
        }

        final String url = floorPlan.getUrl();
        Log.d(TAG, "loading floor plan bitmap from " + url);

        mLoadTarget = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "onBitmap loaded with dimensions: " + bitmap.getWidth() + "x"
                        + bitmap.getHeight());
                if (mOverlayFloorPlan != null && floorPlan.getId().equals(mOverlayFloorPlan.getId())) {
                    Log.d(TAG, "showing overlay");

                    setupGroundOverlay(floorPlan, bitmap);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                showInfo("Failed to load bitmap");
                mOverlayFloorPlan = null;
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // N/A
            }
        };

        RequestCreator request =
                Picasso.get().load(url);

        final int bitmapWidth = floorPlan.getBitmapWidth();
        final int bitmapHeight = floorPlan.getBitmapHeight();

        if (bitmapHeight > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        } else if (bitmapWidth > MAX_DIMENSION) {
            request.resize(MAX_DIMENSION, 0);
        }

        request.into(mLoadTarget);
    }

    private void showInfo(String text) {
        final Snackbar snackbar = Snackbar.make(rootView, text,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

//    @Override
//    public void onMapClick(LatLng point) {
//        if (mMap != null) {
//
//            mWayfindingDestination = new IAWayfindingRequest.Builder()
//                    .withFloor(mFloor)
//                    .withLatitude(point.latitude)
//                    .withLongitude(point.longitude)
//                    .build();
//
//            mIALocationManager.requestWayfindingUpdates(mWayfindingDestination, mWayfindingListener);
//
//            if (mDestinationMarker == null) {
//                mDestinationMarker = mMap.addMarker(new MarkerOptions()
//                        .position(point)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//            } else {
//                mDestinationMarker.setPosition(point);
//            }
//            Log.d(TAG, "Set destination: (" + mWayfindingDestination.getLat() + ", " +
//                    mWayfindingDestination.getLng() + "), floor=" +
//                    mWayfindingDestination.getFloor());
//        }
//    }

    private boolean hasArrivedToDestination(IARoute route) {
        // empty routes are only returned when there is a problem, for example,
        // missing or disconnected routing graph
        if (route.getLegs().size() == 0) {
            return false;
        }

        final double FINISH_THRESHOLD_METERS = 8.0;
        double routeLength = 0;
        for (IARoute.Leg leg : route.getLegs()) routeLength += leg.getLength();
        return routeLength < FINISH_THRESHOLD_METERS;
    }

    /**
     * Clear the visualizations for the wayfinding paths
     */
    private void clearRouteVisualization() {
        for (Polyline pl : mPolylines) {
            pl.remove();
        }
        mPolylines.clear();
    }

    /**
     * Visualize the IndoorAtlas Wayfinding route on top of the Google Maps.
     */
    private void updateRouteVisualization() {

        clearRouteVisualization();

        if (mCurrentRoute == null) {
            return;
        }

        for (IARoute.Leg leg : mCurrentRoute.getLegs()) {

//            if (leg.getEdgeIndex() == null) {
//                // Legs without an edge index are, in practice, the last and first legs of the
//                // route. They connect the destination or current location to the routing graph.
//                // All other legs travel along the edges of the routing graph.
//
//                // Omitting these "artificial edges" in visualization can improve the aesthetics
//                // of the route. Alternatively, they could be visualized with dashed lines.
//                continue;
//            }

            PolylineOptions opt = new PolylineOptions();
            opt.add(new LatLng(leg.getBegin().getLatitude(), leg.getBegin().getLongitude()));
            opt.add(new LatLng(leg.getEnd().getLatitude(), leg.getEnd().getLongitude()));

            // Here wayfinding path in different floor than current location is visualized in
            // a semi-transparent color
            if (leg.getBegin().getFloor() == mFloor && leg.getEnd().getFloor() == mFloor) {
                opt.color(0xFF0000FF);
            } else {
                opt.color(0x300000FF);
            }

            mPolylines.add(mMap.addPolyline(opt));
        }
    }

    public void onPoiClick(PointOfInterest point) {
        if (mMap != null) {

            mWayfindingDestination = new IAWayfindingRequest.Builder()
                    .withFloor((int) point.getFloor_num())
                    .withLatitude(point.get_geoloc().getLat())
                    .withLongitude(point.get_geoloc().getLng())
                    .build();

            mIALocationManager.requestWayfindingUpdates(mWayfindingDestination, mWayfindingListener);

            if (mDestinationMarker == null) {
                mDestinationMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(point.get_geoloc().getLat(), point.get_geoloc().getLng()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } else {
                mDestinationMarker
                        .setPosition(new LatLng(point.get_geoloc().getLat(), point.get_geoloc().getLng()));
            }
            Log.d(TAG, "Set destination: (" + mWayfindingDestination.getLatitude() + ", " +
                    mWayfindingDestination.getLongitude() + "), floor=" +
                    mWayfindingDestination.getFloor());
        }
    }

    /**
     * Location changed, move marker and camera position.
     */
    @Override
    public void onLocationChanged(IALocation location) {

        Log.d(TAG, "new location received with coordinates: " + location.getLatitude()
                + "," + location.getLongitude());

        if (mMap == null) {
            // location received before map is initialized, ignoring update here
            return;
        }

        final LatLng center = new LatLng(location.getLatitude(), location.getLongitude());
        mUserLocation = center;

        final int newFloor = location.getFloorLevel();
        if (mWayfindingDestination != null && mFloor != newFloor) {
            updateRouteVisualization();
        }
        mFloor = newFloor;

        showLocationCircle(center, location.getAccuracy());

        // our camera position needs updating if location has significantly changed
        if (mCameraPositionNeedsUpdating) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 18.0f));
            mCameraPositionNeedsUpdating = false;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onEnterRegion(IARegion region) {
        if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
            Log.d(TAG, "enter floor plan " + region.getId());
            mCameraPositionNeedsUpdating = true; // entering new fp, need to move camera
            if (mGroundOverlay != null) {
                mGroundOverlay.remove();
                mGroundOverlay = null;
            }
            mOverlayFloorPlan = region; // overlay will be this (unless error in loading)
            fetchFloorPlanBitmap(region.getFloorPlan());
        }
    }

    @Override
    public void onExitRegion(IARegion region) {
    }

    private void clearWayFindingRoute() {

        mCurrentRoute = null;
        mWayfindingDestination = null;
        mDestinationMarker.remove();
        mDestinationMarker = null;
        clearRouteVisualization();
        mIALocationManager.removeWayfindingUpdates();

    }
}
