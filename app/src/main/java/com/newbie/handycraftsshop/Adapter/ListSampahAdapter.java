package com.newbie.handycraftsshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.newbie.handycraftsshop.Activity.BuyActivity;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListSampahAdapter extends RecyclerView.Adapter<ListSampahAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<SampahModel> listSampah;

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_sampah, parent, false);
        return new ListViewHolder(view);
    }

    public ListSampahAdapter(Context mContext, ArrayList<SampahModel> listSampah) {
        this.mContext = mContext;
        this.listSampah = listSampah;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBuy = new Intent(mContext, BuyActivity.class);
                mContext.startActivity(toBuy);
            }
        });
        holder.tv_namaBarang.setText(listSampah.get(position).getNama());
//        Picasso.get().load(listSampah.get(position).getImage()).into(holder.imgsampah_home);
        Log.d("Image", listSampah.get(position).getImage());
        holder.imgfavorite_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listSampah.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgsampah_home, imgfavorite_home;
        Button btn_beli;
        TextView tv_namaBarang;
        public ListViewHolder(View itemView) {
            super(itemView);
            imgsampah_home = itemView.findViewById(R.id.img_sampah);
            imgfavorite_home = itemView.findViewById(R.id.img_favorite_home);
            btn_beli = itemView.findViewById(R.id.btn_beli_home);
            tv_namaBarang = itemView.findViewById(R.id.tv_nama_barang);
        }
    }

}
