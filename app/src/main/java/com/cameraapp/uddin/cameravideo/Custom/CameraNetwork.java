package com.cameraapp.uddin.cameravideo.Custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class CameraNetwork extends AsyncHttpClient {

    public Context mContext;
    public static String mServerUrl = "";
    public RequestParams mParams = null;
    public AlertDialog.Builder mBuilder = null;
    public LocalBroadcastManager mBroad = null;

    public String mWebsiteUrl = "http://18.221.221.116/api";
//    public String mWebsiteUrl = "http://192.168.0.218/api";
    public String mLogin = "/login";
    public String mRegister = "/register";
    public String mUpload = "/uploadvideo";
    public String mGetvideo = "/posturls";
    public String mRemove = "/removevideo";

    public CameraNetwork(Context context) {
        mContext = context;
    }

    public void login(String user, String password) {

        mServerUrl = mWebsiteUrl + mLogin;

        mParams = new RequestParams();
        mParams.put("name", user);
        mParams.put("password", password);

        post(mServerUrl, mParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                mBroad = LocalBroadcastManager.getInstance(mContext);

                Intent mIntent = new Intent("Authentication");

                if (statusCode == 200) {
                    Log.e("TAG", "Success");
                    mIntent.putExtra("Login", "Success");
                } else {
                    Log.e("TAG", "Fail");
                    mIntent.putExtra("Login", "Fail");
                }

                mBroad.sendBroadcast(mIntent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mBroad = LocalBroadcastManager.getInstance(mContext);

                Intent mIntent = new Intent("Authentication");
                mIntent.putExtra("Login", "Failure");
                mBroad.sendBroadcast(mIntent);
            }
        });
    }

    public void register(String user, String password) {

        mServerUrl = mWebsiteUrl + mRegister;

        mParams = new RequestParams();
        mParams.put("name", user);
        mParams.put("password", password);

        post(mServerUrl, mParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                mBroad = LocalBroadcastManager.getInstance(mContext);

                Intent mIntent = new Intent("Authentication");
                mIntent.putExtra("Register", "Success");
                mBroad.sendBroadcast(mIntent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                mBroad = LocalBroadcastManager.getInstance(mContext);

                Intent mIntent = new Intent("Authentication");
                mIntent.putExtra("Register", "Failure");
                mBroad.sendBroadcast(mIntent);
            }
        });
    }
}
