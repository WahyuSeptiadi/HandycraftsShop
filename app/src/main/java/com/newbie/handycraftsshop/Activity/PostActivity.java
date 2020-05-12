package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
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

public class PostActivity extends AppCompatActivity implements OnMapReadyCallback {

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
        setContentView(R.layout.activity_post);
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

//        getIncomingIntent();

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
                CropImage.activity().setAspectRatio(1,1).start(PostActivity.this);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahBarang();
                Intent toHome = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(toHome);
//                onBackPressed();
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
    }

    public void tambahBarang() {
        String nama_barang = etNamaBarang.getText().toString();
        String harga = etHarga.getText().toString();
        String stock = etStockBarang.getText().toString();
        String desc = etDeskripsi.getText().toString();

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting. .");
        pd.show();

        if (mImageUri != null) {
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
                        postBarang.setImage(downloadUri.toString());
                        SampahModel postBarang = new SampahModel(user.getId(), nama_barang, Integer.valueOf(harga), Integer.valueOf(stock), desc, downloadUri.toString(), user.getImageUrl(), user.getUsername(), latit, longit);

                        Toast.makeText(PostActivity.this, "Postingan Berhasil Ditambahkan", Toast.LENGTH_LONG).show();

                        db.collection("users").document(mUser)
                                .collection("Data Post")
                                .document().set(postBarang);

                        db.collection("Data Postingan").document().set(postBarang);

                    }else{
                        Toast.makeText(PostActivity.this, "Failed Posting", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No Image Selected !", Toast.LENGTH_SHORT).show();
        }
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
            startActivity(new Intent(PostActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
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
                Intent toMaps = new Intent(PostActivity.this, Maps.class);
                startActivity(toMaps);
            }
        });
        moveCamera(new LatLng(latit, longit), 15f);
    }

    private void moveCamera(LatLng latLng, float zoom){
        gMap.clear();
        Log.d(TAG, "moveCamera: moving camera " + latLng.latitude + latLng.longitude);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng);
        gMap.addMarker(options);
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
