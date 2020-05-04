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

import com.bumptech.glide.Glide;
import com.newbie.handycraftsshop.Activity.BuyActivity;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        holder.tv_namaBarang.setText(listSampah.get(position).getNama());
        holder.tv_usernamepublisher.setText(listSampah.get(position).getUsernamepublisher());
        Glide.with(mContext)
             .load(listSampah.get(position).getImage())
             .into(holder.imgsampah_home);
        Glide.with(mContext)
                .load(listSampah.get(position).getImagepublisher())
                .into(holder.civ_imgpublisher);

//        Picasso.get().load(listSampah.get(position).getImage()).into(holder.imgsampah_home);
//        Log.d("Image", listSampah.get(position).getImage());

        holder.btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBuy = new Intent(mContext, BuyActivity.class);
                mContext.startActivity(toBuy);
            }
        });

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
        CircleImageView civ_imgpublisher;
        Button btn_beli;
        TextView tv_namaBarang, tv_usernamepublisher;
        public ListViewHolder(View itemView) {
            super(itemView);
            imgsampah_home = itemView.findViewById(R.id.imgSampahPost);
            imgfavorite_home = itemView.findViewById(R.id.img_favorite_home);
            btn_beli = itemView.findViewById(R.id.btn_beli_home);
            tv_namaBarang = itemView.findViewById(R.id.tv_nama_barang);
            civ_imgpublisher = itemView.findViewById(R.id.civ_imagePublisher);
            tv_usernamepublisher = itemView.findViewById(R.id.tv_usernamepostinganHome);

        }
    }

}
