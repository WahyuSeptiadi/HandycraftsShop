package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.newbie.handycraftsshop.Model.PostBarang;
import com.newbie.handycraftsshop.R;
import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {

    private Button btnPost;
    private EditText etDeskripsi, etHarga, etNamaBarang, etStockBarang;
    private static final String TAG = "PostActivity";
    private static final String KEY_DESKRIPSI = "deskripsi";
    private static final String KEY_HARGA = "harga";
    private static final String KEY_NAMA = "nama barang";
    public static final int PICK_IMAGE = 1;
    private ImageView iv_sampah;
    private FirebaseAuth mAuth;
    private Uri mImageUri;
    private StorageReference mStorageReference;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private PostBarang postBarang;

    private FirebaseFirestore db;
    private String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        postBarang = new PostBarang();

        etNamaBarang = findViewById(R.id.et_post_name);
        etHarga = findViewById(R.id.et_post_price);
        etStockBarang = findViewById(R.id.et_post_total_stock);
        etDeskripsi = findViewById(R.id.et_post_deskripsi);
        btnPost = findViewById(R.id.btn_postBarang);
        iv_sampah = findViewById(R.id.cv_post_item_photo);
//        postBarang =

        iv_sampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tambahBarang();
//                Intent toHome = new Intent(PostActivity.this, HomeActivity.class);

//                startActivity(toHome);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(iv_sampah);
//            insertImage(mImageUri);
//            Picasso.with(this).load(mImageUri).into(iv_sampah);
        }
    }

    public void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "pilih gambar"), PICK_IMAGE);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public void tambahBarang() {
        String nama_barang = etNamaBarang.getText().toString();
        String harga = etHarga.getText().toString();
        String stock = etStockBarang.getText().toString();
        String desc = etDeskripsi.getText().toString();

        if (mImageUri != null) {
            StorageReference storageRef = storage.getReference().child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
//            db.collection("users").document(mUser).update("image", postBarang.getImage());
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
                        postBarang.setImage(downloadUri.toString());
                        PostBarang postBarang = new PostBarang(nama_barang, Integer.valueOf(harga), Integer.valueOf(stock), desc, downloadUri.toString(), "");
                        Toast.makeText(PostActivity.this, "Postingan Berhasil Ditambahkan", Toast.LENGTH_LONG).show();
//                        db.collection("users").document(mUser).update("image", downloadUri.toString());
                        if (postBarang.getImage() != null) {

                        }else {

                        }
                        db.collection("users").document(mUser).collection("Data Post").document().set(postBarang);
                    }
                }
            });
        }
    }

}
