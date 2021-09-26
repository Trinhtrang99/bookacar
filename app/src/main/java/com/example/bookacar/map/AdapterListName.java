package com.example.bookacar.map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookacar.R;
import com.example.bookacar.util.PlaceName;

import java.util.List;

public class AdapterListName extends RecyclerView.Adapter<AdapterListName.ViewHolder> {
    List<PlaceName> list;
    onClickItem onclick;

    public AdapterListName(List<PlaceName> list, onClickItem onclick) {
        this.list = list;
        this.onclick = onclick;
    }

    @Override
    public AdapterListName.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemplace, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListName.ViewHolder holder, int position) {
        holder.tv.setText(list.get(position).getName());
        holder.tv.setOnClickListener(v -> {
           onclick.Click(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.name);
        }
    }

    public interface onClickItem {
        void Click(int position);
    }
}
