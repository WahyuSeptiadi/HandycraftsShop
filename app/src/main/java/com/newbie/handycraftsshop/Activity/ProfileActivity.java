package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.User;
import com.newbie.handycraftsshop.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private ImageView btn_back;
    private CircleImageView profileUser;
    private TextView nama_profile_User;
    private Uri mImageUri;
    private FirebaseStorage storage;
    private User user;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private String mUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        storage = FirebaseStorage.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        user = new User();
//        DatabaseReference usersRef = ref.child("users");

        btn_back = findViewById(R.id.btn_profile_back);
        nama_profile_User = findViewById(R.id.tv_NamaProfile);
        profileUser = findViewById(R.id.civ_ImageProfile);

        profileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(ProfileActivity.this);
//                updateImage();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.super.onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
                return true;
        }
        return false;
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
            Toast.makeText(this, "Ada yang gak beres nih :(", Toast.LENGTH_SHORT).show();
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
                        User user = new User(mUser, username, downloadUri.toString());
//                        Map<String, Object> updateImage = new HashMap<String, Object>();
//                        updateImage.put("imageUrl", "Amazing Grace");
                        reference.setValue(user);
//                        reference.updateChildren(updateImage);
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
}
