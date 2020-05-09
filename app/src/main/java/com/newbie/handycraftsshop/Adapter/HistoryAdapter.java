package com.newbie.handycraftsshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.newbie.handycraftsshop.Activity.BuyActivity;
import com.newbie.handycraftsshop.Model.HistoryModel;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.Wishlist;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

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
        wishlist = new Wishlist();

//        holder.tv_hargaBarang.setText(listHistory.get(position).getHargaBarang());
//        holder.tv_banyakBarang.setText(listHistory.get(position).getBanyakBarang());
//        holder.tv_totalHarga.setText(listHistory.get(position).getTotalHarga());

        holder.tv_hargaBarang.setText("Nama");
        holder.tv_banyakBarang.setText("Banyak");
        holder.tv_totalHarga.setText("Total");

//        DocumentReference documentReference = firebaseFirestore.collection("Data Postingan").document(listHistory.get(position).getIdBarang());
        firebaseFirestore.collection("Data Postingan").whereEqualTo("id_barang", listHistory.get(position).getIdBarang()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        SampahModel sampahModel = documentSnapshot.toObject(SampahModel.class);
                        assert sampahModel != null;
                        Glide.with(mContext)
                                .load(sampahModel.getImage())
                                .into(holder.gambar_barang);
                        holder.tv_namaBarang.setText(sampahModel.getNama());
                    }
                }
            }
        });

//        holder.btn_beli.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toBuy = new Intent(mContext, BuyActivity.class);
//                mContext.startActivity(toBuy);
//            }
//        });
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
            tv_namaBarang = itemView.findViewById(R.id.tv_nama_barang);
            tv_banyakBarang = itemView.findViewById(R.id.tv_history_banyak_dibeli);
            tv_hargaBarang = itemView.findViewById(R.id.tv_history_harga_barang);
            tv_totalHarga = itemView.findViewById(R.id.tv_history_total);

        }
    }
}
