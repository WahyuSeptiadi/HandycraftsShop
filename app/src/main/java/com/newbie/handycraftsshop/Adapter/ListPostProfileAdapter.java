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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.newbie.handycraftsshop.Activity.PostActivity;
import com.newbie.handycraftsshop.Activity.UpdateActivity;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.R;

import java.security.UnresolvedPermission;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;
import java.util.zip.Inflater;

/**
 * Created by wahyu_septiadi on 04, May 2020.
 * Visit My GitHub --> https://github.com/WahyuSeptiadi
 */

public class ListPostProfileAdapter extends RecyclerView.Adapter<ListPostProfileAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<SampahModel> listSampah;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String mUser;
    private DocumentReference documentReference;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

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

        int harga = listSampah.get(position).getHarga();

        holder.namaSampah.setText(listSampah.get(position).getNama());
        holder.hargaSampah.setText("Rp. "+ String.valueOf(harga));

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ambil_data_image = listSampah.get(holder.getAdapterPosition()).getNama();

                db = FirebaseFirestore.getInstance();

                CollectionReference collectionReference = db.collection("Data Postingan");
                collectionReference.whereEqualTo("nama", ambil_data_image).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (DocumentSnapshot documentSnapshot: task.getResult()){
                                SampahModel sampahModel = documentSnapshot.toObject(SampahModel.class);
                                Intent toUpdate = new Intent(mContext, UpdateActivity.class);
                                toUpdate.putExtra("id_barang2", documentSnapshot.getId());
                                toUpdate.putExtra("hargaBarang2", sampahModel.getHarga());
                                toUpdate.putExtra("namaBarang2", sampahModel.getNama());
                                toUpdate.putExtra("deskripsi2", sampahModel.getDeskripsi());
                                toUpdate.putExtra("image2", sampahModel.getImage());
                                toUpdate.putExtra("stock2", sampahModel.getStockbarang());
                                toUpdate.putExtra("imagepublisher", sampahModel.getImagepublisher());
                                toUpdate.putExtra("userID", sampahModel.getUserID());
                                toUpdate.putExtra("usernamepublisher", sampahModel.getUsernamepublisher());

                                collectionReference.document(documentSnapshot.getId()).update(documentSnapshot.getData());
                                mContext.startActivity(toUpdate);

                            }
                        }
                    }
                });

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser().getUid();
                CollectionReference data_postingan = db.collection("Data Postingan");
                String nameOfpic = listSampah.get(holder.getAdapterPosition()).getImage();
                CollectionReference data_post = db.collection("users").document(mUser).collection("Data Post");

                data_postingan.whereEqualTo("image", nameOfpic).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                data_postingan.document(documentSnapshot.getId()).delete();
                                //Toast.makeText(mContext, "telah dihapus", Toast.LENGTH_LONG).show();

                                data_post.whereEqualTo("image", nameOfpic).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (DocumentSnapshot document : task.getResult()) {
                                                data_post.document(document.getId()).delete();
                                                Toast.makeText(mContext, "telah dihapus "+document.getId(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    }
                                });

                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listSampah.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSampah, imgFavor, btnEdit, btnDelete;
        TextView like, namaSampah, hargaSampah;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSampah = itemView.findViewById(R.id.img_sampahProfile);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            namaSampah = itemView.findViewById(R.id.tv_nama_barang);
            hargaSampah = itemView.findViewById(R.id.tv_profile_harga_barang);
//            imgFavor = itemView.findViewById(R.id.img_favorite_profile);
//            like = itemView.findViewById(R.id.like);
        }
    }
}
