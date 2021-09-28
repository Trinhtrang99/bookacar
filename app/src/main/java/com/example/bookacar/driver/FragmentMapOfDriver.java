package com.example.bookacar.driver;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookacar.R;
import com.example.bookacar.map.GPSLocation;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.ftcr.FTCRRouter;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.FTCRMapRoute;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;

import java.io.File;
import java.io.IOException;

public class FragmentMapOfDriver {
    private AndroidXMapFragment m_mapFragment;
    private AppCompatActivity m_activity;
    private View m_mapFragmentContainer;
    public Map m_map;
    private Button m_calculateRouteButton;
    TextView txtLength;
    public FTCRMapRoute m_mapRoute;



    public FTCRRouter m_router;
    private FTCRRouter.CancellableTask m_routeTask;
    private Long lengthPtP;

    public FragmentMapOfDriver(AppCompatActivity activity) {
        m_activity = activity;
        txtLength = m_activity.findViewById(R.id.txtLength);
        initMapFragment();
    }

    private AndroidXMapFragment getMapFragment() {
        return (AndroidXMapFragment) m_activity.getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initMapFragment() {
        m_mapFragment = getMapFragment();
        m_mapFragmentContainer = m_activity.findViewById(R.id.mapfragment);
        String path = new File(m_activity.getExternalFilesDir(null), ".here-map-data")
                .getAbsolutePath();
        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(path);

        if (m_mapFragment != null) {
            m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == Error.NONE) {
                        m_router = new FTCRRouter();
                        GPSLocation location = new GPSLocation(m_activity);
                        m_map = m_mapFragment.getMap();
                        m_map.setCenter(new GeoCoordinate(location.getLatitude(), location.getLongitude()), Map.Animation.NONE);
                        Image marker_img2 = new Image();
                        try {
                            marker_img2.setImageResource(R.drawable.ma);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MapMarker marker2 = new MapMarker(m_map.getCenter(), marker_img2);
                        marker2.setDraggable(true);
                        m_map.addMapObject(marker2);
                        m_map.setZoomLevel(15);
//                        geoCoordinateStar = new GeoCoordinate(ActivityBookCar.txtDonlat, ActivityBookCar.txtDonLong);
//                        geoCoordinateEnd = new GeoCoordinate(ActivityBookCar.lat, ActivityBookCar.log);

//                        Log.d("kmfg", " đón" + ActivityBookCar.txtDonlat + ActivityBookCar.txtDonLong + " đến" +
//                                ActivityBookCar.lat + ActivityBookCar.log
//                        );
                        //
                    } else {
                        new android.app.AlertDialog.Builder(m_activity).setMessage(
                                "Error : " + error.name() + "\n\n" + error.getDetails())
                                .setTitle(R.string.engine_init_error)
                                .setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                m_activity.finish();
                                            }
                                        }).create().show();
                    }
                }
            });
        }
    }


}
