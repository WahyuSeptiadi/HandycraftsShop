package com.newbie.handycraftsshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newbie.handycraftsshop.R;

public class ListSampahAdapter extends RecyclerView.Adapter<ListSampahAdapter.ListViewHolder> {
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_sampah, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgsampah_home, imgfavorite_home;
        Button btn_beli;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgsampah_home = itemView.findViewById(R.id.img_sampah);
            imgfavorite_home = itemView.findViewById(R.id.img_favorite_home);
            btn_beli = itemView.findViewById(R.id.btn_beli_home);
        }
    }
}
