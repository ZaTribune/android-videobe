package com.shadow.videobe.home;

public interface HomeVideosListener {
    void onVideoDeleted(String videoPath);
    void onVideoCreated(String videoPath);
    void playVideo(String videoPath);
}
