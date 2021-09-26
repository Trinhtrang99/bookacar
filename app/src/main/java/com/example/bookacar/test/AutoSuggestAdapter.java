package com.example.bookacar.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.example.bookacar.R;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.AutoSuggestQuery;
import com.here.android.mpa.search.AutoSuggestSearch;

import java.util.List;

public class AutoSuggestAdapter extends ArrayAdapter<AutoSuggest> {
    private List<AutoSuggest> m_resultsList;
    private onClickPlace onClickPlace;
    private Double txtLat;
    private Double txtLong;

    public AutoSuggestAdapter(Context context, int resource, List<AutoSuggest>
            objects, onClickPlace onClickPlace) {
        super(context, resource, objects);
        m_resultsList = objects;
        this.onClickPlace = onClickPlace;
    }

    @Override
    public int getCount() {
        return m_resultsList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AutoSuggest autoSuggest = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_autosuggest_list_item,
                    parent, false);
        }
        LinearLayout linearLayout = convertView.findViewById(R.id.ln);
        linearLayout.setOnClickListener(v -> {
            onClickPlace.onClickChoose(position,txtLat,txtLong );
        });
        TextView tv = null;
//        tv = convertView.findViewById(R.id.header);
//        tv.setBackgroundColor(Color.DKGRAY);

        // set title
        tv = convertView.findViewById(R.id.title);
        tv.setText(autoSuggest.getTitle());

        // set highlightedTitle
        tv = convertView.findViewById(R.id.highlightedTitle);
        tv.setText(HtmlCompat.fromHtml(autoSuggest.getHighlightedTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        // set request URL
        tv = convertView.findViewById(R.id.url);
        tv.setText("Url: " + autoSuggest.getUrl());

        // set Type
        tv = convertView.findViewById(R.id.type);
        tv.setText("Type: " + autoSuggest.getType().name());

        switch (autoSuggest.getType()) {
            case PLACE:
                AutoSuggestPlace autoSuggestPlace = (AutoSuggestPlace) autoSuggest;
                // set vicinity
                tv = convertView.findViewById(R.id.vicinity);
                //     tv.setVisibility(View.VISIBLE);
                if (autoSuggestPlace.getVicinity() != null) {
                    tv.setText("Vicinity: " + autoSuggestPlace.getVicinity());
                } else {
                    tv.setText("Vicinity: nil");
                }

                // set category
                tv = convertView.findViewById(R.id.category);
                //    tv.setVisibility(View.VISIBLE);
                if (autoSuggestPlace.getCategory() != null) {
                    tv.setText("Category: " + autoSuggestPlace.getCategory());
                } else {
                    tv.setText("Category: nil");
                }

                // set position
                tv = convertView.findViewById(R.id.position);
                //  tv.setVisibility(View.VISIBLE);
                if (autoSuggestPlace.getPosition() != null) {
                    tv.setText("Position: " + autoSuggestPlace.getPosition().toString());
                    txtLat = autoSuggestPlace.getPosition().getLatitude();
                    txtLong = autoSuggestPlace.getPosition().getLongitude();
                } else {
                    tv.setText("Position: nil");
                }

                // set boundaryBox
                tv = convertView.findViewById(R.id.boundaryBox);
                // tv.setVisibility(View.VISIBLE);
                if (autoSuggestPlace.getBoundingBox() != null) {
                    tv.setText("BoundaryBox: " + ((AutoSuggestPlace) autoSuggest).getBoundingBox().toString());
                } else {
                    tv.setText("BoundaryBox: nil");
                }
                break;
            case QUERY:
                AutoSuggestQuery autoSuggestQuery = (AutoSuggestQuery) autoSuggest;
                // set completion
                tv = convertView.findViewById(R.id.vicinity);
                tv.setText("Completion: " + autoSuggestQuery.getQueryCompletion());

                // set category
                tv = convertView.findViewById(R.id.category);
                tv.setVisibility(View.GONE);

                // set position
                tv = convertView.findViewById(R.id.position);
                tv.setVisibility(View.GONE);

                // set boundaryBox
                tv = convertView.findViewById(R.id.boundaryBox);
                tv.setVisibility(View.GONE);
                break;
            case SEARCH:
                AutoSuggestSearch autoSuggestSearch = (AutoSuggestSearch) autoSuggest;
                // set vicinity
                tv = convertView.findViewById(R.id.vicinity);
                tv.setVisibility(View.GONE);

                // set category
                tv = convertView.findViewById(R.id.category);
                //  tv.setVisibility(View.VISIBLE);
                if (autoSuggestSearch.getCategory() != null) {
                    tv.setText("Category: " + autoSuggestSearch.getCategory());
                } else {
                    tv.setText("Category: nil");
                }

                // set position
                tv = convertView.findViewById(R.id.position);
                //   tv.setVisibility(View.VISIBLE);
                if (autoSuggestSearch.getPosition() != null) {
                    tv.setText("Position: " + autoSuggestSearch.getPosition().toString());
                } else {
                    tv.setText("Position: nil");
                }

                // set boundaryBox
                tv = convertView.findViewById(R.id.boundaryBox);
                // tv.setVisibility(View.VISIBLE);
                if (autoSuggestSearch.getBoundingBox() != null) {
                    tv.setText("BoundaryBox: " + autoSuggestSearch.getBoundingBox().toString());
                } else {
                    tv.setText("BoundaryBox: nil");
                }
                break;
            default:
        }
        return convertView;
    }

    @Override
    public AutoSuggest getItem(int position) {
        return m_resultsList.get(position);
    }

    interface onClickPlace {
        void onClickChoose(int position, Double lat,Double log);
    }
}