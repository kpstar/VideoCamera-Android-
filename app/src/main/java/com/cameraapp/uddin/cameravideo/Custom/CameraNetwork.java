package com.cameraapp.uddin.cameravideo.Custom;

import android.app.AlertDialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class CameraNetwork extends AsyncHttpClient {

    public Context mContext;
    public static String mServerUrl = "";
    public RequestParams mParams = null;
    public AlertDialog.Builder mBuilder = null;

    public String mWebsiteUrl = "http://18.221.221.116/api";
    public String mLogin = "/login";
    public String mRegister = "/register";
    public String mUpload = "/uploadvideo";
    public String mGetvideo = "/posturls";
    public String mRemove = "/removevideo";

    public CameraNetwork(Context context) {
        mContext = context;
        mBuilder = new AlertDialog.Builder(mContext);
    }

    public void login(String user, String password) {

        mServerUrl = mWebsiteUrl + mLogin;

        mParams = new RequestParams(mContext);
        mParams.put("name", user);
        mParams.put("password", password);

        post(mServerUrl, mParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mBuilder.setTitle("Error")
                        .setMessage("Please check network connection");
                mBuilder.show();
            }
        });
    }

    public void register(String user, String password) {

    }
}
