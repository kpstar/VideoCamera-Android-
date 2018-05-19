package com.cameraapp.uddin.cameravideo.Activity;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Environment;
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

    public String ext_Directory = Environment.getExternalStorageDirectory().getAbsolutePath();

    public MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        mSwitch = (Switch)findViewById(R.id.switchCamera);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    mCamera = openCamera(true);
                } else {
                    mCamera = openCamera(false);
                }

                if (mCamera != null)
                    mCamera.setDisplayOrientation(90);
            }
        });

        mRecord = (ImageButton)findViewById(R.id.recordBtn);
        mRecord.setId(RECORD_BUTTON_ID);
        mRecord.setOnClickListener(this);

        mPreviewLayout = (ConstraintLayout)findViewById(R.id.previewCam);
        mPreviewLayout.setVisibility(View.INVISIBLE);

        mSurface = (SurfaceView)findViewById(R.id.surfaceView);

        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        recorder = new MediaRecorder();
        mCamera = openCamera(false);
        mCamera.setDisplayOrientation(90);
        //Camera.Parameters params = new
        init();

        isCamera = mSwitch.isChecked();
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
                mPreviewLayout.setVisibility(View.VISIBLE);
                mRecord.setImageResource(R.drawable.stop);
                try {
                    //mCamera.startPreview();
                    recorder.prepare();
                    recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            isRecording = !isRecording;
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
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (isFront) {
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        try {
                            cam = Camera.open(camIdx);
                            return cam;
                        } catch (RuntimeException e) {
                            Log.e("TAG", "Camera failed to open: " + e.getLocalizedMessage());
                        }
                    }
                } else {
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        try {
                            cam = Camera.open(camIdx);
                            return cam;
                        } catch (RuntimeException e) {
                            Log.e("TAG", "Camera failed to open: " + e.getLocalizedMessage());
                        }
                    }
                }
            }
        }
        return cam;
    }

    public void init() {
        try {
            File file = new File(ext_Directory +"/cameravideo");
            file.mkdirs();
            Random r = new Random();
            String name = (r.nextInt(9999)+1000)+".mp4";
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
}
