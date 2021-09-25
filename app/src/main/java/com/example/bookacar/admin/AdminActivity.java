package com.example.bookacar.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin);

        binding.btnAddDriver.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditActivity.class));
        });
    }
}