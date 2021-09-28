package com.example.bookacar.driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.bookacar.Account.Login;
import com.example.bookacar.BaseActivity;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityDriverBinding;
import com.example.bookacar.driver.model.UserBook;
import com.example.bookacar.person.ChangeInfActivity;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends BaseActivity {

    private ActivityDriverBinding binding;
    private List<UserBook> userBooks;
    private UserBookAdapter adapter;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver);

        userBooks = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.btnLogout.setOnClickListener(view -> {
            preferenceManager.clear();
            startActivity(new Intent(this, Login.class));
        });

        binding.btnChangeInfo.setOnClickListener(view -> {
            startActivity(new Intent(this, ChangeInfActivity.class));
        });

        getUserBook();
    }

    private void getUserBook () {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_BOOK)
                .get()
                .addOnCompleteListener(task -> {
                    userBooks.clear();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        UserBook userBook = new UserBook(
                                queryDocumentSnapshot.getId(),
                                queryDocumentSnapshot.getString(Constants.KEY_LOCATION_START),
                                queryDocumentSnapshot.getString(Constants.KEY_LOCATION_END),
                                queryDocumentSnapshot.getString(Constants.KEY_PHONE_NUMBER)
                        );
                        userBooks.add(userBook);
                    }

                    adapter = new UserBookAdapter(userBooks);
                    binding.rvUserBook.setAdapter(adapter);

                    showProgressDialog(false);
                });
    }
}