package com.shadow.videobe.player;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.shadow.videobe.R;
import com.shadow.videobe.data.entities.Video;

public class PlayerFragment extends Fragment {
    private static final String ARG_VIDEO_PATH = "ARG_VIDEO_PATH";
    private static final String ARG_VIDEO_CURRENT_POSITION = "ARG_VIDEO_CURRENT_POSITION";

    private String videoPath;
    private VideoView videoView;
    private int currentPosition = 0;
    private static final String PLAYBACK_TIME = "play_time";

    public PlayerFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        currentPosition=videoView.getCurrentPosition();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLAYBACK_TIME, videoView.getCurrentPosition());
    }
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String videoPath, int videoStart) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_PATH, videoPath);
        args.putInt(ARG_VIDEO_CURRENT_POSITION, videoStart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoPath = getArguments().getString(ARG_VIDEO_PATH);
            currentPosition = getArguments().getInt(ARG_VIDEO_CURRENT_POSITION,1);
        }
        if (savedInstanceState!=null)
            currentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_player, container, false);
        videoView=view.findViewById(R.id.videoView);
        // because Placing the MediaController only works if the video size is known.
        videoView.setOnPreparedListener(mp -> mp.setOnVideoSizeChangedListener((mp1, width, height) -> {
            MediaController mediaController=new MediaController(getContext());
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);


        }));
        initializePlayer();
        return view;
    }

    private void initializePlayer() {
        if (!videoPath.isEmpty()) {
            Log.i(PlayerFragment.class.getSimpleName(),"XXXXXXXXXXXXXXXXXXX");
            videoView.setVideoPath(videoPath);
            if (currentPosition > 0) {
                videoView.seekTo(currentPosition);
            } else {
                // Skipping to 1 shows the first frame of the video.
                videoView.seekTo(1);
            }
        }
    }
    private void releasePlayer() {
        videoView.stopPlayback();
    }


    public void play(String videoPath) {
        videoView.setVideoPath(videoPath);
        videoView.start();
    }
}
