package com.example.bookacar;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.databinding.ItemgiftBinding;

import java.util.List;

public class Adaptergitf extends RecyclerView.Adapter<Adaptergitf.ViewHodel> {
    List<gitfModel> list;

    public Adaptergitf(List<gitfModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemgiftBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.itemgift,
                parent,
                false
        );
        return new ViewHodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptergitf.ViewHodel holder, int position) {
        holder.binding.descrip.setText(list.get(position).description);
        holder.binding.date.setText(list.get(position).data);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHodel extends RecyclerView.ViewHolder {
        ItemgiftBinding binding;

        public ViewHodel(@NonNull ItemgiftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
