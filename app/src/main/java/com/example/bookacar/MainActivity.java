package com.example.bookacar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookacar.databinding.ActivityMainBinding;
import com.example.bookacar.home.HomeFragment;
import com.example.bookacar.map.MapFragment;
import com.example.bookacar.map.MapFragmentView;
import com.example.bookacar.person.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MapFragmentView fragmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loadFragment(new HomeFragment());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(new HomeFragment());
                        backStackFragment(new HomeFragment());
                        binding.map.setVisibility(View.GONE);
                        return true;
                    case R.id.Map:
                        binding.map.setVisibility(View.VISIBLE);
                        fragmentView = new MapFragmentView(MainActivity.this);
                        return true;
                    case R.id.Acc:
                        binding.map.setVisibility(View.GONE);
                        loadFragment(new PersonFragment());
                        backStackFragment(new PersonFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void backStackFragment(Fragment fragment) {
        fragment = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frame_layout, fragment, fragment.getClass().getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


}