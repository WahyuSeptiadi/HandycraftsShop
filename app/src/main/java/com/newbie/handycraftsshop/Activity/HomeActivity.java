package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.newbie.handycraftsshop.Adapter.ListPostHomeAdapter;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.User;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private Button btnBuy;
    private TextView namaUser;
    private CircleImageView ppUser;
    private ImageView imgChat, imgCart;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private SampahModel sampahModel;

    private ListPostHomeAdapter myAdapter;
    private List<String> listDoc;
    Map<String, SampahModel> dataku ;
    ArrayList<String> listOfWishlist = new ArrayList<>();
    ArrayList<SampahModel> downModelArrayList = new ArrayList<>();

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private String mUser;

    private SwipeRefreshLayout sRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.rv_home_sampah);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        sampahModel = new SampahModel();
        dataku = new Hashtable<>();

        ppUser = findViewById(R.id.imageprofile_home);
        namaUser = findViewById(R.id.tv_nameprofile_home);
        imgChat = findViewById(R.id.imgChat);
        imgCart = findViewById(R.id.imgCart);
        fab = findViewById(R.id.fabPost);

        sRefresh = findViewById(R.id.swapRefresh);

        getListOfWish();

        sRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sRefresh.setRefreshing(false);
                        myAdapter.notifyDataSetChanged();
                        getDataFromFirestore();
                    }
                }, 1000);
            }
        });

        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChat = new Intent(HomeActivity.this, DaftarChatActivity.class);
                startActivity(toChat);
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent toCart = new Intent(HomeActivity.this, WishlistActivity.class);
//                startActivityForResult(toCart, 201);
                startActivity(toCart);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPost = new Intent(HomeActivity.this, PostActivity.class);
                startActivity(toPost);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                namaUser.setText(user.getUsername());
                Glide.with(HomeActivity.this).load(user.getImageUrl()).into(ppUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ppUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(toProfile);

            }
        });
//        getDataFromFirestore();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getListOfWish();
    }

    private void getListOfWish(){
        DocumentReference docRef = db.collection("wishlist").document(mUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                listOfWishlist.clear();
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    if (documentSnapshot.exists()){
                        Set<String> data = Objects.requireNonNull(documentSnapshot.getData()).keySet();
                        listOfWishlist.addAll(data);
                    }
                    getDataFromFirestore();
                }else{
                    getDataFromFirestore();
                }
//                Toast.makeText(HomeActivity.this, ""+listOfWishlist.toString(), Toast.LENGTH_LONG).show();
            }
        });
        getDataFromFirestore();
    }

    private void getDataFromFirestore(){
        db.collection("Data Postingan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                dataku.clear();
                downModelArrayList.clear();
//                listDoc = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
//                    listDoc.add(documentSnapshot.getId());
                    sampahModel = documentSnapshot.toObject(SampahModel.class);
                    downModelArrayList.add(sampahModel);
                    dataku.put(documentSnapshot.getId(), sampahModel);
                }
                myAdapter= new ListPostHomeAdapter(HomeActivity.this, downModelArrayList, dataku, listOfWishlist);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            // here you can call your method !
        }
    }
}
