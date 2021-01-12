package com.app.machinetest.presentation.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.app.machinetest.R;
import com.app.machinetest.presentation.home.EmployeeListActivity;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(SplashActivity.this,
                        EmployeeListActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}