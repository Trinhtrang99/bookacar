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

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.FragmentBaseDialog;
import com.example.bookacar.MainActivity;
import com.example.bookacar.R;
import com.example.bookacar.util.PlaceName;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.PlaceRequest;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragmentView implements AdapterListName.onClickItem {
    public static List<DiscoveryResult> s_ResultList;
    private AndroidXMapFragment m_mapFragment;
    private AppCompatActivity m_activity;
    private Map m_map;
    private List<MapObject> m_mapObjectList = new ArrayList<>();
    GPSLocation gpsLocation;
    private MapRoute m_mapRoute;
    AlertDialog dialog;
    List<PlaceName> listPlaceName = new ArrayList<>();
    Boolean isClick = false;
    Boolean isDen = false;
    Boolean isDon = false;
    private final FragmentBaseDialog customProgressDialogFragment = new FragmentBaseDialog();

    public MapFragmentView() {
    }

    public void onClickDon() {
        if (MainActivity.don != null) {
            isDon = true;
            SearchRequest searchRequest = new SearchRequest(MainActivity.don);
            searchRequest.setSearchCenter(m_map.getCenter());
            searchRequest.execute(discoveryResultPageListener);
            showProgressDialog(true);
        }

    }

    public void onClickDen() {
        if (MainActivity.txtDen != null) {
            isDen = true;
            SearchRequest searchRequest = new SearchRequest(MainActivity.txtDen);
            searchRequest.setSearchCenter(m_map.getCenter());
            searchRequest.execute(discoveryResultPageListener);
            showProgressDialog(true);
        }

    }

    public MapFragmentView(AppCompatActivity activity) {
        m_activity = activity;
        gpsLocation = new GPSLocation(m_activity);
        initMapFragment();

    }

    private ResultListener<DiscoveryResultPage> discoveryResultPageListener = new ResultListener<DiscoveryResultPage>() {
        @Override
        public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {

                s_ResultList = discoveryResultPage.getItems();

                listPlaceName.clear();
                for (DiscoveryResult item : s_ResultList) {

                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                        PlaceLink placeLink = (PlaceLink) item;
                        //addMarkerAtPlace(placeLink);
                        PlaceRequest placeRequest = placeLink.getDetailsRequest();
                        placeRequest.execute(m_placeResultListener);
                    }

                }

            } else {
                Toast.makeText(m_activity,
                        "ERROR:Discovery search request returned return error code+ " + errorCode,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void addMarkerAtPlace(PlaceLink placeLink) {
        Image img = new Image();
        try {
            img.setImageResource(R.drawable.ma);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapMarker mapMarker = new MapMarker();
        mapMarker.setIcon(img);
        mapMarker.setCoordinate(new GeoCoordinate(placeLink.getPosition()));
        m_map.addMapObject(mapMarker);
        m_mapObjectList.add(mapMarker);
    }

    private void addMark(GeoCoordinate geoCoordinate) {
        Image img = new Image();
        try {
            img.setImageResource(R.drawable.ma);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapMarker mapMarker = new MapMarker();
        mapMarker.setIcon(img);
        mapMarker.setCoordinate(geoCoordinate);
        m_map.addMapObject(mapMarker);
        m_mapObjectList.add(mapMarker);
    }

    private AndroidXMapFragment getMapFragment() {
        return (AndroidXMapFragment) m_activity.getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private PositioningManager positioningManager = null;
    private PositioningManager.OnPositionChangedListener positionListener;
    private GeoCoordinate currentPosition;

    private void initMapFragment() {

        m_mapFragment = getMapFragment();
        if (m_mapFragment != null) {
            m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    if (error == Error.NONE) {
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
                        m_map.setZoomLevel((m_map.getMaxZoomLevel() + m_map.getMinZoomLevel()) / 2);
                        m_map.setZoomLevel(15);

                    } else {
                    }
                }
            });
        }
        cleanMap();
    }


//    public void search(String query) {
//
//        try {
//            DiscoveryRequest request = new SearchRequest(query).setSearchCenter(currentPosition);
//            request.setCollectionSize(5);
//            ErrorCode error = request.execute(new ResultListener<DiscoveryResultPage>() {
//                @Override
//                public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode error) {
//                    if (error != ErrorCode.NONE) {
//                        Log.e("HERE", error.toString());
//                    } else {
//                        for (DiscoveryResult discoveryResult : discoveryResultPage.getItems()) {
//                            if (discoveryResult.getResultType() == DiscoveryResult.ResultType.PLACE) {
//                                PlaceLink placeLink = (PlaceLink) discoveryResult;
////                                MapMarker marker = new MapMarker();
////                                marker.setCoordinate(placeLink.getPosition());
////                                marker.setCoordinate(currentPosition);
////                                m_map.addMapObject(marker);
//                            }
//                        }
//                    }
//                }
//            });
//            if (error != ErrorCode.NONE) {
//                Log.e("HERE", error.toString());
//            }
//        } catch (IllegalArgumentException ex) {
//            Log.e("HERE", ex.getMessage());
//        }
//    }


    private ResultListener<Place> m_placeResultListener = new ResultListener<Place>() {
        @Override
        public void onCompleted(Place place, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
                GeoCoordinate geoCoordinate = place.getLocation().getCoordinate();

                listPlaceName.add(new PlaceName(place.getName(), geoCoordinate.getLatitude(), geoCoordinate.getLongitude()));

                displayAlertDialog();

            } else {
//                Toast.makeText(getApplicationContext(),
//                        "ERROR:Place request returns error: " + errorCode, Toast.LENGTH_SHORT)
//                        .show();
            }

        }
    };


    public void displayAlertDialog() {
        LayoutInflater inflater = m_activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialoglist, null);
        RecyclerView rc = alertLayout.findViewById(R.id.rc);
        AlertDialog.Builder alert = new AlertDialog.Builder(m_activity);
        alert.setView(alertLayout);
        dialog = alert.create();
        AdapterListName adapterListName = new AdapterListName(listPlaceName, this);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(m_activity, 1, RecyclerView.VERTICAL, false);
        rc.setLayoutManager(layoutManager1);
        rc.setAdapter(adapterListName);
        dialog.show();
    }

    @Override
    public void Click(int position) {
        isClick = true;
        if (isDon) {
            MainActivity.setTextForEdt(listPlaceName.get(position).getName());
            isDon = false;
            GeoCoordinate geoCoordinate = new
                    GeoCoordinate(listPlaceName.get(position).getV(), listPlaceName.get(position).getV2());
            m_map.setCenter(geoCoordinate, Map.Animation.BOW);
            addMark(geoCoordinate);
        }
        if (isDen) {
            MainActivity.setTextForDen(listPlaceName.get(position).getName());
            isDen = false;
            GeoCoordinate geoCoordinate = new
                    GeoCoordinate(listPlaceName.get(position).getV(), listPlaceName.get(position).getV2());
            m_map.setCenter(geoCoordinate, Map.Animation.BOW);
            addMark(geoCoordinate);
        }
        dialog.cancel();
        showProgressDialog(false);
    }

    public void showProgressDialog(boolean show) {
        try {
            if (show) {
                if (!customProgressDialogFragment.isShowing()) {
                    customProgressDialogFragment.setShowing(true);
                    customProgressDialogFragment.show(m_activity.getSupportFragmentManager(), "");
                }
            } else {
                if (customProgressDialogFragment.isShowing()) {
                    customProgressDialogFragment.dismiss();
                    customProgressDialogFragment.setShowing(false);
                }
            }
        } catch (Exception ex) {

        }
    }

    public void cleanMap() {
        if (!m_mapObjectList.isEmpty()) {
            m_map.removeMapObjects(m_mapObjectList);
            m_mapObjectList.clear();
        }
    }
    private SearchView m_searchView;
    private void initControls() {
        m_searchView = m_activity.findViewById(R.id.search);
    }

}
