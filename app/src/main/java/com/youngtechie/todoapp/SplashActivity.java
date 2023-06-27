package com.youngtechie.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.youngtechie.todoapp.storage.SharedPreferenceClass;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    Handler h = new Handler();
    SharedPreferenceClass pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = new SharedPreferenceClass(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = pref.getValue_string("token");
                if (token.isEmpty()) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 5000);

    }
}