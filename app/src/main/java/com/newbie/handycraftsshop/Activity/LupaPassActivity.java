package com.newbie.handycraftsshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.newbie.handycraftsshop.R;

public class LupaPassActivity extends AppCompatActivity {

    private EditText registeredEmail;
    private Button resetPasswordBtn;
    private ImageView goBack;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);

        registeredEmail = findViewById(R.id.editText_lupa_password);
        resetPasswordBtn = findViewById(R.id.btn_lupa_password_reseting);
        goBack = findViewById(R.id.btn_lupa_password_back);
        firebaseAuth =  FirebaseAuth.getInstance();

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordBtn.setEnabled(false);
                if(TextUtils.isEmpty(registeredEmail.getText())){
                    Toast.makeText(LupaPassActivity.this,"Maaf tolong isi kolom email.",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LupaPassActivity.this,"Email berhasil dikirim",Toast.LENGTH_LONG).show();
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(LupaPassActivity.this,error,Toast.LENGTH_SHORT).show();
                            }
                            resetPasswordBtn.setEnabled(true);
                        }
                    });
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LupaPassActivity.super.onBackPressed();
            }
        });

        registeredEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkInput() {
        if(TextUtils.isEmpty(registeredEmail.getText())){
            resetPasswordBtn.setEnabled(false);
            registeredEmail.setError("This is required");
        }else{
            resetPasswordBtn.setEnabled(true);
        }
    }
}
