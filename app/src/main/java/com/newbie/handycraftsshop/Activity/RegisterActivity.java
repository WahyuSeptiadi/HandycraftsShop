package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.newbie.handycraftsshop.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText re_email, re_username, re_password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference references;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Register");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        re_email = (EditText)findViewById(R.id.et_email);
        re_username = (EditText)findViewById(R.id.et_username);
        re_password = (EditText)findViewById(R.id.et_password);
        btn_register = (Button)findViewById(R.id.btn_login_login);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = re_username.getText().toString();
                String txt_email = re_email.getText().toString();
                String txt_password = re_password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "All field are required", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else{
                    register(txt_username, txt_email, txt_password);
                }
            }
        });

    }

    private void register(final String username, String email, final String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            references = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String > hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL","default");

                            references.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Toast.makeText(RegisterActivity.this, "Register Anda Berhasil!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(RegisterActivity.this, "Register Anda Gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}