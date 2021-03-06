package com.example.bookacar.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bookacar.ActivityGift;
import com.example.bookacar.HistoryActivity;
import com.example.bookacar.R;
import com.example.bookacar.bookcar.ActivityBookCar;
import com.example.bookacar.databinding.FragmentHomeBinding;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private PreferenceManager preferenceManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());

        binding.txtPhone.setText(preferenceManager.getString(Constants.KEY_PHONE_NUMBER));

        binding.cvCar.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ActivityBookCar.class);
            intent.putExtra(Constants.KEY_TYPE_BOOK, Constants.INTENT_CAR);
            intent.putExtra("Cost", 20000);
            startActivity(intent);
        });

        binding.cvMotorcycle.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ActivityBookCar.class);
            intent.putExtra(Constants.KEY_TYPE_BOOK, Constants.INTENT_MOTO);
            intent.putExtra("Cost", 30000);
            startActivity(intent);
        });

        binding.btHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HistoryActivity.class));
        });
        binding.gift.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ActivityGift.class));
        });
    }
}