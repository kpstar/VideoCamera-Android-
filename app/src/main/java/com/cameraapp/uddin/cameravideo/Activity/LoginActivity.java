package com.cameraapp.uddin.cameravideo.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cameraapp.uddin.cameravideo.Custom.CameraNetwork;
import com.cameraapp.uddin.cameravideo.R;

import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static int LogInBtnID = 1001;
    public static int RegisterBtnId = 1002;
    public SharedPreferences mShare;
    Intent mIntent = null;

    EditText edtUser, edtPasswd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getPermissions();

        Button loginBtn = (Button)findViewById(R.id.btnSign);
        loginBtn.setId(LogInBtnID);
        loginBtn.setOnClickListener(this);

        Button registerBtn = (Button)findViewById(R.id.btnRegister);
        registerBtn.setId(RegisterBtnId);
        registerBtn.setOnClickListener(this);

        edtUser = (EditText)findViewById(R.id.edtUsername);
        edtPasswd = (EditText)findViewById(R.id.edtPassword);

        mShare = getSharedPreferences("CameraApp", Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissions() {

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CAPTURE_VIDEO_OUTPUT, Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions, 0);
        }
    }

    @Override
    public void onClick(View v) {

        int mId = v.getId();
        String error = "";
        String mUser = edtUser.getText().toString();
        String mPasswd = edtPasswd.getText().toString();

        if (mUser.isEmpty()) {
            error = "Please insert username.";
        }

        if (mPasswd.isEmpty()) {
            error = "Please insert password.";
        }

        if (!error.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Alert")
                    .setMessage(error)
                    .setNeutralButton("OK", null);
            builder.create();
            builder.show();
            return;
        }

        CameraNetwork mNetwork = new CameraNetwork(this);

        if (mId == LogInBtnID) {
            //  LogIn button Pressed

            mNetwork.login(mUser, mPasswd);
        } else {
            //  Register Button Pressed

            mNetwork.register(mUser, mPasswd);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("Authentication")
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        String temp_user = mShare.getString("Username", "");

        if (!temp_user.isEmpty()) {
            mIntent = new Intent(this, PasscodeActivity.class);
            startActivity(mIntent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String mUser = edtUser.getText().toString();
            String mPasswd = edtPasswd.getText().toString();
            edtUser.setText("");
            edtPasswd.setText("");

            String temp_desc = "";

            temp_desc = intent.getStringExtra("Login");

            if (temp_desc!= null) {
                inputData(mUser, mPasswd);
                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            temp_desc = intent.getStringExtra("Register");

            if (temp_desc!=null) {
                inputData(mUser, mPasswd);
                Toast.makeText(LoginActivity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    };

    public void inputData(String user, String password) {

        SharedPreferences.Editor editor = mShare.edit();
        editor.putInt("CodeStatus", 0);
        editor.putString("Username", user);
        editor.putString("Password", password);
        editor.apply();

        mIntent = new Intent(this, PasscodeActivity.class);
        startActivity(mIntent);
        finish();
    }
}
