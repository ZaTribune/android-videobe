package com.shadow.videobe.editing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shadow.videobe.R;
import com.shadow.videobe.SplashActivity;
import com.shadow.videobe.data.entities.Video;
import com.shadow.videobe.home.HomeVideosListener;
import com.shadow.videobe.tools.ToolsFragment;
import com.shadow.videobe.tools.functions.watermark.WatermarkFragment;
import com.shadow.videobe.ui.DisplayHelper;
import com.shadow.videobe.ui.MediaHelper;
import com.shadow.videobe.utils.CacheUtil;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class EditingFragment extends Fragment implements EditingContract.View{
    private Context context;
    public HomeVideosListener homeVideosListener;
    private static final String ARG_TOOL = "tool";
    public static final int SD_REQUEST_VIDEO = 1;
    private TextView tv_videoWidth,tv_videoHeight;
    private ImageView ivVideoSample;
    private Button btn_ok;
    private VideoProvider videoProvider;
    private Video video;

    // TODO: parameters
    private String tool;
    public EditingFragment() {

    }

    public static EditingFragment newInstance(String tool) {
        EditingFragment fragment = new EditingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TOOL, tool);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        if (getArguments() != null) {
            tool =getArguments().getString(ARG_TOOL,"defaultTool");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_editing, container, false);
        TextView tv_tool = view.findViewById(R.id.tv_tool);
        tv_videoWidth=view.findViewById(R.id.tv_videoWidth);
        tv_videoHeight=view.findViewById(R.id.tv_videoHeight);
        ivVideoSample=view.findViewById(R.id.ivVideoSample);
        ImageButton btn_selectVideo = view.findViewById(R.id.btn_selectVideo);
        btn_ok =view.findViewById(R.id.btn_ok);
        btn_selectVideo.setOnClickListener(v -> {
            if(SplashActivity.storageReadPermission) {
                //if u try to add another client, this intent wont fire again as the value of permission is reset again
                Log.i(ToolsFragment.class.getSimpleName(),"permission Access Storage granted");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, SD_REQUEST_VIDEO);
            }
        });
        tv_tool.setText(tool);
        switch (tool){
            case "watermark":
                getChildFragmentManager().beginTransaction().replace(R.id.functionFragment,WatermarkFragment.newInstance("",""),tool).commit();
                break;
            case "crop":
                System.out.println("C");
                break;
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeVideosListener) {
            homeVideosListener = (HomeVideosListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeVideosListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeVideosListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == SD_REQUEST_VIDEO) {
            Log.i(WatermarkFragment.class.getSimpleName(),"result of requesting a video");
            if(data!=null) {
                Uri selectedVideo = data.getData();//ex.  content://media/external/images/media/397
                String[] projection = {MediaStore.Video.Media.DATA};//  _data     //length is 1
                if (selectedVideo != null) {
                    Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(selectedVideo,//Uri->From table
                            projection,//projection--> columns to be returned by the query
                            null,//selection
                            null,//selection args
                            null//sortOrder
                    );
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);// we only specified one column in the projection that's the _data column that stores the filepath
                    videoProvider=new VideoProvider();
                    video=videoProvider.getVideoFromPath(cursor.getString(columnIndex));  //ex. /storage/sdcard1/DCIM/Camera/IMG_20170706_184224_1.jpg
                    Bitmap bitmap=MediaHelper.decodeSampledBitmapFromPath(
                            CacheUtil.saveBitmapInCache(context.getCacheDir(),video.getSampleFrame())
                            ,ivVideoSample.getWidth()
                            ,ivVideoSample.getHeight());
                    video.setSampleFrame(bitmap);
                    ivVideoSample.setImageBitmap(bitmap);//then it must be saved in the cache, and the path is saved also
                    tv_videoWidth.setText(String.valueOf(video.getWidth()));
                    tv_videoHeight.setText(String.valueOf(video.getHeight()));
                    cursor.close();
                }
            }
        }
    }

    public Button getStartButton() {
        return btn_ok;
    }

    public Video getVideo(){return video;}

    @Override
    public void setPreview(String samplePath) {
        ivVideoSample.setImageBitmap(BitmapFactory.decodeFile(samplePath));
    }

    @Override
    public void onStartEditing() {

    }

    @Override
    public void onFinishedEditing(String videoPath) {
        homeVideosListener.onVideoCreated(videoPath);
        Toast.makeText(context, "File saved on " + videoPath, Toast.LENGTH_SHORT).show();
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm_play);
        dialog.show();
        //because we have three
        dialog.findViewById(R.id.btn_ok).setOnClickListener(v->{
            homeVideosListener.playVideo(videoPath);//the MainActivity listener
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v->dialog.dismiss());
    }
}
