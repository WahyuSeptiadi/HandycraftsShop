package com.newbie.handycraftsshop.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.newbie.handycraftsshop.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    /*
    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(LoginActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundColor(Color.parseColor("#1a1b29"))
                .withFooterText("CopyRight 2020")
                .withAfterLogoText("HandyCraft Shop")
                .withLogo(R.drawable.main_icon);

        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.WHITE);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
