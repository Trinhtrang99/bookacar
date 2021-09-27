package com.example.bookacar.admin;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.Account.Login;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityHomAdBinding;

public class HomAd extends AppCompatActivity {
    ActivityHomAdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hom_ad);
        binding.cvCar.setOnClickListener(v -> {
            startActivity(new Intent(this, ConfimDriverActivity.class));
        });
        binding.cvListDriver.setOnClickListener(v -> {
            Intent i = new Intent(this, AdminActivity.class);
            i.putExtra("DRIVER", 1);
            startActivity(i);
        });
        binding.logout.setOnClickListener(view -> {
            startActivity(new Intent(this, Login.class));
        });
        binding.cvUser.setOnClickListener(v -> {
            Intent i = new Intent(this, AdminActivity.class);
            i.putExtra("DRIVER", 0);
            startActivity(i);
        });
    }
}