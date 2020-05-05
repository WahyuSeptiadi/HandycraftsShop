package com.newbie.handycraftsshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by wahyu_septiadi on 04, May 2020.
 * Visit My GitHub --> https://github.com/WahyuSeptiadi
 */

public class ListPostProfileAdapter extends RecyclerView.Adapter<ListPostProfileAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<SampahModel> listSampah;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_sampah_profile, parent, false);
        return new ListViewHolder(view);
    }

    public ListPostProfileAdapter(Context mContext, ArrayList<SampahModel> listSampah) {
        this.mContext = mContext;
        this.listSampah = listSampah;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Glide.with(mContext)
                .load(listSampah.get(position).getImage())
                .into(holder.imgSampah);
    }

    @Override
    public int getItemCount() {
        return listSampah.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSampah, imgFavor, btnEdit, btnDelete;
        TextView like;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSampah = itemView.findViewById(R.id.img_sampahProfile);
            imgFavor = itemView.findViewById(R.id.img_favorite_profile);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            like = itemView.findViewById(R.id.like);
        }
    }
}
