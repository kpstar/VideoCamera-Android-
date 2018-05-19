package com.cameraapp.uddin.cameravideo.Custom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.Policy;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by saif on 7/22/2017.
 */

public class VideoCapture extends SurfaceView implements SurfaceHolder.Callback {

    private MediaRecorder recorder;
    private SurfaceHolder holder;
    public Context context;
    private Camera camera;
    public static String videoPath;
    int currentCameraId = 0;
    int currentFlash = 0;

    public VideoCapture(Context context) {
        super(context);
        this.context = context;
        startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public VideoCapture(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public VideoCapture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public void FlashMode(){
//        if (camera != null) {
//            stopCamera();
//        }
        camera.stopPreview();
        if (currentFlash == 0) {
            Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            currentFlash = 1;
        }else {
            Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            currentFlash = 0;
        }
//        camera = Camera.open();
//
//        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    public void switchCamera() {
        if (camera != null) {
            stopCamera();
        }
//        Camera.CameraInfo currentCamInfo = new Camera.CameraInfo();
//        int currentCameraId;
//        currentCameraId = currentCamInfo.facing;
        if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            currentCameraId = 1;
        }
        else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            currentCameraId = 0;
        }

//        startCamera(currentCameraId);

        camera = Camera.open(currentCameraId);

        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();



    }

    private void stopCamera(){
        if (camera != null){
            camera.stopPreview();
//            camera.setPreviewCallback(null);
            camera.release();
//            camera = null;
//            holder.removeCallback(listener);
//            holder = null;
        }
    }



    private void startCamera(int currentCameraId){
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera = getCameraInstance(currentCameraId); // camera.open()
        camera.setDisplayOrientation(90);
    }

    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
        Log.e("tag", "surfaceChanged");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("tag", "surfaceCreated");
        try {
//            Parameters parameters = camera.getParameters();
//            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
//            camera.setParameters(parameters);

            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCapturingVideo(int screenWidth){
        Log.e("tag", "startCapturingVideo");
        try {
            init(screenWidth);
            recorder.prepare();
            recorder.start();
            // save video path
            SharedPreferences prefs = context.getSharedPreferences("CameraApp", MODE_PRIVATE);
            prefs.edit().putString("recording_path", videoPath).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(int screenWidth) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/trendz");
            file.mkdirs();
            Random r = new Random();
            String name = (r.nextInt(9999)+1000)+".mp4";
            videoPath = Environment.getExternalStorageDirectory().getPath() +"/trendz/"+name;
            recorder = new MediaRecorder();
            recorder.setOrientationHint(90);
            recorder.setPreviewDisplay(holder.getSurface());
            camera.unlock();
            recorder.setCamera(camera);
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
            Toast.makeText(context, "Recording Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopCapturingVideo() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
            }
            if(camera != null){
//                camera.lock();
//                camera.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(5)
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.e("tag", "surfaceDestroyed");
    }

    private Camera getCameraInstance(int currentCameraId) {
        Camera c = null;
        try {
            c = Camera.open(currentCameraId); // attempt to get a Camera instance

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("tag", "Camera is not available");
        }
        return c;
    }

}