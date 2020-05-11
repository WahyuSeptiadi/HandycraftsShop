package com.newbie.handycraftsshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.newbie.handycraftsshop.Model.HistoryModel;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.Wishlist;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ListViewHolder> {
    private ArrayList<HistoryModel> listHistory;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private Wishlist wishlist;
    private Context mContext;
    private String mUser;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_history, parent, false);
        return new ListViewHolder(view);
    }

    public HistoryAdapter(Context mContext, ArrayList<HistoryModel> listHistory) {
        this.mContext = mContext;
        this.listHistory = listHistory;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        holder.tv_namaBarang.setText(String.valueOf(listHistory.get(position).getNama_barang()));
        holder.tv_hargaBarang.setText(String.valueOf(listHistory.get(position).getHarga_barang()));
        holder.tv_banyakBarang.setText(String.valueOf(listHistory.get(position).getBanyak_barang()));
        holder.tv_totalHarga.setText(String.valueOf(listHistory.get(position).getHargaTotal()));
        Glide.with(mContext)
                .load(listHistory.get(position).getImagesampah())
                .into(holder.gambar_barang);
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        CircleImageView gambar_barang;
        TextView tv_namaBarang, tv_banyakBarang, tv_hargaBarang, tv_totalHarga;
        public ListViewHolder(View itemView) {
            super(itemView);
            gambar_barang = itemView.findViewById(R.id.civ_gambar_barang);
            tv_namaBarang = itemView.findViewById(R.id.tv_history_nama_barang);
            tv_banyakBarang = itemView.findViewById(R.id.tv_history_banyak_dibeli);
            tv_hargaBarang = itemView.findViewById(R.id.tv_history_harga_barang);
            tv_totalHarga = itemView.findViewById(R.id.tv_history_total);

        }
    }
}
