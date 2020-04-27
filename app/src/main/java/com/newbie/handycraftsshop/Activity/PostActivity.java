package com.newbie.handycraftsshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.newbie.handycraftsshop.R;

public class PostActivity extends AppCompatActivity {

    private Button btnPost;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

//        btnPost = findViewById(R.id.btn_postBarang);
//
//        btnPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toHome = new Intent(PostActivity.this, HomeActivity.class);
//                startActivity(toHome);
//            }
//        });

        btn_back = (ImageView)findViewById(R.id.btn_profile_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
