package com.cameraapp.uddin.cameravideo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cameraapp.uddin.cameravideo.Activity.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getPermissions();

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

    public void getPermissions() {


    }
}
