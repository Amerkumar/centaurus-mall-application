package app.com.thecentaurusmall.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.Deque;
import java.util.List;

import app.com.thecentaurusmall.MainActivity;
import app.com.thecentaurusmall.R;

public class IndoorMapFragment extends Fragment implements
        OnMapReadyCallback,
        FloorSelectionDialog.FloorSelectDialogListener
{

    /* used to decide when bitmap should be downscaled */
    private static final int MAX_DIMENSION = 2048;


    private static final String TAG = IndoorMapFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private IndoorMapViewModel mViewModel;
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
    private List<Polyline> mPolylines;

    public static IndoorMapFragment newInstance() {
        return new IndoorMapFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // instantiate IALocationManager
        mIALocationManager = IALocationManager.create(getContext());

        // disable indoor-outdoor detection (assume we're indoors)
        mIALocationManager.lockIndoors(true);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.indoor_map_fragment, container, false);
        mapView = rootView.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        floorButton = rootView.findViewById(R.id.floor_material_button);

        floorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FloorSelectionDialog floorSelectionDialog = new FloorSelectionDialog();
////                floorSelectionDialog.setDialogFloorClickListener(this);
//               floorSelectionDialog.show(getActivity().getSupportFragmentManager(), "floors");
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
               .show()
                ;

            }
        });

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.floating_action_button);
        fabFlag = true;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(IndoorMapFragment.class.getSimpleName(), "FAB");
                // if true
                // it means user want to close the location detection
                // enabled by default
                if (fabFlag) {
                    Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_my_location,
                            getActivity().getTheme());
                    Drawable newDrawable = myFabSrc.getConstantState().newDrawable();
                    newDrawable.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                    floatingActionButton.setImageDrawable(newDrawable);
                    stopLocationUpdates();
                    fabFlag = false;
                }
                // if false
                // it means user want to enable the location

                else {
                    Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_my_location,
                            getActivity().getTheme());
                    Drawable newDrawable = myFabSrc.getConstantState().newDrawable();
                    newDrawable.mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    floatingActionButton.setImageDrawable(newDrawable);
                    startLocationUpdates();
                    fabFlag = true;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.d(IndoorMapFragment.class.getSimpleName(), "Permission for Location");
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{

                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE
                        },
                        PERMISSIONS_REQUEST_LOCATION);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(IndoorMapViewModel.class);
        // TODO: Use the ViewModel

//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        mapView.getMapAsync(this);

        if (fabFlag) {
            startLocationUpdates();
        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
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
        ((MainActivity) getActivity()).hideToolbar();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).showToolbar();
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



    private int mFloor;

    /**
     * Listener that handles location change events.
     */
    private IALocationListener mListener = new IALocationListenerSupport() {


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

            final int newFloor = location.getFloorLevel();
            if (mFloor != newFloor) {
                updateRouteVisualization();
            }
            mFloor = newFloor;

            showLocationCircle(center, location.getAccuracy());

            // our camera position needs updating if location has significantly changed
            if (mCameraPositionNeedsUpdating) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 17.5f));
                mCameraPositionNeedsUpdating = false;
            }
        }
    };

    /**
     * Listener that changes overlay if needed
     */
    private IARegion.Listener mRegionListener = new IARegion.Listener() {
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

    };

    public void onDialogFloorClick(int which) {
        switch (which) {
            case 0:
                floorButton.setText("A");
                break;
            case 1:
                floorButton.setText("4");
                break;
            case 2:
                floorButton.setText("3");
                break;
            case 3:
                floorButton.setText("2");
                break;
            case 4:
                floorButton.setText("1");
                break;
            case 5:
                floorButton.setText("G");
                break;

        }
    }

    private void startLocationUpdates() {
        // start receiving location updates & monitor region changes
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
        mIALocationManager.registerOrientationListener(
                // update if heading changes by 1 degrees or more
                new IAOrientationRequest(1, 0),
                mOrientationListener);

        if (mWayfindingDestination != null) {
            mIALocationManager.requestWayfindingUpdates(mWayfindingDestination, mWayfindingListener);
        }
    }


    private void stopLocationUpdates() {
        // unregister location & region changes
        mIALocationManager.removeLocationUpdates(mListener);
        mIALocationManager.unregisterRegionListener(mRegionListener);
        mIALocationManager.unregisterOrientationListener(mOrientationListener);

        if (mWayfindingDestination != null) {
            mIALocationManager.removeWayfindingUpdates();
        }
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
        Log.d(TAG, "loading floor plan bitmap from "+url);

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
        final Snackbar snackbar = Snackbar.make(rootView.findViewById(android.R.id.content), text,
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
//            Log.d(TAG, "Set destination: (" + mWayfindingDestination.getLatitude() + ", " +
//                    mWayfindingDestination.getLongitude() + "), floor=" +
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

            if (leg.getEdgeIndex() == null) {
                // Legs without an edge index are, in practice, the last and first legs of the
                // route. They connect the destination or current location to the routing graph.
                // All other legs travel along the edges of the routing graph.

                // Omitting these "artificial edges" in visualization can improve the aesthetics
                // of the route. Alternatively, they could be visualized with dashed lines.
                continue;
            }

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

}
