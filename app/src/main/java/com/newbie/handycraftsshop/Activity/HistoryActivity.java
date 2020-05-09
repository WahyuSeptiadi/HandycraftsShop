package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    private HistoryModel historyModel;
    private String userId;
    private Map<String, Object> dataOnDatabase;
    private ArrayList<HistoryModel> historyList  = new ArrayList<>();
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

        getListOfHistory();
    }

    protected void onStart() {
        super.onStart();
        getListOfHistory();
    }

    private void getListOfHistory(){
        CollectionReference collectionReference = firebaseFirestore.collection("users").document(userId).collection("belibarang");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        historyModel = queryDocumentSnapshot.toObject(HistoryModel.class);
                        Toast.makeText(HistoryActivity.this, ""+historyModel.getIdBarang(), Toast.LENGTH_LONG).show();
                        historyList.add(historyModel);
                    }
                }
                myAdapter = new HistoryAdapter(HistoryActivity.this, historyList);
                recyclerView.setAdapter(myAdapter);
            }
        });
    }
}
