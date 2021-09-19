/*
 * Copyright (c) 2011-2020 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bookacar.map;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookacar.R;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.search.DiscoveryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the properties and functionality of the Map view.It also implements 4
 * types of discovery requests that HERE Android SDK provides as example.
 */
public class MapFragmentView {
    public static List<DiscoveryResult> s_ResultList;
    private AndroidXMapFragment m_mapFragment;
    private AppCompatActivity m_activity;
    private Map m_map;
    private Button m_placeDetailButton;
    private List<MapObject> m_mapObjectList = new ArrayList<>();
    EditText tx;
    TextView txv,txvv;

//    GPSLocation gpsLocation;
//    private MapRoute m_mapRoute;
//    Double b,a;


    public MapFragmentView() {
    }

    public MapFragmentView(AppCompatActivity activity) {
        m_activity = activity;
      //  gpsLocation = new GPSLocation(m_activity);
        initMapFragment();
//        initSearchControlButtons();
//       initResultListButton();

    }

    private AndroidXMapFragment getMapFragment() {
        return (AndroidXMapFragment) m_activity.getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initMapFragment() {
        m_mapFragment = getMapFragment();


        if (m_mapFragment != null) {
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    if (error == Error.NONE) {
                        m_map = m_mapFragment.getMap();

                     //   m_map.setCenter(new GeoCoordinate(gpsLocation.getLatitude(), gpsLocation.getLongitude()), Map.Animation.NONE);

                      //  Image marker_img2 = new Image();
//                        try {
//                        //    marker_img2.setImageResource(R.drawable.ma);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        // create a MapMarker centered at current location with png image.
                      //  MapMarker marker2 = new MapMarker(m_map.getCenter(), marker_img2);
                        /*
                         * Set MapMarker draggable.
                         * How to move to?
                         * In order to activate dragging of the MapMarker you have to do a long press on
                         * the MapMarker then move it to a new position and release the MapMarker.
                         */

                     //   marker2.setDraggable(true);

                        // add a MapMarker to current active map.
                     //   m_map.addMapObject(marker2);
                      //  m_map.setZoomLevel(12);


//                        CoreRouter coreRouter = new CoreRouter();
//
//                        /* Initialize a RoutePlan */
//                        RoutePlan routePlan = new RoutePlan();
//
//                        /*
//                         * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters for the
//                         * route calculation,including transport modes,route types and route restrictions etc.Please
//                         * refer to API doc for full list of APIs
//                         */
//                        RouteOptions routeOptions = new RouteOptions();
//                        /* Other transport modes are also available e.g Pedestrian */
//                        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
//                        /* Disable highway in this route. */
//                        routeOptions.setHighwaysAllowed(false);
//                        /* Calculate the shortest route available. */
//                        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
//                        /* Calculate 1 route. */
//                        routeOptions.setRouteCount(1);
//                        /* Finally set the route option */
//                        routePlan.setRouteOptions(routeOptions);
//
//                        /* Define waypoints for the route */
//                        /* START: 4350 Still Creek Dr */
//
//                        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(gpsLocation.getLatitude(), gpsLocation.getLongitude()));
//                        /* END: Langley BC */
//                        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(20.980007, 105.785921));
//                        /* Add both waypoints to the route plan */
//                        routePlan.addWaypoint(startPoint);
//                        routePlan.addWaypoint(destination);
//
//                        /* Trigger the route calculation,results will be called back via the listener */
//                        coreRouter.calculateRoute(routePlan,
//                                new Router.Listener<List<RouteResult>, RoutingError>() {
//                                    @Override
//                                    public void onProgress(int i) {
//                                        /* The calculation progress can be retrieved in this callback. */
//                                    }
//
//                                    @Override
//                                    public void onCalculateRouteFinished(List<RouteResult> routeResults,
//                                                                         RoutingError routingError) {
//                                        /* Calculation is done. Let's handle the result */
//                                        if (routingError == RoutingError.NONE) {
//                                            m_map.removeMapObject(m_mapRoute);
//                                            if (routeResults.get(0).getRoute() != null) {
//                                                /* Create a MapRoute so that it can be placed on the map */
//                                                m_mapRoute = new MapRoute(routeResults.get(0).getRoute());
//                                                /* Show the maneuver number on top of the route */
//                                                m_mapRoute.setManeuverNumberVisible(true);
//
//                                                /* Add the MapRoute to the map */
//                                                m_map.addMapObject(m_mapRoute);
//
//                                                //-------------------------------------
//                                                String a = String.valueOf(m_mapRoute.getRoute().getLength());
//                                                // createRoute.setText(a);
//                                                //--------------------------------------
//                                                /*
//                                                 * We may also want to make sure the map view is orientated properly
//                                                 * so the entire route can be easily seen.
//                                                 */
//                                                GeoBoundingBox gbb = routeResults.get(0).getRoute()
//                                                        .getBoundingBox();
//                                                m_map.zoomTo(gbb, Map.Animation.NONE,
//                                                        Map.MOVE_PRESERVE_ORIENTATION);
//                                            } else {
//                                                Toast.makeText(m_activity,
//                                                        "Error:route results returned is not valid",
//                                                        Toast.LENGTH_LONG).show();
//                                            }
//                                        } else {
//                                            Toast.makeText(m_activity,
//                                                    "Error:route calculation returned error code: " + routingError,
//                                                    Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });


                    } else {
//                        new AlertDialog.Builder(m_activity).setMessage(
//                                "Error : " + error.name() + "\n\n" + error.getDetails())
//                                .setTitle(R.string.engine_init_error)
//                                .setNegativeButton(android.R.string.cancel,
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                m_activity.finish();
//                                            }
//                                        }).create().show();
                    }
                }
            });
        }
    }

//    private void initResultListButton() {
//        m_placeDetailButton = (Button) m_activity.findViewById(R.id.resultListBtn);
//        m_placeDetailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                /* Open the ResultListActivity */
//                Intent intent = new Intent(m_activity, ResultListActivity.class);
//                m_activity.startActivity(intent);
//
//
//            }
//        });
//    }
//
//    private void initSearchControlButtons() {
//
//        tx = (EditText) m_activity.findViewById(R.id.tx);
//
//        Button searchRequestButton = (Button) m_activity.findViewById(R.id.searchRequestBtn);
//        searchRequestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                cleanMap();
//                final String name = tx.getText().toString();
//                String y = "";
//                if (name.equals(y)) {
//                    SearchRequest searchRequest = new SearchRequest("rạp phim");
//                    searchRequest.setSearchCenter(m_map.getCenter());
//                    searchRequest.execute(discoveryResultPageListener);
//                } else {
//                    SearchRequest searchRequest = new SearchRequest(name);
//                    searchRequest.setSearchCenter(m_map.getCenter());
//                    searchRequest.execute(discoveryResultPageListener);
//                }
//
//
//
//
//
//            }
//        });
//    }
//
//    private ResultListener<DiscoveryResultPage> discoveryResultPageListener = new ResultListener<DiscoveryResultPage>() {
//        @Override
//        public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {
//            if (errorCode == ErrorCode.NONE) {
//                m_placeDetailButton.setVisibility(View.VISIBLE);
//                s_ResultList = discoveryResultPage.getItems();
//                for (DiscoveryResult item : s_ResultList) {
//                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
//                        PlaceLink placeLink = (PlaceLink) item;
//                        addMarkerAtPlace(placeLink);
//                    }
//                }
//            } else {
//                Toast.makeText(m_activity,
//                        "ERROR:Discovery search request returned return error code+ " + errorCode,
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    private void addMarkerAtPlace(PlaceLink placeLink) {
//        Image img = new Image();
//        try {
//            img.setImageResource(R.drawable.marker);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        MapMarker mapMarker = new MapMarker();
//        mapMarker.setIcon(img);
//        mapMarker.setCoordinate(new GeoCoordinate(placeLink.getPosition()));
//        m_map.addMapObject(mapMarker);
//        m_mapObjectList.add(mapMarker);
//    }
//
//    private void cleanMap() {
//        if (!m_mapObjectList.isEmpty()) {
//            m_map.removeMapObjects(m_mapObjectList);
//            m_mapObjectList.clear();
//        }
//        m_placeDetailButton.setVisibility(View.GONE);
//    }
}
