package com.shadow.videobe.tools.functions.watermark;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.shadow.videobe.R;
import com.shadow.videobe.data.entities.Video;
import com.shadow.videobe.editing.EditingContract;
import com.shadow.videobe.ui.DisplayHelper;
import com.shadow.videobe.utils.CacheUtil;
import com.shadow.videobe.utils.NotificationProvider;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WatermarkModel {
    private Context context;
    private FFmpeg fFmpeg;
    private String TAG;
    private EditingContract.View editingListener;
    private NotificationProvider notificationProvider;
    private String previewPath;

    public WatermarkModel(Context context, EditingContract.View editingListener) {
        this.context = context;
        this.editingListener =editingListener;
        fFmpeg=FFmpeg.getInstance(context);
        notificationProvider=new NotificationProvider(context);
        TAG=WatermarkModel.class.getSimpleName();
        previewPath= CacheUtil.createTempImage(context.getCacheDir());
        try {
            fFmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {
                    System.out.println("onStart");
                }

                @Override
                public void onFailure() {
                    System.out.println("onFailure");
                }

                @Override
                public void onSuccess() {
                    System.out.println("onSuccess");
                }

                @Override
                public void onFinish() {
                    System.out.println("onFinish");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
            Toast.makeText(context,"FFmpeg not supported.",Toast.LENGTH_LONG).show();
        }
    }

    void getSampledOverlay(String watermarkPath, String videoSamplePath, int posX, int posY){

        try {
            fFmpeg.execute(
                    //todo:modify the attributes of "overlay" to specify the watermark location
                    new String[]{"-y","-i", videoSamplePath, "-i", watermarkPath, "-filter_complex", "[0:v][1:v]overlay="+posX+":"+posY, "-preset", "ultrafast", previewPath}
                    , new FFmpegExecuteResponseHandler() {
                        @Override
                        public void onSuccess(String message){
                            editingListener.setPreview( previewPath);
                        }
                        @Override
                        public void onProgress(String message) {}
                        @Override
                        public void onFailure(String message) {
                            Log.i(TAG, message);
                        }
                        @Override
                        public void onStart() {}
                        @Override
                        public void onFinish() {}
                    });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.getMessage();
        }
    }

    void createEditedVideo(Video video, String watermarkPath, int posX, int posY, String saveToDir) {
        String code = Base64.encodeToString(new Date().toString().getBytes(), Base64.NO_PADDING);
        String outputVideoName = "videobe-" + code + ".mp4";
        try {
            fFmpeg.execute(
                    //todo:modify the attributes of "overlay" to specify the watermark location
                    new String[]{"-i",video.getPath(),
                                 "-i", watermarkPath,
                                 "-filter_complex", "overlay=x="+posX+":y="+posY, "-preset",
                                 "ultrafast",saveToDir+"/"+outputVideoName}
                    , new FFmpegExecuteResponseHandler() {
                        AlertDialog progressDialog= DisplayHelper.createProgressDialog(context,"Processing Video");
                        TextView progressDialogText;
                        ProgressBar progressBar;
                        int id=(int)System.currentTimeMillis();
                        @Override
                        public void onSuccess(String message) {
                            editingListener.onFinishedEditing(saveToDir+"/"+outputVideoName);
                        }
                        @Override
                        public void onProgress(String message) {
                            if (progressDialogText != null&&progressBar!=null&&notificationProvider!=null) {
                                long progress=getProgress(message,video.getDurationInSec());
                                if (progress!=0) {
                                    progressDialogText.setText(String.format(Locale.getDefault(), "%s%d",context.getString(R.string.encoding_video),progress));
                                    progressBar.setProgress((int) progress);
                                    notificationProvider.publishProgress(id,progress,"","");
                                }
                            }
                        }
                        @Override
                        public void onFailure(String message) {
                            progressDialog.cancel();
                            Toast.makeText(context, "Error:" + message, Toast.LENGTH_SHORT).show();
                            Log.i("error", message);
                        }
                        @Override
                        public void onStart() {
                            progressDialog.show();
                            notificationProvider.createProgressNotification("Videobe","Processing Video...",id);
                            progressDialogText=progressDialog.findViewById(R.id.progressDialogText);
                            progressBar=progressDialog.findViewById(R.id.progressBar);
                            progressDialog.findViewById(R.id.btn_cancel).setOnClickListener(v->{
                                progressDialog.cancel();
                                fFmpeg.killRunningProcesses();
                                File file=new File(video.getPath());
                                if (file.exists())
                                    file.delete();
                                notificationProvider.cancelNotificationProgress(id);
                            });
                            progressDialog.findViewById(R.id.btn_background).setOnClickListener(v-> progressDialog.dismiss());
                        }
                        @Override
                        public void onFinish() {
                            progressDialog.dismiss();
                            // When the loop is finished, updates the notification
                            // Removes the progress bar
                            notificationProvider.publishProgress(id,100,"Completed","");
                        }
                    });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private long getProgress(String message,long videoLengthInSec) {
        if (message.contains("speed")) {
            Pattern pattern = Pattern.compile("time=([\\d\\w:]+)");
            Matcher matcher = pattern.matcher(message);
            matcher.find();
            String tempTime = String.valueOf(matcher.group(1));
            //Log.d(TAG, "getProgress: tempTime " + tempTime);
            String[] arrayTime = tempTime.split(":");
            long currentTime =
                    TimeUnit.HOURS.toSeconds(Long.parseLong(arrayTime[0]))
                            + TimeUnit.MINUTES.toSeconds(Long.parseLong(arrayTime[1]))
                            + Long.parseLong(arrayTime[2]);
            return 100 * currentTime/videoLengthInSec;
        }
        return 0;
    }
}
