package com.newbie.handycraftsshop.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.newbie.handycraftsshop.Model.MapsModel;
import com.newbie.handycraftsshop.Model.SampahModel;
import com.newbie.handycraftsshop.Model.User;
import com.newbie.handycraftsshop.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button btnPost;
    private ImageView btnBack;
    private EditText etDeskripsi, etHarga, etNamaBarang, etStockBarang;
    private static final String TAG = "PostActivity";
    private static final String KEY_DESKRIPSI = "deskripsi";
    private static final String KEY_HARGA = "harga";
    private static final String KEY_NAMA = "nama barang";
    public static final int PICK_IMAGE = 1;
    private ImageView iv_sampah;
    private FirebaseAuth mAuth;
    private Uri mImageUri;
    private FirebaseStorage storage;
    private SampahModel postBarang;
    private MapView mapView;
    private MapsModel mapsModel;
    private User user;
    private GoogleMap gMap;
    private double latit = 0.0;
    private double longit = 0.0;
    private int PLACE_PICKER_REQUEST = 1;
    private MapsModel getMapsModel(){
        return mapsModel;
    }
    private void setMapsModel(MapsModel mapsModel){
        this.mapsModel = mapsModel;
    }

    private String deskripsi_barang2;
    private String nama_barang2;
    private String imageURL2;
    private String id_barang2;
    private int harga_barang2;
    private int stock_barang2;
    private String imagepublisher;
    private String userID;
    private String usernamepublisher;
    private SampahModel sampahModel;

    private FirebaseFirestore db;
    private String mUser;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        sampahModel = new SampahModel();
        postBarang = new SampahModel();
        mapsModel = new MapsModel();
        user = new User();

        mapView =(MapView) findViewById(R.id.mv_location);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        getUsernameImageData();

        getIncomingIntent();

        etNamaBarang = findViewById(R.id.et_post_name);
        etHarga = findViewById(R.id.et_post_price);
        etStockBarang = findViewById(R.id.et_post_total_stock);
        etDeskripsi = findViewById(R.id.et_post_deskripsi);
        btnPost = findViewById(R.id.btn_postBarang);
        iv_sampah = findViewById(R.id.cv_post_item_photo);
        btnBack = findViewById(R.id.btn_post_back);

        iv_sampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickFromGallery();
                CropImage.activity().setAspectRatio(1,1).start(UpdateActivity.this);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();

//                updateimage(mImageUri);
                    updateData(id_barang2);
                    Map<String, Object> map = new HashMap<>();
                    map.put("deskripsi", deskripsi_barang2);
                    map.put("harga", harga_barang2);
                    map.put("image", imageURL2);
                    map.put("nama", nama_barang2);
                    map.put("stockbarang", stock_barang2);
                    map.put("usernamepublisher", usernamepublisher);
                    map.put("imagepublisher", imagepublisher);
                    map.put("userID", userID);

                    String aaa = db.collection("Data Postingan").document(id_barang2).toString();

                    db.collection("Data Postingan").document(id_barang2)
                            .update(map);

                    Toast.makeText(UpdateActivity.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "APANIIII SUKSEEEEESSSSSSS  " + aaa);

                    Log.d(TAG, "on click SUKSEEEEESSSSSSS USERS DATA POST " + id_barang2);
                    Log.d(TAG, "SUKSEEEEESSSSSSS NIH BARANGNYA " + nama_barang2);
                    Log.d(TAG, "SUKSEEEEESSSSSSS NIH BARANGNYA " + deskripsi_barang2);
                    Log.d(TAG, "SUKSEEEEESSSSSSS NIH BARANGNYA " + harga_barang2);
                    Log.d(TAG, "SUKSEEEEESSSSSSS NIH BARANGNYA " + stock_barang2);
//                    getIncomingIntent();

                onBackPressed();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            latit = bundle.getDouble("latitude");
            longit = bundle.getDouble("longitude");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mapView.onStart();
    }

    private void getUsernameImageData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            iv_sampah.setImageURI(mImageUri);
        }else{
            Toast.makeText(this, "Ada yang gak beres nih :(", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateActivity.this, HomeActivity.class));
            finish();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        //Mendapatkan Dokumen
        DocumentReference documentReference = db.collection("users").document(mUser).collection("location").document("toko");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    double latitude = documentSnapshot.getDouble("latitude");
                    double longitude = documentSnapshot.getDouble("longitude");
                    moveCamera(new LatLng(latitude, longitude), 15f);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent toMaps = new Intent(UpdateActivity.this, MapsUpdate.class);
                startActivity(toMaps);
            }
        });
        moveCamera(new LatLng(latit, longit), 15f);
//        gMap.setMyLocationEnabled(true);

    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving camera " + latLng.latitude + latLng.longitude);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng);
        gMap.addMarker(options);
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void loadMaps(){
        DocumentReference documentReference = db.collection("users").document(mUser).collection("location").document("toko");
    }

    private void getIncomingIntent() {

        nama_barang2 = getIntent().getStringExtra("namaBarang2");
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            id_barang2 = extras.getString("id_barang2", "");
            nama_barang2 = extras.getString("namaBarang2", "");
            harga_barang2 = extras.getInt("hargaBarang2", 0);
            stock_barang2 = extras.getInt("stock2", 0);
            deskripsi_barang2 = extras.getString("deskripsi2", "");
            imageURL2 = extras.getString("image2", "");
            imagepublisher = extras.getString("imagepublisher", "");
            userID = extras.getString("userID", "");
            usernamepublisher = extras.getString("usernamepublisher", "");

            Log.d("BEBAS APA AJA ", nama_barang2);
            setInten(id_barang2, nama_barang2, deskripsi_barang2, harga_barang2, stock_barang2, imageURL2);

        } else{
            Log.d("INI ELSE", "BBBBBBBBBB");
        }
    }

    private void updateimage(Uri uri){
        StorageReference storageReference = storage.getReference().child(id_barang2);

        db.collection("Data Postingan").document(id_barang2).update("image", sampahModel.getImage());
        UploadTask uploadTask = storageReference.putFile(uri);

        Task<Uri> uriTask = uploadTask.continueWithTask((task) -> {
            if (task.isSuccessful()){
                throw task.getException();
            }
            return storageReference.getDownloadUrl();
        }).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                sampahModel.setImage(downloadUri.toString());
                imageURL2 = downloadUri.toString();
                db.collection("Data Postingan").document(id_barang2).update("image", downloadUri.toString());
                Toast.makeText(UpdateActivity.this, "Update Image Berhasil", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(UpdateActivity.this, "Update Image Gagal", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "EMANG GABISA UPDATE IMAGE ");
            }
        });
    }

    private void setInten(String id_barang2, String namabarang2, String deskrpsibarang2, int hargabarang2, int stockbarang2, String imageurl2) {

        this.id_barang2 = id_barang2;
        Log.d(TAG, "set inten SUKSEEEEESSSSSSS USERS DATA POST " + id_barang2);

        etNamaBarang = findViewById(R.id.et_post_name);
        etHarga = findViewById(R.id.et_post_price);
        etStockBarang = findViewById(R.id.et_post_total_stock);
        etDeskripsi = findViewById(R.id.et_post_deskripsi);
        iv_sampah = findViewById(R.id.cv_post_item_photo);

        etNamaBarang.setText(namabarang2);
        etDeskripsi.setText(deskrpsibarang2);
        etHarga.setText(Integer.toString(hargabarang2));
        etStockBarang.setText(Integer.toString(stockbarang2));
        Glide.with(getApplicationContext()).load(imageurl2).into(iv_sampah);

//        updateData(id_barang2);

    }

    private void updateData(String id_barang2){

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Data Postingan").document();

        etNamaBarang = findViewById(R.id.et_post_name);
        etHarga = findViewById(R.id.et_post_price);
        etStockBarang = findViewById(R.id.et_post_total_stock);
        etDeskripsi = findViewById(R.id.et_post_deskripsi);
        iv_sampah = findViewById(R.id.cv_post_item_photo);

        deskripsi_barang2 = etDeskripsi.getText().toString();
        String harga_barang_update = etHarga.getText().toString();
        nama_barang2 = etNamaBarang.getText().toString();
        String stock_barang_update = etStockBarang.getText().toString();

        harga_barang2 = Integer.parseInt(harga_barang_update);
        stock_barang2 = Integer.parseInt(stock_barang_update);

        Map<String, Object> map = new HashMap<>();
        map.put("deskripsi", deskripsi_barang2);
        map.put("harga", harga_barang2);
//        map.put("image", );
        map.put("nama", nama_barang2);
        map.put("stockbarang", stock_barang2);

        Log.d(TAG, "update data SUKSEEEEESSSSSSS USERS DATA POST " + nama_barang2);

        documentReference.update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "SUKSEEEEESSSSSSS FIXNYA DISINII " + nama_barang2);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "GAGAALLLLLLLLL USERS DATA POST", e);
                    }
                });

        DocumentReference documentReference1 = FirebaseFirestore.getInstance()
                .collection("Data Postingan").document();

        documentReference1.update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "SUKSEEEEESSSSSSS DATA POSTINGAN");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "GAGAALLLLLLLLL USERS DATA POST", e);
                    }
                });

    }

}
