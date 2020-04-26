package com.newbie.handycraftsshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.newbie.handycraftsshop.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgChat = findViewById(R.id.imgChat);

        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChat = new Intent(ProfileActivity.this, DaftarChatActivity.class);
                startActivity(toChat);
            }
        });
    }
}
