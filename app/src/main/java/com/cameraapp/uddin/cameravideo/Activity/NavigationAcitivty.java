package com.cameraapp.uddin.cameravideo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.cameraapp.uddin.cameravideo.R;


public class NavigationAcitivty extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public SharedPreferences mShare;
    Intent mIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_acitivty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Videos");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        mShare = getSharedPreferences("CameraApp", 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_acitivty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myvideo) {
            presentMyVideo();
        } else if (id == R.id.nav_uploadedvideo) {
            presentUploadedVideo();
        } else if (id == R.id.nav_changepasscode ) {
            changePasscode();
        } else {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void presentMyVideo() {

        getSupportActionBar().setTitle("My Videos");
    }

    private void presentUploadedVideo() {

        getSupportActionBar().setTitle("Uploaded Videos");
    }

    private void changePasscode() {


        SharedPreferences.Editor editor = mShare.edit();
        editor.putInt("Status", 0);
        editor.apply();

        mIntent = new Intent(NavigationAcitivty.this, PasscodeActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void signOut() {

        SharedPreferences.Editor editor = mShare.edit();
        editor.remove("Username");
        editor.remove("Password");
        editor.remove("Passcode");
        editor.remove("Status");
        editor.apply();
        mIntent = new Intent(NavigationAcitivty.this, LoginActivity.class);
        startActivity(mIntent);
        finish();
    }
}
