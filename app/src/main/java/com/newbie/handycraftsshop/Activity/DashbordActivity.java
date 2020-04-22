package com.newbie.handycraftsshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.newbie.handycraftsshop.R;

public class DashbordActivity extends AppCompatActivity {

    private Button login, register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        login = findViewById(R.id.btn_login_dashbord);
        register = findViewById(R.id.btn_register_dashbord);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegister = new Intent(DashbordActivity.this, RegisterActivity.class);
                startActivity(toRegister);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin = new Intent(DashbordActivity.this, LoginActivity.class);
                startActivity(toLogin);
            }
        });
    }
}
