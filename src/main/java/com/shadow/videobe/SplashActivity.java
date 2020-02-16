package com.shadow.videobe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shadow.videobe.data.entities.Video;
import com.shadow.videobe.editing.VideoProvider;
import com.shadow.videobe.utils.PermissionModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private PermissionModel permissionModel;
    public static boolean storageReadPermission;
    public static boolean storageWritePermission;
    public static VideoProvider videoProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        permissionModel = new PermissionModel(this);
        permissionModel.checkPermissionReadStorage();
        permissionModel.checkPermissionWriteStorage();
        if (videoProvider==null) {
            videoProvider=new VideoProvider();
            File[]files=videoProvider.getVideosFromApplicationFolder();
            progressBar.setMax(files.length);
            VideosTask videosTask = new VideosTask();
            videosTask.execute(videoProvider.getVideosFromApplicationFolder());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            Toast.makeText(SplashActivity.this,"Please provide permissions before using the App.",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    @SuppressLint("StaticFieldLeak")
    class VideosTask extends AsyncTask<File,Integer,Integer> {
        @Override
        protected void onPreExecute() {
            imageView.startAnimation( AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade));
        }

        @Override
        protected Integer doInBackground(File... files) {
            for (int i=0;i<files.length;i++) {
                if (videoProvider.getVideoFromPath(files[i].getPath())!=null) {
                    videoProvider.getVideos().add(videoProvider.getVideoFromPath(files[i].getPath()));
                    publishProgress(i);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Intent intent=new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }

}
