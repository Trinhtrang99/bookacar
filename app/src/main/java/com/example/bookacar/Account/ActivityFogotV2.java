package com.example.bookacar.Account;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityFogotV2Binding;
import com.example.bookacar.util.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ActivityFogotV2 extends AppCompatActivity {
    ActivityFogotV2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fogot_v2);

        binding.btnChangePassword.setOnClickListener(view -> {
            updateAccount();
        });
    }

    private void updateAccount() {
        binding.progress.setVisibility(View.VISIBLE);
        if (binding.edtPassword.getText().toString().equals(binding.edtPasswordAgain.getText().toString())) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_PHONE_NUMBER, getIntent().getStringExtra("Phone"));
            user.put(Constants.KEY_PASSWORD, binding.edtPassword.getText().toString());
            database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                    .whereEqualTo(Constants.KEY_PHONE_NUMBER, getIntent().getStringExtra("Phone"))
                    .get()
                    .addOnCompleteListener( task -> {
                        binding.progress.setVisibility(View.GONE);
                        if (task.isSuccessful() && task.getResult() != null
                                && task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                            database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                                    .document(documentSnapshot.getId())
                                    .update(user)
                                    .addOnSuccessListener(documentReference -> {
                                        onBackPressed();
                                    });

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Khong dung", Toast.LENGTH_SHORT).show();
        }
    }
}