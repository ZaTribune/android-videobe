package com.shadow.videobe.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.shadow.videobe.R;

public class NotificationProvider {
    private Context context;
    private NotificationManager manager;
    private String channelId;
    private NotificationCompat.Builder builder;

    public NotificationProvider(Context context) {
        this.context = context;
        channelId = "Videobe";
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert manager != null;
            if (manager.getNotificationChannel(channelId)==null) {
                CharSequence name = context.getString(R.string.channel_name);
                String description = context.getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                manager.createNotificationChannel(channel);
            }
        }
    }
    public void createProgressNotification(String title,String text,int id){
        builder = new NotificationCompat.Builder(context,channelId);
        builder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.icons);
        builder.setOnlyAlertOnce(true);
        // Sets the progress indicator to a max value,
        // the current completion percentage and "determinate" state
        builder.setProgress(100,0, false);
        manager.notify(id, builder.build());// gets you the sound and display of notification
    }
    public void cancelNotificationProgress(int id){
        manager.cancel(id);
    }
    public void publishProgress(int id, long progress,String title,String content) {
        if (!title.isEmpty()){
            builder.setContentTitle(title);
            builder.setContentText(content);
        }
        if (progress==100) {//when completed
            builder.setProgress(0, 0, false);
        }else
            builder.setProgress(100, (int)progress, false);
        manager.notify(id,builder.build());
    }
}
