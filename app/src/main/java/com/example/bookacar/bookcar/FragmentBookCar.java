package com.example.bookacar.bookcar;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookacar.R;
import com.example.bookacar.map.GPSLocation;
import com.github.kevinsawicki.http.HttpRequest;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolyline;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.ftcr.FTCRRoute;
import com.here.android.mpa.ftcr.FTCRRouteOptions;
import com.here.android.mpa.ftcr.FTCRRoutePlan;
import com.here.android.mpa.ftcr.FTCRRouter;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.FTCRMapRoute;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapPolyline;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.RoutingError;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.graphics.Color.argb;

public class FragmentBookCar {
    private AndroidXMapFragment m_mapFragment;
    private AppCompatActivity m_activity;
    private View m_mapFragmentContainer;
    public Map m_map;
    private Button m_calculateRouteButton;
    TextView txtLength;
    public FTCRMapRoute m_mapRoute;

    private GeoCoordinate[] m_fakeRoute;
    private GeoCoordinate m_startPoint;
    private GeoCoordinate m_endPoint;

    private static final String URL_UPLOAD_ROUTE =
            "https://fleet.api.here.com/2/overlays/upload.json";
    public static final String OVERLAY_NAME = "OVERLAY-EXAMPLE";

    private String m_appId;
    private String m_appToken;
    private String m_overlayName = OVERLAY_NAME;
    public FTCRRouter m_router;
    private FTCRRouter.CancellableTask m_routeTask;
    private Long lengthPtP;

    public FragmentBookCar(AppCompatActivity activity) {
        m_activity = activity;
        txtLength = m_activity.findViewById(R.id.txtLength);
        try {
            ApplicationInfo app = m_activity.getPackageManager()
                    .getApplicationInfo(m_activity.getPackageName(), PackageManager.GET_META_DATA);
            m_appId = app.metaData.getString("com.here.android.maps.appid");
            m_appToken = app.metaData.getString("com.here.android.maps.apptoken");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            m_activity.finish();
        }
        initMapFragment();
        initCreateRouteButton();
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


    private void initCreateRouteButton() {
        m_calculateRouteButton = (Button) m_activity.findViewById(R.id.btnBook);

//        m_calculateRouteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Log.d("latkmfg", ActivityBookCar.getLat() + "");
//                addFakeRoute();
//                // Log.d("kmfg", "L" + ActivityBookCar.txtDonlat + ActivityBookCar.txtDonLong);
////                geoCoordinateStar = new GeoCoordinate(ActivityBookCar.txtDonlat, ActivityBookCar.txtDonLong);
////                geoCoordinateEnd = new GeoCoordinate(ActivityBookCar.lat, ActivityBookCar.log);
//                if (m_map != null && m_mapRoute != null) {
//                    m_map.removeMapObject(m_mapRoute);
//                    m_mapRoute = null;
//                } else {
//                    calculateRoute();
//                }
//            }
//        });

    }

    private void uploadRouteGeometryOnServer() {

        makeRequest(m_fakeRoute, new Callback() {
            @Override
            public void onResponse(final String message) {
                //   Log.i(TAG, "Response: \n" + message);
                final String userMessage;
                if (message.contains("error")) {
                    userMessage = "Could not upload layer on the server\n" + message;
                } else {
                    userMessage = "Layer successfully uploaded on the server";
                }

                m_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_calculateRouteButton.setEnabled(true);
                    }
                });
            }
        });
    }

    public void calculateRoute() {

        FTCRRouteOptions routeOptions = new FTCRRouteOptions();
        /* Other transport modes are also available e.g Pedestrian */
        routeOptions.setTransportMode(FTCRRouteOptions.TransportMode.CAR);
        /* Calculate the shortest route available. */
        routeOptions.setRouteType(FTCRRouteOptions.Type.SHORTEST);
        /* Define waypoints for the route */
        RouteWaypoint startPoint = new RouteWaypoint(m_startPoint);

        RouteWaypoint destination = new RouteWaypoint(m_endPoint);

        /* Initialize a RoutePlan */
        List<RouteWaypoint> routePoints = new ArrayList<>();
        routePoints.add(startPoint);
        routePoints.add(destination);
        FTCRRoutePlan routePlan = new FTCRRoutePlan(routePoints, routeOptions);

        routePlan.setOverlay(OVERLAY_NAME);
        if (m_routeTask != null) {
            m_routeTask.cancel();
        }

        m_routeTask = m_router.calculateRoute(routePlan, new FTCRRouter.Listener() {
            @Override
            public void onCalculateRouteFinished(@NonNull List<FTCRRoute> routeResults,
                                                 @NonNull FTCRRouter.ErrorResponse errorResponse) {
                if (errorResponse.getErrorCode() == RoutingError.NONE) {
                    lengthPtP = routeResults.get(0).getLength();
                    txtLength.setText(lengthPtP/1000 + " km");

                    if (routeResults.get(0) != null) {
                        /* Create a FTCRMapRoute so that it can be placed on the map */
                        m_mapRoute = new FTCRMapRoute(routeResults.get(0));

                        /* Add the FTCRMapRoute to the map */
                        m_map.addMapObject(m_mapRoute);

                        GeoBoundingBox gbb = routeResults.get(0).getBoundingBox();
                        m_map.zoomTo(gbb, Map.Animation.NONE,
                                Map.MOVE_PRESERVE_ORIENTATION);
                    } else {
                        Toast.makeText(m_activity,
                                "Error:route results returned is not valid",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(m_activity,
                            "Error:route calculation returned error code: "
                                    + errorResponse.getErrorCode()
                                    + ",\nmessage: " + errorResponse.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addFakeRoute(GeoCoordinate m_startPoint, GeoCoordinate m_endPoint) {
        this.m_startPoint = m_startPoint;
        this.m_endPoint = m_endPoint;
        m_fakeRoute = new GeoCoordinate[]
                {
                        m_startPoint,
                        m_endPoint
                };

        uploadRouteGeometryOnServer();
        GeoPolyline fakeRoutePolyLine = new GeoPolyline();
        fakeRoutePolyLine.add(Arrays.asList(m_fakeRoute));
        MapPolyline mapFakeRoutePolyline = new MapPolyline(fakeRoutePolyLine);
        mapFakeRoutePolyline.setLineWidth(15);
        mapFakeRoutePolyline.setLineColor(
                argb(255, 185, 63, 2));
        mapFakeRoutePolyline.setPatternStyle(MapPolyline.PatternStyle.DASH_PATTERN);
        m_map.addMapObject(mapFakeRoutePolyline);

        MapMarker startMapMarker = new MapMarker(m_startPoint);
        MapMarker endMapMarker = new MapMarker(m_endPoint);
        m_map.addMapObject(startMapMarker);
        m_map.addMapObject(endMapMarker);
    }

    public interface Callback {
        void onResponse(String message);
    }

    void makeRequest(GeoCoordinate[] list, final Callback callback) {
        final HttpRequest httpRequest = HttpRequest.post(
                URL_UPLOAD_ROUTE
                        + "?app_id=" + m_appId
                        + "&app_code=" + m_appToken
                        + "&map_name=" + m_overlayName
                        + "&storage=readonly");

        final StringBuilder overlaySpec = new StringBuilder();
        overlaySpec.append("[");
        StringBuilder pathStr = new StringBuilder();
        pathStr.append("[");
        for (GeoCoordinate geoCoordinate : list) {
            pathStr.append("[")
                    .append(geoCoordinate.getLatitude())
                    .append(",")
                    .append(geoCoordinate.getLongitude())
                    .append("]")
                    .append(",");
        }
        pathStr.deleteCharAt(pathStr.length() - 1);
        pathStr.append("]");
        String shape = "{\"op\":\"create\",\"shape\":" + pathStr + "}";
        overlaySpec.append(shape).append(",");
        overlaySpec.deleteCharAt(overlaySpec.length() - 1);
        overlaySpec.append("]");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (FragmentBookCar.this.m_activity.isDestroyed()) {
                    return;
                }

                httpRequest.part("overlay_spec", overlaySpec.toString());
                if (httpRequest.created()) {
                    callback.onResponse(httpRequest.body());
                } else {
                    String message;
                    try {
                        message = "Code:" + httpRequest.code()
                                + ", " + httpRequest.body();
                    } catch (HttpRequest.HttpRequestException e) {
                        message = e.getMessage();
                    }
                    callback.onResponse(message);

                }
            }
        }).start();
    }
}
