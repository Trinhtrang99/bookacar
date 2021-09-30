package com.example.bookacar.driver;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bookacar.BaseActivity;
import com.example.bookacar.R;
import com.example.bookacar.databinding.ActivityThongKeBinding;
import com.example.bookacar.driver.model.UserBook;
import com.example.bookacar.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends BaseActivity implements UserBookAdapter.IRecyclerViewOnClick {

    private ActivityThongKeBinding binding;
    private List<UserBook> userBooks;
    private UserBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thong_ke);
        userBooks = new ArrayList<>();

        getUserBook();

    }

    private void getUserBook () {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_BOOK)
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

                        userBooks.add(userBook);
                    }

                    adapter = new UserBookAdapter(userBooks);
                    adapter.setRecyclerViewOnClick(this::confirmOnClick);
                    binding.recyclerView.setAdapter(adapter);

                    showProgressDialog(false);
                });
    }

    @Override
    public void confirmOnClick(Integer position) {
        Toast.makeText(ThongKeActivity.this, position + "===", Toast.LENGTH_SHORT).show();
    }
}