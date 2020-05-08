package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.newbie.handycraftsshop.Adapter.ListPostHomeAdapter;
import com.newbie.handycraftsshop.Adapter.WishlistAdapter;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.Wishlist;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.Set;

public class WishlistActivity extends AppCompatActivity {
    private ImageView btnBack;
    private FirebaseFirestore db;
    private SampahModel sampahModel;
    private RecyclerView recyclerView;
    private WishlistAdapter myAdapter;
    private FirebaseAuth mAuth;
    private String mUser;
    private Wishlist wishlist;
    ArrayList<SampahModel> downModelArrayList = new ArrayList<>();
    ArrayList<String> listOfWishlist = new ArrayList<>();
    ArrayList<Wishlist> wishlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        btnBack = findViewById(R.id.btn_wishlist_back);
        recyclerView = findViewById(R.id.rv_wishlist_sampah);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        sampahModel = new SampahModel();
        wishlist = new Wishlist();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistActivity.super.onBackPressed();
            }
        });
        getListOfWish();
    }

    private void getDataFromFirestore(){
        db.collection("Data Postingan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    if (listOfWishlist.contains(documentSnapshot.getId())){
                        sampahModel = documentSnapshot.toObject(SampahModel.class);
                        downModelArrayList.add(sampahModel);
                    }
                }
                myAdapter = new WishlistAdapter(WishlistActivity.this, downModelArrayList);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }

    private void getListOfWish(){
        DocumentReference docRef = db.collection("wishlist").document(mUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        Set<String> data = documentSnapshot.getData().keySet();
                        listOfWishlist.addAll(data);
                    }
                }
                Toast.makeText(WishlistActivity.this, listOfWishlist.toString(), Toast.LENGTH_LONG).show();
                getDataFromFirestore();
            }
        });
    }
    @Override
    public void onBackPressed() {
        /* no call super() */
        setResult(RESULT_OK);
        finish();
    }
}
