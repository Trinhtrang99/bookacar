package com.example.bookacar.Account;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityRegistionV2Binding;

public class ActivityRegistionV2 extends AppCompatActivity {
    ActivityRegistionV2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registion_v2);
    }
}