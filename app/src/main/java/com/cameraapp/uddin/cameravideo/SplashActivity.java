package com.cameraapp.uddin.cameravideo;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cameraapp.uddin.cameravideo.Activity.LoginActivity;

import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mIntent);
                SplashActivity.this.finish();
            }
        }, 1500);
    }
}
