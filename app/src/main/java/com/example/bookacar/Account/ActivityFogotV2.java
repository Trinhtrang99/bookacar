package com.example.bookacar.Account;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityFogotV2Binding;

public class ActivityFogotV2 extends AppCompatActivity {
    ActivityFogotV2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fogot_v2);
    }
}