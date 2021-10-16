package com.example.bookacar.driver;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.bookacar.BaseActivity;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityThongKeBinding;
import com.example.bookacar.driver.model.UserBook;
import com.example.bookacar.firebase.network.NotificationApi;
import com.example.bookacar.firebase.network.RetrofitInstance;
import com.example.bookacar.firebase.network.response.NotificationData;
import com.example.bookacar.firebase.network.response.PushNotification;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongKeActivity extends BaseActivity implements UserBookAdapter.IRecyclerViewOnClick {

    private ActivityThongKeBinding binding;
    private List<UserBook> userBooks;
    private UserBookAdapter adapter;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thong_ke);
        userBooks = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding.imgback.setOnClickListener(v -> {
            finish();
        });
        getUserBook();
    }

    private void getUserBook() {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_BOOK)
                .whereEqualTo(Constants.KEY_IS_BOOK, false)
                .get()
                .addOnCompleteListener(task -> {
                    userBooks.clear();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        UserBook userBook = new UserBook(
                                queryDocumentSnapshot.getId(),
                                queryDocumentSnapshot.getString(Constants.KEY_NAME),
                                queryDocumentSnapshot.getString(Constants.KEY_LOCATION_START),
                                queryDocumentSnapshot.getString(Constants.KEY_LOCATION_END),
                                queryDocumentSnapshot.getString(Constants.KEY_PHONE_NUMBER),
                                queryDocumentSnapshot.getLong(Constants.KEY_TOTAL_MONEY) + " VND"
                        );
                        userBook.setTypeBook(queryDocumentSnapshot.getString(Constants.KEY_TYPE_BOOK));

                        userBooks.add(userBook);
                    }

                    adapter = new UserBookAdapter(userBooks);
                    adapter.setRecyclerViewOnClick(this::confirmOnClick);
                    binding.recyclerView.setAdapter(adapter);

                    showProgressDialog(false);
                });
    }

    @Override
    public void confirmOnClick(int position) {
        addBook(position);
        addNotification(position);
    }

    private void addBook(int position) {
        UserBook userBook = userBooks.get(position);
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> confirmBooks = new HashMap<>();
        confirmBooks.put(Constants.KEY_NAME, userBook.getName());
        confirmBooks.put(Constants.KEY_LOCATION_START, userBook.getLocationStart());
        confirmBooks.put(Constants.KEY_LOCATION_END, userBook.getLocationEnd());
        confirmBooks.put(Constants.KEY_PHONE_NUMBER, userBook.getPhoneNumber());
        confirmBooks.put(Constants.KEY_TOTAL_MONEY, userBook.getTotalMoney());
        db.collection(Constants.KEY_COLLECTION_CONFIRM_BOOK)
                .document(preferenceManager.getString(Constants.KEY_ID_USER))
                .set(confirmBooks)
                .addOnSuccessListener(documentReference -> {
                    userBooks.remove(position);
                    adapter.update(userBooks);

                    findUser(userBook.getPhoneNumber());

                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    showProgressDialog(false);

                    db.collection(Constants.KEY_COLLECTION_BOOK)
                            .document(userBook.getId()).update(Constants.KEY_IS_BOOK, true);
                });
    }

    private void findUser(String phone) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .whereEqualTo(Constants.KEY_PHONE_NUMBER, phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        PushNotification pushNotification = new PushNotification(
                                new NotificationData("Tài xế", " Đang đến chỗ bạn"),
                                documentSnapshot.getString(Constants.KEY_FCM_TOKEN)
                        );

                        sendNotification(pushNotification);
                    }
                });
    }

    private void sendNotification(PushNotification pushNotification) {
        RetrofitInstance.getRetrofit().create(NotificationApi.class)
                .postNotification(pushNotification)
                .enqueue(new Callback<PushNotification>() {
                    @Override
                    public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ThongKeActivity.this, "OK", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PushNotification> call, Throwable t) {
                        Log.d("KMFG", t.getMessage());
                    }
                });
    }

    private void addNotification(int position) {
        UserBook userBook = userBooks.get(position);
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> notifications = new HashMap<>();
        notifications.put(Constants.KEY_DATE, new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        notifications.put(Constants.KEY_TIME, new SimpleDateFormat("HH:mm").format(new Date()));

        Toast.makeText(ThongKeActivity.this, userBook.getId() + "==", Toast.LENGTH_SHORT).show();
        db.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(userBook.getId())
                .collection(Constants.KEY_COLLECTION_NOTIFICATION)
                .add(notifications)
                .addOnSuccessListener(documentReference -> {

                });
    }
}