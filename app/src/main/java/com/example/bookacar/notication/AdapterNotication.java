package com.example.bookacar.notication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.R;

import java.util.List;

public class AdapterNotication extends RecyclerView.Adapter<AdapterNotication.ViewHolder> {
    List<Notication> list;

    public AdapterNotication(List<Notication> list) {
        this.list = list;
    }

    @Override
    public AdapterNotication.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_noti, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotication.ViewHolder holder, int position) {
        holder.tvdetail.setText(list.get(position).detail);
        holder.tvtime.setText(list.get(position).time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvdetail, tvtime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvdetail = itemView.findViewById(R.id.title);
            tvtime = itemView.findViewById(R.id.time);
        }
    }
}
