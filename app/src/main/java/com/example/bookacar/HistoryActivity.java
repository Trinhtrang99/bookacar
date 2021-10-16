package com.example.bookacar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.bookacar.databinding.ActivityHistoryBinding;
import com.example.bookacar.databinding.ActivityHomAdBinding;
import com.example.bookacar.driver.UserBookAdapter;
import com.example.bookacar.driver.model.UserBook;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity {
    private ActivityHistoryBinding binding;
    private AdapterHistory adapter;
    private List<HistoryModel> historyModels;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);

        historyModels = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

        getUserBook();
    }

    private void getUserBook() {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_BOOK)
                .whereEqualTo(Constants.KEY_PHONE_NUMBER, preferenceManager.getString(Constants.KEY_PHONE_NUMBER))
                .get()
                .addOnCompleteListener(task -> {
                    historyModels.clear();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        HistoryModel historyModel = new HistoryModel(
                                queryDocumentSnapshot.getString(Constants.KEY_DATE),
                                queryDocumentSnapshot.getString(Constants.KEY_LOCATION_START),
                                queryDocumentSnapshot.getString(Constants.KEY_LOCATION_END),
                                queryDocumentSnapshot.getLong(Constants.KEY_TOTAL_MONEY).toString()
                        );

                        historyModels.add(historyModel);
                    }

                    adapter = new AdapterHistory(historyModels);
                    binding.rc.setAdapter(adapter);

                    showProgressDialog(false);
                });
    }
}