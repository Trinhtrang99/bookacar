package com.example.bookacar.driver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.R;
import com.example.bookacar.databinding.ItemUserBookBinding;
import com.example.bookacar.driver.model.UserBook;

import java.util.List;

public class UserBookAdapter extends RecyclerView.Adapter<UserBookAdapter.UserBookHolder>{

    private List<UserBook> userBooks;

    public UserBookAdapter(List<UserBook> userBooks) {
        this.userBooks = userBooks;
    }

    @NonNull
    @Override
    public UserBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBookBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_book,
                parent,
                false
        );

        return new UserBookHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserBookHolder holder, int position) {
        holder.binding.txtLocationStart.setText("Bắt đầu: " + userBooks.get(position).getLocationStart());
        holder.binding.txtLocationEnd.setText("Điểm đến: " + userBooks.get(position).getLocationEnd());
        holder.binding.txtLocationPhone.setText("SĐT: " + userBooks.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return userBooks.size();
    }

    class UserBookHolder extends RecyclerView.ViewHolder {
        ItemUserBookBinding binding;
        public UserBookHolder(@NonNull ItemUserBookBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
