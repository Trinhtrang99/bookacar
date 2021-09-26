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

import com.example.bookacar.R;
import com.example.bookacar.databinding.FragmentNoticationBinding;
import com.example.bookacar.map.AdapterListName;

import java.util.ArrayList;
import java.util.List;


public class NoticationFragment extends Fragment {

    public NoticationFragment() {

    }

    FragmentNoticationBinding binding;

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

        AdapterNotication adapterListName = new AdapterNotication(getList());
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        binding.rcNotication.setLayoutManager(layoutManager1);
        binding.rcNotication.setAdapter(adapterListName);
    }

    private List<Notication> getList() {
        List<Notication> list = new ArrayList<>();
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        list.add(new Notication("Tài xế đã nhận cuốc vào lúc 10:30", "Ngày 22/9/2021 lúc 10:30"));
        list.add(new Notication("Hoàn thành chuyến đi tới Kim Giang", "Ngày 22/9/2021 lúc 11:30"));
        return list;
    }
}