package com.example.bookacar.driver;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityMainDriverBinding;
import com.example.bookacar.home.HomeFragment;
import com.example.bookacar.notication.NoticationFragment;
import com.example.bookacar.person.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainDriver extends AppCompatActivity {
    ActivityMainDriverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_driver);
        loadFragment(new HomeDriverFragment());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(new HomeDriverFragment());
                        backStackFragment(new HomeDriverFragment());
                        return true;
                    case R.id.Map:
                        loadFragment(new NoticationFragment());
                        backStackFragment(new NoticationFragment());
                        return true;
                    case R.id.Acc:
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