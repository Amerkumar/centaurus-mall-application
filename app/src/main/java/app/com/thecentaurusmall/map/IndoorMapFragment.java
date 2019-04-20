package app.com.thecentaurusmall.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IAOrientationRequest;

import app.com.thecentaurusmall.MainActivity;
import app.com.thecentaurusmall.R;

public class IndoorMapFragment extends Fragment implements
        OnMapReadyCallback,
        FloorSelectionDialog.FloorSelectDialogListener
{

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private IndoorMapViewModel mViewModel;
    private GoogleMap mMap;
    private MapView mapView;
    private Circle mCircle;
    private Marker mHeadingMarker;
    private IALocationManager mIALocationManager;
    private boolean fabFlag;
    private MaterialButton floorButton;

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
        View rootView = inflater.inflate(R.layout.indoor_map_fragment, container, false);
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
}
