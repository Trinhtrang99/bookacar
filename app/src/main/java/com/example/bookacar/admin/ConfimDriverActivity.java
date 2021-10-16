package com.example.bookacar.admin;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.bookacar.BaseActivity;
import com.example.bookacar.R;
import com.example.bookacar.admin.model.Confirm;
import com.example.bookacar.databinding.ActivityConfimDriverBinding;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfimDriverActivity extends BaseActivity implements AdaperConfirm.IRecyclerView {
    private ActivityConfimDriverBinding binding;
    private AdaperConfirm adapter;
    private List<Confirm> confirmList;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confim_driver);
        preferenceManager = new PreferenceManager(getApplicationContext());

        confirmList = new ArrayList<>();
        getConfirm();
    }

    private void getConfirm () {
        showProgressDialog(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .whereEqualTo(Constants.KEY_CONFIRM_USER_DRIVER, false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Confirm confirm = new Confirm(
                                    queryDocumentSnapshot.getId(),
                                    queryDocumentSnapshot.getString(Constants.KEY_IMAGE),
                                    queryDocumentSnapshot.getString(Constants.KEY_NAME),
                                    queryDocumentSnapshot.getString(Constants.KEY_PASSWORD),
                                    queryDocumentSnapshot.getString(Constants.KEY_PHONE_NUMBER),
                                    queryDocumentSnapshot.getBoolean(Constants.KEY_CONFIRM_USER_DRIVER)
                            );

                            confirmList.add(confirm);
                        }

                        adapter = new AdaperConfirm(confirmList);
                        adapter.setRecyclerView(this);
                        binding.rvConfirm.setAdapter(adapter);
                    }

                    showProgressDialog(false);
                });
    }

    @Override
    public void confirmOnClick(Integer position) {
        updateAccount(position);
    }

    private void updateAccount(Integer position) {
        showProgressDialog(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_CONFIRM_USER_DRIVER, true);
        database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(confirmList.get(position).getId())
                .update(user)
                .addOnSuccessListener(documentReference -> {
                    confirmList.get(position).setConfirm(true);
                    adapter.notifyDataSetChanged();
                    showProgressDialog(false);
                });
    }
}