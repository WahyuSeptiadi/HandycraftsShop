package com.newbie.handycraftsshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.newbie.handycraftsshop.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog pdProgress;
    private TextView tvForgot, tvDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login_login);
        tvForgot = findViewById(R.id.tv_login_forgot);
        tvDaftar = findViewById(R.id.tv_login_daftar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(toHome);
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLupaPass = new Intent(LoginActivity.this, LupaPassActivity.class);
                startActivity(toLupaPass);
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegis = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegis);
            }
        });
    }
}
