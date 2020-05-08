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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.newbie.handycraftsshop.Activity.BuyActivity;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.Wishlist;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ListViewHolder> {
    private Context mContext;
    private ArrayList<SampahModel> listSampah;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String mUser;
    private Wishlist wishlist;
    ArrayList<Wishlist> wishlists = new ArrayList<>();

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_sampah_home, parent, false);
        return new ListViewHolder(view);
    }

    public WishlistAdapter(Context mContext, ArrayList<SampahModel> listSampah) {
        this.mContext = mContext;
        this.listSampah = listSampah;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();
        wishlist = new Wishlist();

        holder.tv_namaBarang.setText(listSampah.get(position).getNama());
        holder.tv_usernamepublisher.setText(listSampah.get(position).getUsernamepublisher());
        Glide.with(mContext)
                .load(listSampah.get(position).getImage())
                .into(holder.imgsampah_home);
        Glide.with(mContext)
                .load(listSampah.get(position).getImagepublisher())
                .into(holder.civ_imgpublisher);
        holder.img_wishlist.setChecked(true);

//        Picasso.get().load(listSampah.get(position).getImage()).into(holder.imgsampah_home);
//        Log.d("Image", listSampah.get(position).getImage());

        holder.btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBuy = new Intent(mContext, BuyActivity.class);
                mContext.startActivity(toBuy);
            }
        });

        holder.img_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser().getUid();
                Dictionary dictionary = new Hashtable();
                String nameOfPic = listSampah.get(holder.getAdapterPosition()).getImage();
                DocumentReference docRef = db.collection("wishlist").document(mUser);
                CollectionReference docRefDataPostWish = db.collection("Data Postingan");
                DocumentReference docRefWishlist = db.collection("wishlist").document(mUser);

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