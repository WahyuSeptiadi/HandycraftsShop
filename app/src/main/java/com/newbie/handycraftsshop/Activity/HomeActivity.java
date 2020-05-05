package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.newbie.handycraftsshop.Adapter.ListPostHomeAdapter;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.User;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private Button btnBuy;
    private TextView namaUser;
    private CircleImageView ppUser;
    private ImageView imgChat;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private SampahModel sampahModel;
    private ListPostHomeAdapter myAdapter;
    private List<String> listDoc;
    ArrayList<SampahModel> downModelArrayList = new ArrayList<>();

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private String mUser;

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

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Login");

        ppUser = findViewById(R.id.imageprofile_home);
        namaUser = findViewById(R.id.tv_nameprofile_home);
        imgChat = findViewById(R.id.imgChat);
        fab = findViewById(R.id.fabPost);

        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChat = new Intent(HomeActivity.this, DaftarChatActivity.class);
                startActivity(toChat);
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

        getDataFromFirestore();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getDataFromFirestore();
    }

    private void getDataFromFirestore(){
        db.collection("Data Postingan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                listDoc = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
//                    listDoc.add(documentSnapshot.getId());
                    sampahModel = documentSnapshot.toObject(SampahModel.class);
                    downModelArrayList.add(sampahModel);
                }
                myAdapter= new ListPostHomeAdapter(HomeActivity.this, downModelArrayList);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }

//    private void getDataFromFirestore(){
//        db.collection("Data Postingan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (DocumentSnapshot documentSnapshot: task.getResult()){
//                    sampahModel = documentSnapshot.toObject(SampahModel.class);
//                    downModelArrayList.add(sampahModel);
//                }
//                myAdapter= new ListPostHomeAdapter(HomeActivity.this, downModelArrayList);
//                recyclerView.setAdapter(myAdapter);
//            }
//        });
//    }

}
