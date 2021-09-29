package com.example.bookacar.admin;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.Account.Login;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityHomAdBinding;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;

public class HomAd extends AppCompatActivity {
    private ActivityHomAdBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hom_ad);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding.cvCar.setOnClickListener(v -> {
            startActivity(new Intent(this, ConfimDriverActivity.class));
        });
        binding.cvListDriver.setOnClickListener(v -> {
            Intent i = new Intent(this, AdminActivity.class);
            i.putExtra(Constants.KEY_TYPE_USER, Constants.INTENT_DRIVER);
            startActivity(i);
        });
        binding.logout.setOnClickListener(view -> {
            preferenceManager.clear();
            startActivity(new Intent(this, Login.class));
        });
        binding.cvUser.setOnClickListener(v -> {
            Intent i = new Intent(this, AdminActivity.class);
            i.putExtra(Constants.KEY_TYPE_USER, Constants.INTENT_USER);
            startActivity(i);
        });
    }
}