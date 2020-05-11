package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.newbie.handycraftsshop.Adapter.HistoryAdapter;
import com.newbie.handycraftsshop.Model.HistoryModel;
import com.newbie.handycraftsshop.R;

import java.util.ArrayList;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    private ImageView btnBack;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private HistoryModel historyModel;
    private String userId;
    private Map<String, Object> dataOnDatabase;
    private RecyclerView recyclerView;
    private HistoryAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        btnBack = findViewById(R.id.btn_history_back);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        historyModel = new HistoryModel();
        recyclerView = findViewById(R.id.rv_history_buy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryActivity.super.onBackPressed();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        getListOfHistory();
    }

    private void getListOfHistory(){
        db.collection("users").document(userId).collection("belibarang").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<HistoryModel> historyList  = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    HistoryModel historyModel = documentSnapshot.toObject(HistoryModel.class);
                    historyList.add(historyModel);
                }
//                Log.d("CheckList", historyModel.getId_barang());
                myAdapter = new HistoryAdapter(HistoryActivity.this, historyList);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }

}
