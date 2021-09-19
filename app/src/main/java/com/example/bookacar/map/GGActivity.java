package com.example.bookacar.map;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookacar.R;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

public class GGActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ggactivity);

        final AndroidXMapFragment mapFragment = (AndroidXMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapfragment);
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

    }
}