package com.example.bookacar.admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.R;
import com.example.bookacar.admin.model.Confirm;
import com.example.bookacar.databinding.ItemDriverAdminBinding;
import com.example.bookacar.util.BitmapUltil;

import java.util.List;

public class AdaperConfirm extends RecyclerView.Adapter<AdaperConfirm.DriverHolder> {
    private List<Confirm> confirmList;
    private IRecyclerView recyclerView;

    public void setRecyclerView(IRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public AdaperConfirm(List<Confirm> confirmList) {
        this.confirmList = confirmList;
    }

    @Override
    public AdaperConfirm.DriverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDriverAdminBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_driver_admin,
                parent,
                false
        );
        return new DriverHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaperConfirm.DriverHolder holder, int position) {
        holder.binding.idConfirm.setVisibility(View.VISIBLE);
        holder.binding.txtPhone.setText(confirmList.get(position).getPhoneNumber());
        holder.binding.txtName.setText(confirmList.get(position).getName());
        holder.binding.imgAvatar.setImageBitmap(BitmapUltil.getBitmap(confirmList.get(position).getImage()));
        holder.binding.acticity.setVisibility(View.GONE);
        if (!confirmList.get(position).getConfirm()) {
            holder.binding.idConfirm.setEnabled(true);
        } else {
            holder.binding.idConfirm.setEnabled(false);
        }

        holder.binding.idConfirm.setOnClickListener(view -> {
            /*Log.d("KMFG", "OKOKO");*/
            recyclerView.confirmOnClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return confirmList.size();
    }

    public class DriverHolder extends RecyclerView.ViewHolder {
        ItemDriverAdminBinding binding;

        public DriverHolder(@NonNull ItemDriverAdminBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface IRecyclerView {
        void confirmOnClick(Integer position);
    }
}
