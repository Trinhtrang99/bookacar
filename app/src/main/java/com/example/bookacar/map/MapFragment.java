package com.example.bookacar.map;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bookacar.R;
import com.example.bookacar.databinding.FragmentMapBinding;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

import java.io.File;


public class MapFragment extends Fragment {

    FragmentMapBinding binding;
    private Map map = null;
    // we will use this var
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    AndroidXMapFragment mapFragment= null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        final AndroidXMapFragment mapFragment = (AndroidXMapFragment)
                getFragmentManager().findFragmentById(R.id.mapfragment);
        // initialize the Map Fragment and
        // retrieve the map that is associated to the fragment
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // now the map is ready to be used
                    Map map = mapFragment.getMap();
                    // ...
                } else {
                    System.out.println("ERROR: Cannot initialize AndroidXMapFragment");
                }
            }
        });
        return binding.getRoot();
    }




    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initialize() {

        // Search for the map fragment to finish setup by calling init().
        mapFragment = (AndroidXMapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);

        // Set up disk cache path for the map service for this application
        // It is recommended to use a path under your application folder for storing the disk cache
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getContext().getExternalFilesDir(null) + File.separator + ".here-maps");

        if (!success) {
            Toast.makeText(getContext(), "Unable to set isolated disk cache path.", Toast.LENGTH_LONG);
        } else {
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();
                        // Set the map center to the Vancouver region (no animation)
                        map.setCenter(new GeoCoordinate(49.196261, -123.004773, 0.0),
                                Map.Animation.NONE);
                        // Set the zoom level to the average between min and max
                        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                        requestPermissions();
                    } else {
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                    }
                }
            });
        }
    }

    private void requestPermissions() {

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }

//        if (ActivityCompat.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_CODE_LOCATION_PERMISSION
//            );
//        } else {
//            // Get current user location
//            getCurrentUserLocation();
//        }
    }
}