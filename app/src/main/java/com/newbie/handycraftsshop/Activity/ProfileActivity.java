package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.newbie.handycraftsshop.Adapter.ListPostProfileAdapter;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.User;
import com.newbie.handycraftsshop.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private ImageView btn_back;
    private CircleImageView profileUser;
    private TextView nama_profile_User;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private Uri mImageUri;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private SampahModel sampahModel;
    private String mUser;
    private User user;
    private FirebaseStorage storage;

    ArrayList<SampahModel> downModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListPostProfileAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storage = FirebaseStorage.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        btn_back = findViewById(R.id.btn_profile_back);
        nama_profile_User = findViewById(R.id.tv_NamaProfile);
        profileUser = findViewById(R.id.civ_ImageProfile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        sampahModel = new SampahModel();
        user = new User();

        recyclerView = findViewById(R.id.rv_profile_sampah);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.super.onBackPressed();
            }
        });

        profileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImageProfile();
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                nama_profile_User.setText(user.getUsername());
                Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profileUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getDataFromFirestore();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            profileUser.setImageURI(mImageUri);
            updateImage();
        }else{
            Toast.makeText(this, "Gagal Ganti Gambar", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(PostActivity.this, HomeActivity.class));
//            finish();
        }
    }

    private void updateImage(){
        String username = user.getUsername();
        if (mImageUri != null) {
//            User user = new User(mUser, username, mImageUri.toString());
            StorageReference storageRef = storage.getReference()
                    .child(System.currentTimeMillis()
                            + ".jpg");

            UploadTask uploadTask = storageRef.putFile(mImageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        user.setImageUrl(downloadUri.toString());
                        String username = user.getUsername();
                        Toast.makeText(ProfileActivity.this, "Update Berhasil", Toast.LENGTH_LONG).show();
                        db.collection("Data Postingan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentSnapshot documentSnapshot: task.getResult()){
                                    sampahModel = documentSnapshot.toObject(SampahModel.class);
                                    if (Objects.equals(documentSnapshot.get("userID"), mUser)){
                                        db.collection("Data Postingan").document(documentSnapshot.getId()).update("imagepublisher", downloadUri.toString());
                                    }
                                }
                            }
                        });


                        User user = new User(mUser, username, downloadUri.toString());
                        reference.setValue(user);
                    }else{
                        Toast.makeText(ProfileActivity.this, "Failed Posting", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No Image Selected !", Toast.LENGTH_SHORT).show();
        }
    }


    private void changeImageProfile(){
        CropImage.activity().setAspectRatio(1,1).start(ProfileActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.history:
                startActivity(new Intent(ProfileActivity.this, HistoryActivity.class));
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                return true;
        }
        return false;
    }

    private void getDataFromFirestore(){
        db.collection("Data Postingan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    sampahModel = documentSnapshot.toObject(SampahModel.class);
                    if (Objects.equals(documentSnapshot.get("userID"), mUser)){
                        downModelArrayList.add(sampahModel);
                    }
                }
                myAdapter = new ListPostProfileAdapter(ProfileActivity.this, downModelArrayList);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }
}
