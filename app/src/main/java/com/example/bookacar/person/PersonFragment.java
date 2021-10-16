package com.example.bookacar.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bookacar.Account.Login;
import com.example.bookacar.HistoryActivity;
import com.example.bookacar.R;
import com.example.bookacar.admin.ActivityThuNhap;
import com.example.bookacar.databinding.FragmentPersonBinding;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;

public class PersonFragment extends Fragment {



    public PersonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentPersonBinding binding;
    private FirebaseAuth firebaseAuth;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_person, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceManager = new PreferenceManager(getContext());

        binding.txtPhone.setText(preferenceManager.getString(Constants.KEY_PHONE_NUMBER));

        firebaseAuth = FirebaseAuth.getInstance();
        binding.txtLogout.setOnClickListener(view1 -> {
            firebaseAuth.signOut();
            preferenceManager.clear();
            getActivity().finish();
            getActivity().startActivity(new Intent(getContext(), Login.class));
        });
        binding.txtInfo.setOnClickListener(v -> {
            Intent i = new Intent(getContext(),ChangeInfActivity.class);
            //TK DRIVER HOẶC TK USER
            i.putExtra("loaiTK","");
            startActivity(i);

        });
        binding.txtLs.setOnClickListener(view1 -> {
            if (preferenceManager.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_DRIVER)) {
                startActivity(new Intent(getContext(), ActivityThuNhap.class));
            } else {
                startActivity(new Intent(getContext(), HistoryActivity.class));
            }
        });
    }
}