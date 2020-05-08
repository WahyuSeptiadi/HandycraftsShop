package com.newbie.handycraftsshop.Activity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.newbie.handycraftsshop.Model.BuyModel;
import com.newbie.handycraftsshop.R;

public class BuyActivity extends AppCompatActivity {

    private ImageView btnBack;
    private String deskripsi;
    private String namabarang;
    private int hargabarang;
    private String imageURL;
    private int stock;
    private TextView tv_deskripsi, tv_stock, tv_harga;
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
                Toast.makeText(BuyActivity.this, totBar, Toast.LENGTH_SHORT).show();
                String deskripsi = tv_deskripsi.toString();
//                int totalBarang = Integer.parseInt(et_totalBeli.getText().toString());
                int harga = 0;
                BuyModel buyModel = new BuyModel(deskripsi, 0,0, hargaBarang);
                db.collection("users").document(mUser).collection("belibarang").document().set(buyModel);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(BuyActivity.this, HomeActivity.class);
                startActivity(toHome);
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
//        toBuy.putExtra("hargaBarang", sampahModel.getHarga());
//        toBuy.putExtra("namaBarang", sampahModel.getNama());
//        toBuy.putExtra("deskripsi", sampahModel.getDeskripsi());
//        toBuy.putExtra("image", sampahModel.getImage());
//        toBuy.putExtra("stock", sampahModel.getStockbarang());
        setInten(deskripsi, hargabarang, stock, imageURL);
    }

    private void setInten(String deskrpsi1, int hargabarang1, int stock1, String imageurl){
        tv_deskripsi.setText(deskrpsi1);
        tv_harga.setText(Integer.toString(hargabarang1));
        tv_stock.setText(Integer.toString(stock1));
        Glide.with(getApplicationContext()).load(imageurl).into(img_sampah);
    }
}
