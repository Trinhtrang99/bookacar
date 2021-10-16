package com.example.bookacar.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.bookacar.AdapterHistory;
import com.example.bookacar.BaseActivity;
import com.example.bookacar.HistoryModel;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityThuNhapBinding;
import com.example.bookacar.driver.model.UserBook;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityThuNhap extends BaseActivity {
    private ActivityThuNhapBinding binding;
    private List<HistoryModel> historyModels;
    private AdapterHistory adapterHistory;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thu_nhap);

        historyModels = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

        getUserBook();
    }

    private Long totalMoney;
    private void getUserBook () {
        showProgressDialog(true);
        totalMoney = 0L;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_HISTORY)
                .whereEqualTo(Constants.KEY_PHONE_NUMBER, preferenceManager.getString(Constants.KEY_PHONE_NUMBER))
                .get()
                .addOnCompleteListener(task -> {

                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                        HistoryModel historyModel = new HistoryModel(
                          queryDocumentSnapshot.getString(Constants.KEY_DATE),
                          queryDocumentSnapshot.getString(Constants.KEY_LOCATION_START),
                          queryDocumentSnapshot.getString(Constants.KEY_LOCATION_END),
                          queryDocumentSnapshot.getString(Constants.KEY_TOTAL_MONEY)
                        );

                        totalMoney += Long.parseLong(queryDocumentSnapshot.getString(Constants.KEY_TOTAL_MONEY).substring(0, queryDocumentSnapshot.getString(Constants.KEY_TOTAL_MONEY).length() - 4));

                        historyModels.add(historyModel);
                    }

                    binding.txtTotalMoney.setText(totalMoney.toString() + "VND");
                    adapterHistory = new AdapterHistory(historyModels);
                    binding.rcIncome.setAdapter(adapterHistory);

                    showProgressDialog(false);
                });
    }
}