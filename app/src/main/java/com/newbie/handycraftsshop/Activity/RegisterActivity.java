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
import android.widget.ImageView;
import android.widget.TextView;
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

    EditText re_email, re_username, re_password, re_repassword;
    Button btn_register;
    ImageView btn_back;
    TextView login;

    FirebaseAuth auth;
    DatabaseReference references;

    ProgressDialog pd;

    String saldoAwal = "1.000.000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        re_email = (EditText)findViewById(R.id.et_email);
        re_username = (EditText)findViewById(R.id.et_username);
        re_password = (EditText)findViewById(R.id.et_password);
        btn_register = (Button)findViewById(R.id.btn_login_login);
        btn_back = (ImageView)findViewById(R.id.btn_profile_back);
        login = (TextView)findViewById(R.id.tv_login_daftar);
        re_repassword = (EditText)findViewById(R.id.et_repassword);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait.. ");
                pd.show();

                String txt_username = re_username.getText().toString();
                String txt_email = re_email.getText().toString();
                String txt_password = re_password.getText().toString();
                String txt_repassword = re_repassword.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "All field are required", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else if(!txt_password.equals(txt_repassword)){
                    Toast.makeText(RegisterActivity.this, "password and re-password is not match", Toast.LENGTH_SHORT).show();
                } else{
                    register(txt_username, txt_email, txt_password);
                    pd.dismiss();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                            hashMap.put("imageUrl","https://firebasestorage.googleapis.com/v0/b/handycrafts-shop.appspot.com/o/default.png?alt=media&token=a721ee3d-b599-40fc-aab7-f58cadc15861");
                            hashMap.put("saldo", saldoAwal);

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
                            pd.dismiss();
                        }
                    }
                });
    }
}
