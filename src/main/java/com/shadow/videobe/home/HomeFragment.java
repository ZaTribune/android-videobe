package com.shadow.videobe.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shadow.videobe.R;
import com.shadow.videobe.ui.MediaHelper;

public class HomeFragment extends Fragment {

    private Context context;

    public static String ARG_DIRECTORY ="ARG_DIRECTORY";
    private static final String ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT";
    private String mDirectory;
    private int mColumnCount;
    private int itemWidth = 100;
    private HomeVideosAdapter homeVideosAdapter;
    private HomeVideosListener homeVideosListener;

    public HomeVideosAdapter getHomeVideosAdapter() {
        return homeVideosAdapter;
    }

    public static Fragment newInstance(String directory, int columnCount) {
        HomeFragment fragment=new HomeFragment();
        Bundle args=new Bundle();
        args.putString(ARG_DIRECTORY,directory);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        if (getArguments()!=null){
            mDirectory = getArguments().getString(ARG_DIRECTORY);
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        RecyclerView rv_videos = view.findViewById(R.id.rv_videos);
        if (mColumnCount <= 1) {
            rv_videos.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),mColumnCount);
            rv_videos.setLayoutManager(gridLayoutManager);
        }
        homeVideosAdapter =new HomeVideosAdapter(context, homeVideosListener);
        rv_videos.setAdapter(homeVideosAdapter);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeVideosListener= (HomeVideosListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeVideosListener=null;
    }
}
