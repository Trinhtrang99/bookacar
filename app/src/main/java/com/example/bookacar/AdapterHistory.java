package com.example.bookacar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.databinding.ItemhistoryBinding;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private List<HistoryModel> list;

    public AdapterHistory(List<HistoryModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemhistoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.itemhistory,
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.ViewHolder holder, int position) {
        holder.binding.tvDate.setText(list.get(position).date);
        holder.binding.txtLocationStart.setText(list.get(position).locationStart);
        holder.binding.txtLocationEnd.setText(list.get(position).locationEnd);
        holder.binding.txtTotalMoney.setText(list.get(position).cost);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemhistoryBinding binding;

        public ViewHolder(@NonNull ItemhistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
