package com.cameraapp.uddin.cameravideo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cameraapp.uddin.cameravideo.Custom.CameraNetwork;
import com.cameraapp.uddin.cameravideo.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import in.arjsna.passcodeview.PassCodeView;

public class PasscodeActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    public static int REGISTER_STATUS = 0;
    public static int CONFIRM_STATUS = 1;
    public static int ENTER_STATUS = 2;

    public int RECORD_BUTTON_ID = 2001;
    public boolean isRecording = false;
    public boolean isCamera = false;

    public Switch mSwitch;
    public ImageButton mRecord;
    public TextView mTitle;
    public ConstraintLayout mPreviewLayout;
    public PassCodeView mPasscode;
    public TextView mTime;
    public SurfaceView mSurface;
    public SurfaceHolder mSurfaceHolder;
    public Camera mCamera;
    public PassCodeView passCodeView;
    public String PASSCODE = "";

    public SharedPreferences mShare;

    public String ext_Directory = Environment.getExternalStorageDirectory().getAbsolutePath();

    public MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        mSwitch = (Switch)findViewById(R.id.switchCamera);

        mRecord = (ImageButton)findViewById(R.id.recordBtn);
        mRecord.setId(RECORD_BUTTON_ID);
        mRecord.setOnClickListener(this);

        mPreviewLayout = (ConstraintLayout)findViewById(R.id.previewCam);
        mPreviewLayout.setVisibility(View.INVISIBLE);

        mSurface = (SurfaceView)findViewById(R.id.surfaceView);

        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        passCodeView = (PassCodeView)findViewById(R.id.pass_code_view);

        mTitle = (TextView)findViewById(R.id.titleTxt);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mShare = getSharedPreferences("CameraApp", Context.MODE_PRIVATE);
        int mState = mShare.getInt("Status", 0);
        initUI(mState);
        bindEvents();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == RECORD_BUTTON_ID) {

            if (isRecording) {
                mPreviewLayout.setVisibility(View.INVISIBLE);
                mRecord.setImageResource(R.drawable.play);
                recorder.stop();
                recorder.release();
            } else {
                recorder = new MediaRecorder();
                if (mCamera != null)
                    mCamera.release();
                mCamera = null;
                mCamera = openCamera(mSwitch.isChecked());
                mCamera.setDisplayOrientation(90);
                //Camera.Parameters params = new
                init();
                mPreviewLayout.setVisibility(View.VISIBLE);
                mRecord.setImageResource(R.drawable.stop);
                try {
                    recorder.prepare();
                    recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            isRecording = !isRecording;
        }
    }


    public void initUI(int status) {

        mPreviewLayout.setVisibility(View.INVISIBLE);
        switch (status) {
            case 0:
                mRecord.setVisibility(View.INVISIBLE);
                mSwitch.setVisibility(View.INVISIBLE);
                mTitle.setText("Register Passcode.");
                break;
            case 1:
                mRecord.setVisibility(View.INVISIBLE);
                mSwitch.setVisibility(View.INVISIBLE);
                mTitle.setText("Confirm Passcode.");
                break;
            case 2:
                mRecord.setVisibility(View.VISIBLE);
                mSwitch.setVisibility(View.VISIBLE);
                mTitle.setText("Enter the Passcode.");
                break;
        }
    }

    public Camera openCamera(boolean isFront) {

        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        if (cameraCount == 0) {
            Toast.makeText(this, "This device doesn't have camera.", Toast.LENGTH_SHORT).show();
            cam = null;
        } else if (cameraCount == 1) {
            mSwitch.setChecked(false);
            cam = Camera.open();
        } else {
            if (isFront) {
                cam = Camera.open(1);
            } else {
                cam = Camera.open();
            }
        }
        return cam;
    }

    public String getFilename() {
        Random r = new Random();
        String mName = (r.nextInt(9999)+1000)+".mp4";
        return mName;
    }

    public void init() {
        try {
            File file = new File(ext_Directory +"/cameravideo");
            file.mkdirs();
            String name = getFilename();
            String videoPath = ext_Directory +"/cameravideo/"+ name;
            recorder = new MediaRecorder();
            recorder.setOrientationHint(90);
            recorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            mCamera.unlock();
            recorder.setCamera(mCamera);
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            recorder.setVideoEncodingBitRate(48000000);
            recorder.setVideoSize(640, 480);
            recorder.setVideoFrameRate(30);
            recorder.setOutputFile(videoPath);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tag", "init error");
            Toast.makeText(this, "Recording Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("tag", "surfaceCreated");
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void bindEvents() {
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override public void onTextChanged(String text) {
                if (text.length() == 6) {
//                    if (text.equals(PASSCODE)) {
//                        Intent intent = new Intent(PasscodeActivity.this, NavigationAcitivty.class);
//                        startActivity(intent);
//                        PasscodeActivity.this.finish();
//                    } else {
//                        passCodeView.setError(true);
//                    }
                    Intent mIntent = new Intent(PasscodeActivity.this, NavigationAcitivty.class);
                    startActivity(mIntent);
                    finish();
                }
            }
        });
    }
}
