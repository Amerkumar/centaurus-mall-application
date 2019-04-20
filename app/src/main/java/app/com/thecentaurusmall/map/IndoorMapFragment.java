package app.com.thecentaurusmall.map;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

import app.com.thecentaurusmall.MainActivity;
import app.com.thecentaurusmall.R;
import app.com.thecentaurusmall.Utils.Utils;
import io.mapwize.mapwizeformapbox.AccountManager;
import io.mapwize.mapwizeformapbox.api.Venue;
import io.mapwize.mapwizeformapbox.map.MapOptions;
import io.mapwize.mapwizeformapbox.map.MapwizePlugin;
import io.mapwize.mapwizeformapbox.map.MapwizePluginFactory;

public class IndoorMapFragment extends Fragment {

    private IndoorMapViewModel mViewModel;
    private MapView mapView;
    private MapwizePlugin mapwizePlugin;

    public static IndoorMapFragment newInstance() {
        return new IndoorMapFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(getContext(), "pk.mapwize");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.indoor_map_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        MapOptions options = new MapOptions
                .Builder()
                .restrictContentToVenue("5ca37c3cead438003e63efde")
                .centerOnVenue("5ca37c3cead438003e63efde")
                .floor(4.0)
                .build();


        mapwizePlugin = MapwizePluginFactory.create(mapView, options);
        mapwizePlugin.setOnDidLoadListener(mapwizePlugin -> {
            // Mapwize is ready to use
            mapwizePlugin.setBottomPadding((int) Utils.convertDpToPixel(56.0f, getContext()));
            mapwizePlugin.setTopPadding((int) Utils.convertDpToPixel(64.0f, getContext()));

        });


        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle("https://outdoor.mapwize.io/styles/mapwize/style.json?key=" +
                AccountManager.getInstance().getApiKey()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(IndoorMapViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).hideToolbar();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).showToolbar();
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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
