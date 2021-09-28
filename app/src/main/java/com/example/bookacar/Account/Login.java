package com.example.bookacar.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.MainActivity;
import com.example.bookacar.R;
import com.example.bookacar.admin.HomAd;
import com.example.bookacar.databinding.ActivityLoginBinding;
import com.example.bookacar.driver.DriverActivity;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_REMEMBER_PASSWORD)) {
            if (preferenceManager.getString(Constants.KEY_TYPE_USER) != null) {
                if (preferenceManager.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_ADMIN)) {
                    Intent intent = new Intent(getApplicationContext(), HomAd.class);
                    startActivity(intent);
                    finish();
                } else if (preferenceManager.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_DRIVER)) {
                    Intent intent = new Intent(getApplicationContext(), DriverActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }

        binding.forgotBtn.setOnClickListener(v -> {
            Intent i = new Intent(Login.this, ActivityFogotPass.class);
            startActivity(i);
        });
        binding.tvregis.setOnClickListener(v -> {
            Intent i = new Intent(Login.this, ActivityRegistion.class);
            startActivity(i);
        });
        binding.btnlogin.setOnClickListener(v -> {
            signIn();
        });

        binding.chkRememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.putBoolean(Constants.KEY_IS_REMEMBER_PASSWORD, isChecked);
        });
    }

    private void signIn() {
        binding.progress.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .whereEqualTo(Constants.KEY_PHONE_NUMBER, binding.edtPhone.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.edtPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    binding.progress.setVisibility(View.GONE);
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));
                        preferenceManager.putString(Constants.KEY_PASSWORD, documentSnapshot.getString(Constants.KEY_PASSWORD));
                        preferenceManager.putString(Constants.KEY_TYPE_USER, documentSnapshot.getString(Constants.KEY_TYPE_USER));

                        if (documentSnapshot.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_ADMIN)) {
                            Intent intent = new Intent(getApplicationContext(), HomAd.class);
                            startActivity(intent);
                            finish();
                        } else if (documentSnapshot.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_DRIVER)) {
                            Intent intent = new Intent(getApplicationContext(), DriverActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}