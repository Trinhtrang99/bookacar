package com.example.bookacar.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bookacar.R;
import com.example.bookacar.databinding.FragmentHomeDriverBinding;

public class HomeDriverFragment extends Fragment {
    FragmentHomeDriverBinding binding;

    public HomeDriverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_driver, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cvMotorcycle.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), BookCarActivity.class);
            startActivity(i);
        });
        binding.cvCar.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), ThongKeActivity.class);
            startActivity(i);
        });
    }
}