package com.example.bookacar.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityFogotPassBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ActivityFogotPass extends AppCompatActivity {
    ActivityFogotPassBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fogot_pass);
        binding.btnSend.setOnClickListener(v -> {
            Intent i = new Intent(ActivityFogotPass.this, ActivityFogotV2.class);
            startActivity(i);
        });

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, token);

                Log.d("KMFG", "onCodeSent: " + s);

                verificationId = s;
                forceResendingToken = token;
                progressDialog.dismiss();

                binding.clRegister.setVisibility(View.GONE);
                binding.clVerification.setVisibility(View.VISIBLE);
            }
        };

        binding.btnSend.setOnClickListener(view -> {
            String phone = binding.edtPhone.getText().toString();
            if (!TextUtils.isEmpty(phone)) {
                startPhoneNumberVerification(phone);
            }
        });

        binding.btnSumbit.setOnClickListener(view -> {
            String code = binding.edtCode.getText().toString();
            if (!TextUtils.isEmpty(code)) {
                verifyPhoneNumberWriteCode (verificationId, code);
            }
        });
    }

    private void verifyPhoneNumberWriteCode(String verificationId, String code) {
        progressDialog.setMessage("Verifying Phone Number");
        progressDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential (PhoneAuthCredential credential) {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Intent intent = new Intent(getApplicationContext(), ActivityFogotV2.class);
                        intent.putExtra("Phone", phone);
                        someActivityResultLauncher.launch(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void startPhoneNumberVerification (String phone) {
        progressDialog.setMessage("Verifying Phone Number");
        progressDialog.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerification (String phone, PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setMessage("Resending Code");
        progressDialog.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> finish());
}