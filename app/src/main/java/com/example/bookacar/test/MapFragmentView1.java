package com.example.bookacar.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookacar.R;
import com.example.bookacar.map.GPSLocation;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.search.AddressFilter;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.AutoSuggestQuery;
import com.here.android.mpa.search.AutoSuggestSearch;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.PlaceRequest;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

public class MapFragmentView1 implements AutoSuggestAdapter.onClickPlace {
    public static List<DiscoveryResult> s_discoverResultList;
    private AndroidXMapFragment m_mapFragment;
    private AppCompatActivity m_activity;
    private View m_mapFragmentContainer;
    private Map m_map;
    private Button m_placeDetailButton;
    private SearchView m_searchView;
    private SearchListener m_searchListener;
    private List<MapObject> m_mapObjectList = new ArrayList<>();
    private AutoSuggestAdapter m_autoSuggestAdapter;
    private List<AutoSuggest> m_autoSuggests;
    private ListView m_resultsListView;
    private TextView m_collectionSizeTextView;
    private LinearLayout m_filterOptionsContainer;
    // private CheckBox m_useFilteringCheckbox;


    public MapFragmentView1(AppCompatActivity activity) {
        m_activity = activity;
        m_searchListener = new SearchListener();
        m_autoSuggests = new ArrayList<>();
        initMapFragment();
        initControls();
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

    public void initControls() {
        m_searchView = m_activity.findViewById(R.id.search);
        m_searchView.setOnQueryTextListener(m_searchListener);
        m_collectionSizeTextView = m_activity.findViewById(R.id.editText_collectionSize);
        m_resultsListView = m_activity.findViewById(R.id.resultsListViev);
        m_autoSuggestAdapter = new AutoSuggestAdapter(m_activity,
                android.R.layout.simple_list_item_1, m_autoSuggests, this);
        m_resultsListView.setAdapter(m_autoSuggestAdapter);
        m_resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoSuggest item = (AutoSuggest) parent.getItemAtPosition(position);
                handleSelectedAutoSuggest(item);
            }
        });
        LinearLayout linearLayout = m_activity.findViewById(R.id.filterOptionsContainer);
        m_filterOptionsContainer = new LinearLayout(m_activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        m_filterOptionsContainer.setVisibility(GONE);
        m_filterOptionsContainer.setOrientation(LinearLayout.VERTICAL);
        m_filterOptionsContainer.setPadding(50, 0, 0, 0);

        TextAutoSuggestionRequest.AutoSuggestFilterType[] filterOptions =
                TextAutoSuggestionRequest.AutoSuggestFilterType.values();
        for (TextAutoSuggestionRequest.AutoSuggestFilterType filterOption : filterOptions) {
            CheckBox curCB = new CheckBox(m_activity);
            curCB.setChecked(false);
            curCB.setText(filterOption.toString());
            m_filterOptionsContainer.addView(curCB);
        }
    }


    private void applyResultFiltersToRequest(TextAutoSuggestionRequest request) {
//        if (m_useFilteringCheckbox.isChecked()) {
//            TextAutoSuggestionRequest.AutoSuggestFilterType[] filterOptions =
//                    TextAutoSuggestionRequest.AutoSuggestFilterType.values();
//            int totalFilterOptionsCount = m_filterOptionsContainer.getChildCount();
//            List<TextAutoSuggestionRequest.AutoSuggestFilterType> filtersToApply =
//                    new ArrayList<>(filterOptions.length);
//            for (int i = 0; i < totalFilterOptionsCount; i++) {
//                if (((CheckBox) m_filterOptionsContainer.getChildAt(i)).isChecked()) {
//                    filtersToApply.add(filterOptions[i]);
//                }
//            }
//            if (!filtersToApply.isEmpty()) {
//                request.setFilters(EnumSet.copyOf(filtersToApply));
//            }
//        }
    }

    private Locale getSelectedLocale() {

        return new Locale("vn-VN");

    }


    @Override
    public void onClickChoose(int position, Double lat, Double log) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", m_autoSuggests.get(position).getTitle());
        resultIntent.putExtra("lat", lat);
        resultIntent.putExtra("long", log);
        Log.d("kmfglat", lat + "ddd" + log);
        m_activity.setResult(1003, resultIntent);
        m_activity.finish();
    }

    private class SearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (!newText.isEmpty()) {
                doSearch(newText);
            } else {
                setSearchMode(false);
            }
            return false;
        }
    }

    private void doSearch(String query) {
        setSearchMode(true);
        TextAutoSuggestionRequest textAutoSuggestionRequest = new TextAutoSuggestionRequest(query);
        textAutoSuggestionRequest.setSearchCenter(m_map.getCenter());
        textAutoSuggestionRequest.setCollectionSize(Integer.parseInt(m_collectionSizeTextView.getText().toString()));
        applyResultFiltersToRequest(textAutoSuggestionRequest);
        Locale locale = getSelectedLocale();
        if (locale != null) {
            textAutoSuggestionRequest.setLocale(locale);
        }

        String countryCode = "VN";
        if (!TextUtils.isEmpty(countryCode)) {
            AddressFilter addressFilter = new AddressFilter();
            // Also available filtering by state code, county, district, city and zip code
            addressFilter.setCountryCode(countryCode);
            textAutoSuggestionRequest.setAddressFilter(addressFilter);
        }

        textAutoSuggestionRequest.execute(new ResultListener<List<AutoSuggest>>() {
            @Override
            public void onCompleted(final List<AutoSuggest> autoSuggests, ErrorCode errorCode) {
                if (errorCode == errorCode.NONE) {
                    processSearchResults(autoSuggests);
                } else {
                    handleError(errorCode);
                }
            }
        });
    }

    private void processSearchResults(final List<AutoSuggest> autoSuggests) {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_autoSuggests.clear();
                m_autoSuggests.addAll(autoSuggests);
                m_autoSuggestAdapter.notifyDataSetChanged();
            }
        });
    }

    private void handleSelectedAutoSuggest(AutoSuggest autoSuggest) {
        int collectionSize = Integer.parseInt(m_collectionSizeTextView.getText().toString());
        switch (autoSuggest.getType()) {
            case PLACE:

                AutoSuggestPlace autoSuggestPlace = (AutoSuggestPlace) autoSuggest;
                PlaceRequest detailsRequest = autoSuggestPlace.getPlaceDetailsRequest();
                detailsRequest.execute(new ResultListener<Place>() {
                    @Override
                    public void onCompleted(Place place, ErrorCode errorCode) {
                        if (errorCode == ErrorCode.NONE) {
                            handlePlace(place);
                        } else {
                            handleError(errorCode);
                        }
                    }
                });
                break;
            case SEARCH:
                AutoSuggestSearch autoSuggestSearch = (AutoSuggestSearch) autoSuggest;
                DiscoveryRequest discoverRequest = autoSuggestSearch.getSuggestedSearchRequest();
                discoverRequest.setCollectionSize(collectionSize);
                discoverRequest.execute(new ResultListener<DiscoveryResultPage>() {
                    @Override
                    public void onCompleted(DiscoveryResultPage discoveryResultPage,
                                            ErrorCode errorCode) {
                        if (errorCode == ErrorCode.NONE) {
                            s_discoverResultList = discoveryResultPage.getItems();
                            Intent intent = new Intent(m_activity, ResultListActivity.class);
                            m_activity.startActivity(intent);
                        } else {
                            handleError(errorCode);
                        }
                    }
                });
                break;
            case QUERY:
                /*
                 Gets TextAutoSuggestionRequest with suggested autocomplete that retrieves
                 the collection of AutoSuggest objects which represent suggested.
                 */
                final AutoSuggestQuery autoSuggestQuery = (AutoSuggestQuery) autoSuggest;
                TextAutoSuggestionRequest queryReqest = autoSuggestQuery
                        .getRequest(getSelectedLocale());
                queryReqest.setCollectionSize(collectionSize);
                queryReqest.execute(new ResultListener<List<AutoSuggest>>() {
                    @Override
                    public void onCompleted(List<AutoSuggest> autoSuggests, ErrorCode errorCode) {
                        if (errorCode == ErrorCode.NONE) {
                            processSearchResults(autoSuggests);
                            m_searchView.setOnQueryTextListener(null);
                            m_searchView.setQuery(autoSuggestQuery.getQueryCompletion(),
                                    false);
                            m_searchView.setOnQueryTextListener(m_searchListener);
                        } else {
                            handleError(errorCode);
                        }
                    }
                });
                break;
            //Do nothing.
            case UNKNOWN:
            default:
        }
    }

    public void setSearchMode(boolean isSearch) {
        if (isSearch) {
            m_mapFragmentContainer.setVisibility(View.INVISIBLE);
            m_resultsListView.setVisibility(View.VISIBLE);
        } else {
            m_mapFragmentContainer.setVisibility(View.VISIBLE);
            m_resultsListView.setVisibility(View.INVISIBLE);
        }
    }

    private void handlePlace(Place place) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(place.getName() + "\n");
        sb.append("Alternative name:").append(place.getAlternativeNames());
        showMessage("Place info", sb.toString(), false);
    }

    private void handleError(ErrorCode errorCode) {
        showMessage("Error", "Error description: " + errorCode.name(), true);
    }

    private void showMessage(String title, String message, boolean isError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(m_activity);
        builder.setTitle(title).setMessage(message);
        if (isError) {
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            builder.setIcon(android.R.drawable.ic_dialog_info);
        }
        builder.setNeutralButton("OK", null);
        builder.create().show();
    }
}