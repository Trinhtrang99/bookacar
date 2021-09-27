package com.example.bookacar.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.admin.model.Confirm;
import com.example.bookacar.databinding.ActivityConfimDriverBinding;

import java.util.ArrayList;
import java.util.List;

public class ConfimDriverActivity extends AppCompatActivity {
    ActivityConfimDriverBinding binding;
    AdaperConfirm adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confim_driver);
        adapter = new AdaperConfirm(getList());
        binding.rvConfirm.setAdapter(adapter);
    }

    private List<Confirm> getList() {
        List<Confirm> confirmList = new ArrayList<>();
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", false));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", false));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", false));
        confirmList.add(new Confirm("1", "", "Nguyễn văn tuấn", "", "0982653888", true));
        return confirmList;
    }
}