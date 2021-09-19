package com.example.bookacar.Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityRegistionBinding;

public class ActivityRegistion extends AppCompatActivity {
    ActivityRegistionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registion);
        binding.btnSend.setOnClickListener(v -> {
            Intent i = new Intent(ActivityRegistion.this, ActivityRegistionV2.class);
            startActivity(i);
        });

    }
}