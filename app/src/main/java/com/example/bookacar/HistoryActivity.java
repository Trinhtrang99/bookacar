package com.example.bookacar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.bookacar.databinding.ActivityHomAdBinding;

public class HistoryActivity extends AppCompatActivity {
    ActivityHomAdBinding binding;
    AdapterHistory adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
    }
}