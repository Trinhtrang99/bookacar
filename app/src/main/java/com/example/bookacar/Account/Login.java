package com.example.bookacar.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.MainActivity;
import com.example.bookacar.R;
import com.example.bookacar.admin.HomAd;
import com.example.bookacar.databinding.ActivityLoginBinding;
import com.example.bookacar.driver.MainDriver;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;
    private boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_REMEMBER_PASSWORD)) {
            binding.edtPhone.setText(preferenceManager.getString(Constants.KEY_PHONE_NUMBER));
            binding.edtPassword.setText(preferenceManager.getString(Constants.KEY_PASSWORD));
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                updateToken(task.getResult());
            });
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
            this.isChecked = isChecked;
        });
    }

    private void updateToken(String fcmToken) {
        binding.progress.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> users = new HashMap<>();
        users.put(Constants.KEY_FCM_TOKEN, fcmToken);

        database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(preferenceManager.getString(Constants.KEY_ID_USER))
                .update(users)
                .addOnCompleteListener(task -> {
                    binding.progress.setVisibility(View.GONE);

                    if (preferenceManager.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_ADMIN)) {
                        Intent intent = new Intent(getApplicationContext(), HomAd.class);
                        startActivity(intent);
                        finish();
                    } else if (preferenceManager.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_DRIVER)) {
                        Intent intent = new Intent(getApplicationContext(), MainDriver.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
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
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_ID_USER, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_FCM_TOKEN, documentSnapshot.getString(Constants.KEY_FCM_TOKEN));

                        if (documentSnapshot.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_ADMIN)) {
                            preferenceManager.putBoolean(Constants.KEY_IS_REMEMBER_PASSWORD, isChecked);
                            Intent intent = new Intent(getApplicationContext(), HomAd.class);
                            startActivity(intent);
                            finish();
                        } else if (documentSnapshot.getString(Constants.KEY_TYPE_USER).equals(Constants.TYPE_DRIVER)) {
                            if (documentSnapshot.getBoolean(Constants.KEY_CONFIRM_USER_DRIVER)) {
                                preferenceManager.putBoolean(Constants.KEY_IS_REMEMBER_PASSWORD, isChecked);
                                Intent intent = new Intent(getApplicationContext(), MainDriver.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Tài khoản chưa được xác nhận", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            preferenceManager.putBoolean(Constants.KEY_IS_REMEMBER_PASSWORD, isChecked);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}