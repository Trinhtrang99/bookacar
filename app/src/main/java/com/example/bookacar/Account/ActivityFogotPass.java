package com.example.bookacar.Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityFogotPassBinding;

public class ActivityFogotPass extends AppCompatActivity {
    ActivityFogotPassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fogot_pass);
        binding.btnSend.setOnClickListener(v -> {
            Intent i = new Intent(ActivityFogotPass.this, ActivityFogotV2.class);
            startActivity(i);
        });
    }
}