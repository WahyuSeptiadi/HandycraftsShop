package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.newbie.handycraftsshop.Model.BuyModel;
import com.newbie.handycraftsshop.Model.User;
import com.newbie.handycraftsshop.R;

import java.util.HashMap;

public class BuyActivity extends AppCompatActivity {

    private ImageView btnBack;
    private String deskripsi, namabarang, imageURL, id_barang, idPublisher;
    private int hargabarang, stock;
    private TextView tv_deskripsi, tv_stock, tv_harga, nama_item;
    private ImageView img_sampah;
    private BuyModel buyModel;
    private Button btnBuy;
    private EditText et_totalBeli;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String mUser;
    FirebaseUser firebaseUser;

    DatabaseReference reference,referencePublisher;
    private int saldo_user_sekarang, saldo_publisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        nama_item = findViewById(R.id.tv_buy_nama_barang);
        tv_deskripsi = findViewById(R.id.tv_deskripsiBuy);
        tv_stock = findViewById(R.id.tv_stockBuy);
        tv_harga = findViewById(R.id.tv_priceBuy);
        img_sampah = findViewById(R.id.imgSampahBuy);
        btnBack = findViewById(R.id.btn_profile_back);
        btnBuy = findViewById(R.id.btn_beli_buy);
        et_totalBeli = findViewById(R.id.totalBeli);
        buyModel = new BuyModel();
        String totBar = et_totalBeli.getText().toString();

        getIncomingIntent();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap hashMap = new HashMap();
        HashMap hashMapPublish = new HashMap();
        //buat pembeli
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                saldo_user_sekarang = Integer.valueOf(user.getSaldo());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //buat publishersaldo_publisher
        referencePublisher = FirebaseDatabase.getInstance().getReference("Users").child(idPublisher);
        referencePublisher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                saldo_publisher = Integer.valueOf(user.getSaldo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int hargaBarang = hargabarang;
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(et_totalBeli)) {
                    Toast.makeText(BuyActivity.this, "Maaf tolong isikan banyaknya barang!", Toast.LENGTH_LONG).show();
                } else {
                    int totalYangDiBeli = Integer.parseInt(et_totalBeli.getText().toString());
                    if (totalYangDiBeli == 0) {
                        Toast.makeText(BuyActivity.this, "Total barang yang Anda beli tidak boleh 0!", Toast.LENGTH_LONG).show();
                    } else if (totalYangDiBeli > stock) {
                        Toast.makeText(BuyActivity.this, "Total barang yang Anda beli melebihi stok kami", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(BuyActivity.this, totBar, Toast.LENGTH_SHORT).show();
                        String idBarang = id_barang;
//                int totalBarang = Integer.parseInt(et_totalBeli.getText().toString());
                        int harga = totalYangDiBeli * hargabarang;
                        BuyModel buyModel = new BuyModel(idBarang, Integer.parseInt(et_totalBeli.getText().toString()), harga, hargabarang);
                        db.collection("users").document(mUser).collection("belibarang").document().set(buyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    hashMap.put("saldo", String.valueOf(saldo_user_sekarang - harga));
                                    hashMapPublish.put("saldo", String.valueOf(saldo_publisher + harga));

                                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(BuyActivity.this, "Anda telah memesan " + deskripsi, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    referencePublisher.updateChildren(hashMapPublish).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                        }
                                    });
                                    BuyActivity.super.onBackPressed();
                                }
                            }
                        });
                    }
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyActivity.super.onBackPressed();
            }
        });
    }

    private void getIncomingIntent() {
        deskripsi = getIntent().getStringExtra("deskripsi");
        namabarang = getIntent().getStringExtra("namaBarang");
        hargabarang = getIntent().getIntExtra("hargaBarang", 0);
        stock = getIntent().getIntExtra("stock", 0);
        imageURL = getIntent().getStringExtra("image");
        id_barang = getIntent().getStringExtra("id_barang");
        idPublisher= getIntent().getStringExtra("userID");

//        toBuy.putExtra("hargaBarang", sampahModel.getHarga());
//        toBuy.putExtra("namaBarang", sampahModel.getNama());
//        toBuy.putExtra("deskripsi", sampahModel.getDeskripsi());
//        toBuy.putExtra("image", sampahModel.getImage());
//        toBuy.putExtra("stock", sampahModel.getStockbarang());
        setInten(namabarang, deskripsi, hargabarang, stock, imageURL);
    }

    private void setInten(String namabarang1, String deskrpsi1, int hargabarang1, int stock1, String imageurl) {
        tv_deskripsi.setText(deskrpsi1);
        tv_harga.setText("Rp. " + Integer.toString(hargabarang1));
        tv_stock.setText(Integer.toString(stock1));
        nama_item.setText(namabarang1);
        Glide.with(getApplicationContext()).load(imageurl).into(img_sampah);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
