package com.shadow.videobe.editing;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.shadow.videobe.data.entities.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoProvider {
    private MediaMetadataRetriever mRetriever;
    private List<Video>videos;

    public List<Video> getVideos() {
        return videos;
    }

    public VideoProvider(){
        mRetriever = new MediaMetadataRetriever();
        videos=new ArrayList<>();
    }

    public Video getVideoFromPath(String videoPath){
        //System.out.println(videoPath);
        //todo fix corrupted videos and extensions
        File file=new File(videoPath);
        try {
            mRetriever.setDataSource(videoPath);
        }catch (Exception e){
            return null;
        }

        String time = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Video video=new Video();
        video.setPath(videoPath);
        video.setTitle(file.getName());
        video.setTime(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(time)));
        video.setSampleFrame(mRetriever.getFrameAtTime());//todo set another time
        return video;
    }
    public File[] getVideosFromApplicationFolder(){
        String root;
        if (Environment.getExternalStorageDirectory() != null)
            root = Environment.getExternalStorageDirectory().toString();
        else
            root = Environment.getDataDirectory().toString();
        File myDir = new File(root + "/videobe");
        myDir.mkdirs();
        File[]files=myDir.listFiles();
        assert files != null;
        return files;
    }
    public void refresh(){
        getVideos().clear();
        File[] files=getVideosFromApplicationFolder();
        for (File file:files) {
                getVideos().add(getVideoFromPath(file.getPath()));
        }
    }
}
