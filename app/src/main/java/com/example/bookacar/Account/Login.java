package com.example.bookacar.Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.MainActivity;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.forgotBtn.setOnClickListener(v -> {
            Intent i = new Intent(Login.this, ActivityFogotPass.class);
            startActivity(i);
        });
        binding.tvregis.setOnClickListener(v -> {
            Intent i = new Intent(Login.this, ActivityRegistion.class);
            startActivity(i);
        });
        binding.btnlogin.setOnClickListener(v -> {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
        });
    }

}