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
import com.google.firebase.firestore.FirebaseFirestore;
import com.newbie.handycraftsshop.Model.BuyModel;
import com.newbie.handycraftsshop.R;

public class BuyActivity extends AppCompatActivity {

    private ImageView btnBack;
    private String deskripsi, namabarang, imageURL, id_barang;
    private int hargabarang, stock;
    private TextView tv_deskripsi, tv_stock, tv_harga, nama_item;
    private ImageView img_sampah;
    private BuyModel buyModel;
    private Button btnBuy;
    private EditText et_totalBeli;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_buy);
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

        int hargaBarang = hargabarang;
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(et_totalBeli)){
                    Toast.makeText(BuyActivity.this, "Maaf tolong isikan banyaknya barang!", Toast.LENGTH_LONG).show();
                }else {
                    int totalYangDiBeli = Integer.parseInt(et_totalBeli.getText().toString());
                    if(totalYangDiBeli==0){
                        Toast.makeText(BuyActivity.this, "Total barang yang Anda beli tidak boleh 0!", Toast.LENGTH_LONG).show();
                    }else if (totalYangDiBeli>stock){
                        Toast.makeText(BuyActivity.this, "Total barang yang Anda beli melebihi stok kami", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(BuyActivity.this, totBar, Toast.LENGTH_SHORT).show();
                        String idBarang = id_barang;
//                int totalBarang = Integer.parseInt(et_totalBeli.getText().toString());
                        int harga = totalYangDiBeli*hargabarang;
                        BuyModel buyModel = new BuyModel(idBarang, Integer.parseInt(et_totalBeli.getText().toString()),harga, hargabarang);
                        db.collection("users").document(mUser).collection("belibarang").document().set(buyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(BuyActivity.this, "Anda telah memesan "+deskripsi, Toast.LENGTH_LONG).show();
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
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        deskripsi = getIntent().getStringExtra("deskripsi");
        namabarang = getIntent().getStringExtra("namaBarang");
        hargabarang = getIntent().getIntExtra("hargaBarang", 0);
        stock = getIntent().getIntExtra("stock", 0);
        imageURL = getIntent().getStringExtra("image");
        id_barang = getIntent().getStringExtra("id_barang");
//        toBuy.putExtra("hargaBarang", sampahModel.getHarga());
//        toBuy.putExtra("namaBarang", sampahModel.getNama());
//        toBuy.putExtra("deskripsi", sampahModel.getDeskripsi());
//        toBuy.putExtra("image", sampahModel.getImage());
//        toBuy.putExtra("stock", sampahModel.getStockbarang());
        setInten(namabarang, deskripsi, hargabarang, stock, imageURL);
    }

    private void setInten(String namabarang1, String deskrpsi1, int hargabarang1, int stock1, String imageurl){
        tv_deskripsi.setText(deskrpsi1);
        tv_harga.setText("Rp. "+Integer.toString(hargabarang1));
        tv_stock.setText(Integer.toString(stock1));
        nama_item.setText(namabarang1);
        Glide.with(getApplicationContext()).load(imageurl).into(img_sampah);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
