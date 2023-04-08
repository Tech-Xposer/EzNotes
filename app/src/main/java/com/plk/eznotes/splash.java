package com.plk.eznotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class splash extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    finish();
                    startActivity(new Intent(splash.this, MainActivity.class));
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}