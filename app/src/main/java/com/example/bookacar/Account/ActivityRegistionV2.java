package com.example.bookacar.Account;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityRegistionV2Binding;
import com.example.bookacar.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ActivityRegistionV2 extends AppCompatActivity {
    ActivityRegistionV2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registion_v2);

        binding.btnRegister.setOnClickListener(view -> {
            registerAccount();
        });
    }

    private void registerAccount () {
        String typeUser = getIntent().getStringExtra("typeUser");
        binding.progress.setVisibility(View.VISIBLE);
        if (binding.edtPassword.getText().toString().equals(binding.edtPasswordAgain.getText().toString())) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_PHONE_NUMBER, getIntent().getStringExtra("Phone"));
            user.put(Constants.KEY_PASSWORD, binding.edtPassword.getText().toString());
            user.put(Constants.KEY_TYPE_USER, typeUser);
            user.put(Constants.KEY_NAME, binding.edtName.getText().toString());

            if (typeUser.equals(Constants.TYPE_DRIVER)) {
                user.put(Constants.KEY_CONFIRM_USER_DRIVER, false);
            }
            database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        binding.progress.setVisibility(View.GONE);
                        onBackPressed();
                    });
        } else {
            Toast.makeText(getApplicationContext(), "khong chinh xac", Toast.LENGTH_SHORT).show();
        }
    }
}