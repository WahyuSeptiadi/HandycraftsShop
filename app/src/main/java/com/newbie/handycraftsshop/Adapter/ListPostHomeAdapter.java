package com.newbie.handycraftsshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.newbie.handycraftsshop.Activity.BuyActivity;
import com.newbie.handycraftsshop.Activity.WishlistActivity;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.Wishlist;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPostHomeAdapter extends RecyclerView.Adapter<ListPostHomeAdapter.ListViewHolder> {

    private ArrayList<String> listOfWishlist = new ArrayList<>();
    private Map<String, String> percobaan = new Hashtable<>();
    private ArrayList<SampahModel> listSampah;
    private Map<String, SampahModel> data;
    private ArrayList<Wishlist> wishlists;
    private ArrayList<Wishlist> listWish;
    private ArrayList<String> coba;
    private DatabaseReference reff;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Wishlist wishlist;
    private Context mContext;
    private String mUser;

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_sampah_home, parent, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        return new ListViewHolder(view);
    }

    public ListPostHomeAdapter(Context mContext, ArrayList<SampahModel> listSampah, Map<String, SampahModel> data, ArrayList<String> listOfWishlist) {
        this.mContext = mContext;
        this.listSampah = listSampah;
        this.data = data;
        this.listOfWishlist = listOfWishlist;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        coba = new ArrayList<>();
        int count = 0;
        db = FirebaseFirestore.getInstance();
        for (Map.Entry<String, SampahModel> e : data.entrySet()) {
            percobaan.put(e.getKey(), e.getValue().getImage());
        }
        holder.tv_namaBarang.setText(listSampah.get(position).getNama());
        holder.tv_usernamepublisher.setText(listSampah.get(position).getUsernamepublisher());
        Glide.with(mContext)
                .load(listSampah.get(position).getImage())
                .into(holder.imgsampah_home);
        Glide.with(mContext)
                .load(listSampah.get(position).getImagepublisher())
                .into(holder.civ_imgpublisher);
        for (String keyOnData : percobaan.keySet()) {
            for (String keyOnWishlist : listOfWishlist) {
                if (keyOnData.equals(keyOnWishlist)){
                    if (percobaan.get(keyOnData).equals(listSampah.get(position).getImage())){
                        holder.img_wishlist.setChecked(true);
                        coba.add(keyOnData);
                        count+=1;
                    }else {
                        holder.img_wishlist.setChecked(false);
                    }
                } else {

                }
            }
        }
//        Toast.makeText(mContext, ""+count+" "+coba.toString(), Toast.LENGTH_LONG).show();

//        Picasso.get().load(listSampah.get(position).getImage()).into(holder.imgsampah_home);
//        Log.d("Image", listSampah.get(position).getImage());

        holder.btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfpic = listSampah.get(holder.getAdapterPosition()).getImage();
                CollectionReference docRefDataPost = db.collection("Data Postingan");
                docRefDataPost.whereEqualTo("image", nameOfpic).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: task.getResult()){
                                SampahModel sampahModel =  documentSnapshot.toObject(SampahModel.class);
                                Intent toBuy = new Intent(mContext, BuyActivity.class);
                                toBuy.putExtra("id_barang", documentSnapshot.getId());
                                toBuy.putExtra("hargaBarang", sampahModel.getHarga());
                                toBuy.putExtra("namaBarang", sampahModel.getNama());
                                toBuy.putExtra("deskripsi", sampahModel.getDeskripsi());
                                toBuy.putExtra("image", sampahModel.getImage());
                                toBuy.putExtra("stock", sampahModel.getStockbarang());
                                toBuy.putExtra("userID", sampahModel.getUserID());
                                mContext.startActivity(toBuy);
                                Log.d("CheckSampahModel",  sampahModel.getNama());
                                Toast.makeText(mContext, sampahModel.getNama(), Toast.LENGTH_SHORT).show();
//                                db.collection("users").document(mUser).collection("belibarang").document().set(sampahModel);
                            }
                        }
                    }
                });
            }
        });

        holder.img_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishlist = new Wishlist();
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser().getUid();
                String nameOfPic = listSampah.get(holder.getAdapterPosition()).getImage();
                DocumentReference docRefWishlist = db.collection("wishlist").document(mUser);
                CollectionReference docRefDataPostWish = db.collection("Data Postingan");
                Dictionary dictionary = new Hashtable();

                if(holder.img_wishlist.isChecked()){
                    docRefDataPostWish.whereEqualTo("image", nameOfPic).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    Toast.makeText(mContext, ""+document.getId(), Toast.LENGTH_LONG).show();
                                    dictionary.put(document.getId(), true);
                                    docRefWishlist.set(dictionary, SetOptions.merge());
                                }
                            }
                        }
                    });
                }else{
                    docRefDataPostWish.whereEqualTo("image", nameOfPic).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    Toast.makeText(mContext, ""+document.getId(), Toast.LENGTH_LONG).show();
                                    dictionary.put(document.getId(), FieldValue.delete());
                                    docRefWishlist.set(dictionary, SetOptions.merge());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSampah.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgsampah_home, imgfavorite_home;
        ToggleButton img_wishlist;
        CircleImageView civ_imgpublisher;
        Button btn_beli;
        TextView tv_namaBarang, tv_usernamepublisher;
        public ListViewHolder(View itemView) {
            super(itemView);
            imgsampah_home = itemView.findViewById(R.id.imgSampahPost);
            img_wishlist = itemView.findViewById(R.id.followed_botton);
            btn_beli = itemView.findViewById(R.id.btn_beli_home);
            tv_namaBarang = itemView.findViewById(R.id.tv_nama_barang);
            civ_imgpublisher = itemView.findViewById(R.id.civ_imagePublisher);
            tv_usernamepublisher = itemView.findViewById(R.id.tv_usernamepostinganHome);

        }
    }

}
