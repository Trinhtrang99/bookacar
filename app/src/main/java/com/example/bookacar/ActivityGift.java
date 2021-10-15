package com.example.bookacar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.bookacar.databinding.ActivityGiftBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityGift extends AppCompatActivity {
    ActivityGiftBinding binding;
    Adaptergitf adaptergitf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift);
        adaptergitf = new Adaptergitf(getList());
        binding.rc.setAdapter(adaptergitf);
    }

    public static List<gitfModel> getList() {
        List<gitfModel> list = new ArrayList<>();
        list.add(new gitfModel("ưu đãi 10.0000đ cho bạn", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 20.0000đ cho bạn", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 40.0000đ cho bạn", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 70.0000đ cho bạn cho chuyến từ 300.000đ", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 10.0000đ cho bạn", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 10.0000đ cho bạn", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 10.0000đ cho bạn", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 10.0000đ cho bạn", "10/12/2021"));
        list.add(new gitfModel("ưu đãi 10.0000đ cho bạn", "10/12/2021"));

        return list;
    }

}