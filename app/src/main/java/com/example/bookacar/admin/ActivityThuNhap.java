package com.example.bookacar.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityThuNhapBinding;

public class ActivityThuNhap extends AppCompatActivity {
    ActivityThuNhapBinding binding;
// định dùng chung cái adapterHistory nhưng kb nên thieesy kế như nào có gì thì ib lại cho trang nhé
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thu_nhap);
    }
}