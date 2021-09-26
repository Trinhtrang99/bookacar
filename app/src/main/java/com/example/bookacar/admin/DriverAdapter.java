package com.example.bookacar.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.R;
import com.example.bookacar.admin.model.Driver;
import com.example.bookacar.databinding.ItemDriverAdminBinding;
import com.example.bookacar.util.BitmapUltil;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverHolder>{

    private List<Driver> drivers;

    public DriverAdapter(List<Driver> drivers) {
        this.drivers = drivers;
    }

    @NonNull
    @Override
    public DriverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDriverAdminBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_driver_admin,
                parent,
                false
        );
        return new DriverHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverHolder holder, int position) {
        holder.binding.txtPhone.setText(drivers.get(position).getPhoneNumber());
        holder.binding.txtName.setText(drivers.get(position).getName());
        holder.binding.imgAvatar.setImageBitmap(BitmapUltil.getBitmap(drivers.get(position).getImage()));
        holder.binding.txtPassword.setText(drivers.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    class DriverHolder extends RecyclerView.ViewHolder {
        ItemDriverAdminBinding binding;
        public DriverHolder(@NonNull ItemDriverAdminBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
