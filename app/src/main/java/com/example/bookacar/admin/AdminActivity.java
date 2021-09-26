package com.example.bookacar.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookacar.Account.Login;
import com.example.bookacar.BaseActivity;
import com.example.bookacar.R;
import com.example.bookacar.admin.model.Driver;
import com.example.bookacar.databinding.ActivityAdminBinding;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends BaseActivity {

    private ActivityAdminBinding binding;
    private DriverAdapter adapter;
    private List<Driver> drivers;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin);

        drivers = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.btnAddDriver.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditActivity.class));
        });

        binding.btnLogout.setOnClickListener(view -> {
            preferenceManager.clear();
            startActivity(new Intent(AdminActivity.this, Login.class));
        });
    }

    private void getFoods () {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .whereEqualTo(Constants.KEY_TYPE_USER, Constants.TYPE_DRIVER)
                .get()
                .addOnCompleteListener(task -> {
                    drivers.clear();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Driver driver = new Driver(
                                queryDocumentSnapshot.getId(),
                                queryDocumentSnapshot.getString(Constants.KEY_IMAGE),
                                queryDocumentSnapshot.getString(Constants.KEY_NAME),
                                queryDocumentSnapshot.getString(Constants.KEY_PASSWORD),
                                queryDocumentSnapshot.getString(Constants.KEY_PHONE_NUMBER)
                        );
                        drivers.add(driver);
                    }

                    adapter = new DriverAdapter(drivers);
                    binding.rvDriver.setAdapter(adapter);

                    showProgressDialog(false);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getFoods();
    }
}