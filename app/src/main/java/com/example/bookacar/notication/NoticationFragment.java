package com.example.bookacar.notication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.BaseFragment;
import com.example.bookacar.R;
import com.example.bookacar.databinding.FragmentNoticationBinding;
import com.example.bookacar.driver.UserBookAdapter;
import com.example.bookacar.driver.model.UserBook;
import com.example.bookacar.map.AdapterListName;
import com.example.bookacar.util.Constants;
import com.example.bookacar.util.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class NoticationFragment extends BaseFragment {

    public NoticationFragment() {

    }

    private FragmentNoticationBinding binding;
    private PreferenceManager preferenceManager;
    private List<Notication> notications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notication, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceManager = new PreferenceManager(getContext());
        notications = new ArrayList<>();

        getUserNotification();
    }

    private void getUserNotification() {
        showProgressDialog(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_ACCOUNT)
                .document(preferenceManager.getString(Constants.KEY_ID_USER))
                .collection(Constants.KEY_COLLECTION_NOTIFICATION)
                .get()
                .addOnCompleteListener(task -> {
                    notications.clear();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Notication notication = new Notication(
                                queryDocumentSnapshot.getString(Constants.KEY_DATE),
                                queryDocumentSnapshot.getString(Constants.KEY_TIME)
                        );

                        notications.add(notication);
                    }

                    AdapterNotication adapterListName = new AdapterNotication(notications);
                    RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
                    binding.rcNotication.setLayoutManager(layoutManager1);
                    binding.rcNotication.setAdapter(adapterListName);

                    showProgressDialog(false);
                });
    }
}