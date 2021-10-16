package com.example.bookacar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bookacar.databinding.ActivityBookBinding;
import com.example.bookacar.databinding.ActivityEditBinding;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BookActivity extends BaseActivity {

    private ActivityBookBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book);
        preferenceManager = new PreferenceManager(getApplicationContext());

        /*binding.btnBook.setOnClickListener(view -> {
            addBook();
        });*/
    }
/*
    private void addBook () {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> books = new HashMap<>();
        books.put(Constants.KEY_LOCATION_START, binding.edtLocationStart.getText().toString());
        books.put(Constants.KEY_LOCATION_END, binding.edtLocationEnd.getText().toString());
        books.put(Constants.KEY_PHONE_NUMBER, preferenceManager.getString(Constants.KEY_PHONE_NUMBER));
        db.collection(Constants.KEY_COLLECTION_BOOK)
                .add(books)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    showProgressDialog(false);
                    finish();
                });
    }*/
}