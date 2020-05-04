package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.newbie.handycraftsshop.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog pdProgress;
    private TextView tvForgot, tvDaftar;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    ProgressDialog pd;

    /*
    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Login");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnLogin = findViewById(R.id.btn_login_login);
        tvForgot = findViewById(R.id.tv_login_forgot);
        tvDaftar = findViewById(R.id.tv_login_daftar);
        etEmail = findViewById(R.id.et_login_username);
        etPassword = findViewById(R.id.et_login_password);

        auth = FirebaseAuth.getInstance();

        //AUTO LOGIN
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent toHome = new Intent(LoginActivity.this, HomeActivity.class);
                //startActivity(toHome);

                pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please wait.. ");
                pd.show();

                String txt_email = etEmail.getText().toString();
                String txt_password = etPassword.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "All field are required", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent toHome = new Intent(LoginActivity.this, HomeActivity.class);
                                        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(toHome);
                                        finish();
                                        pd.dismiss();
                                    } else{
                                        Toast.makeText(LoginActivity.this, "Authentification failed!", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                            });
                }
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
