package com.shadow.videobe.tools.functions.watermark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageWriter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import com.shadow.videobe.MainActivity;
import com.shadow.videobe.SplashActivity;
import com.shadow.videobe.data.entities.Video;
import com.shadow.videobe.ui.MediaHelper;
import com.shadow.videobe.R;
import com.shadow.videobe.editing.EditingFragment;
import com.shadow.videobe.tools.ToolsFragment;
import com.shadow.videobe.utils.CacheUtil;
import com.shadow.videobe.utils.HintsUtil;
import com.shadow.videobe.utils.PermissionModel;

import java.io.File;
import java.util.Objects;

public class WatermarkFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int SD_READ_REQUEST = 2;
    private static final int SD_WRITE_REQUEST = 4;
    private WatermarkModel watermarkModel;
    private String videoSamplePath;
    private Button btn_start;
    private ImageView iv_selectPic;
    //private ImageView iv_watermark;
    private TextView iv_picWidth, iv_picHeight;
    private ImageButton incPosX,decPosX, incPosY,decPosY,top_left,top_right,bottom_left,bottom_right,center,upScale,downScale;
    private int posX,posY=10;
    //todo:communication is better
    private Video video;
    private String picPath;
    private Bitmap watermarkBitmap;
    private PermissionModel permissionModel;
    private Context context;
    private EditingFragment parent;

    public WatermarkFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static WatermarkFragment newInstance(String param1, String param2) {
        WatermarkFragment fragment = new WatermarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent=((EditingFragment) getParentFragment());
        if (parent!=null)
        context=getContext();
        watermarkModel =new WatermarkModel(context,parent);//the editing fragment
        permissionModel=new PermissionModel(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watermark, container, false);
        btn_start = parent.getStartButton();//starting edit
        iv_selectPic = view.findViewById(R.id.iv_selectPic);
        //iv_watermark = view.findViewById(R.id.iv_watermark);
        iv_picWidth=view.findViewById(R.id.iv_picWidth);
        iv_picHeight=view.findViewById(R.id.iv_picHeight);
        incPosX = view.findViewById(R.id.incPosX);
        decPosX=view.findViewById(R.id.decPosX);
        incPosY = view.findViewById(R.id.incPosY);
        decPosY=view.findViewById(R.id.decPosY);
        upScale=view.findViewById(R.id.upScale);
        downScale=view.findViewById(R.id.downScale);
        top_left=view.findViewById(R.id.top_left);
        top_right=view.findViewById(R.id.top_right);
        bottom_left=view.findViewById(R.id.bottom_left);
        bottom_right=view.findViewById(R.id.bottom_right);
        center=view.findViewById(R.id.center);
        setControlsEnabled(false);

        iv_selectPic.setOnClickListener(v -> {
            video = parent.getVideo();
                if (parent.getVideo()==null) {//means no picture displayed on the ImageView
                    HintsUtil.createFragmentHint(WatermarkFragment.this,view.getRootView(), R.id.btn_selectVideo, "Note", "Please select a video first! .");
                    return;
                }
            permissionModel.checkPermissionReadStorage();
            if (SplashActivity.storageReadPermission) {
                Log.i(ToolsFragment.class.getSimpleName(), "storageReadPermission Access Storage granted");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// to filter only images
                startActivityForResult(intent, SD_READ_REQUEST);
            }

        });
        btn_start.setOnClickListener(v -> {
            permissionModel.checkPermissionWriteStorage();
            if (SplashActivity.storageWritePermission) {
                //On my phone the output directory cold only be customized in internal memory
                //the App create a folder named "videobe" on specified memory
                //the only way to have output files created on the SD card (if available) is by writing to an app specific directory on the SD card. In my case it would be
                ///storage/-some SD card id-/Android/data/com.shadow.video_tools <- main package name
                String root;
                if (Environment.getExternalStorageDirectory() != null)
                    root = Environment.getExternalStorageDirectory().toString();
                else
                    root = Environment.getDataDirectory().toString();

                File myDir = new File(root + "/videobe");
                myDir.mkdirs();
                //output name must be unique so I used the Date as an id concatenated to the App name
                //used the amazing Base64 to generate a unique String sequence from the current value of Date
                watermarkModel.createEditedVideo(video,CacheUtil.saveBitmapInCache(context.getCacheDir(),watermarkBitmap),posX,posY,myDir.getPath());
                System.out.println("before compression : "+watermarkBitmap.getByteCount());

            }
        });
        return view;
    }//end of createView()

    private void setControlsEnabled(boolean isEnabled) {
        btn_start.setEnabled(isEnabled);
        incPosX.setEnabled(isEnabled);
        decPosX.setEnabled(isEnabled);
        incPosY.setEnabled(isEnabled);
        decPosY.setEnabled(isEnabled);
        upScale.setEnabled(isEnabled);
        downScale.setEnabled(isEnabled);
        top_left.setEnabled(isEnabled);
        top_right.setEnabled(isEnabled);
        bottom_left.setEnabled(isEnabled);
        bottom_right.setEnabled(isEnabled);
        center.setEnabled(isEnabled);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SD_READ_REQUEST) {
            Log.i(WatermarkFragment.class.getSimpleName(), "result of requesting a pic");
            if (data != null) {
                Uri selectedImage = data.getData();//ex.  content://media/external/images/media/397
                String[] projection = {MediaStore.Images.Media.DATA};//  _data     //length is 1
                if (selectedImage != null) {
                    Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(selectedImage,//Uri->From table
                            projection,//projection--> columns to be returned by the query
                            null,//selection
                            null,//selection args
                            null//sortOrder
                    );
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);// we only specified one column in the projection that's the _data column that stores the filepath
                    picPath = cursor.getString(columnIndex);//ex. /storage/sdcard1/DCIM/Camera/IMG_20170706_184224_1.jpg
                    cursor.close();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;//true will calculate only
                    //Returns null, sizes are in the options variable
                    BitmapFactory.decodeFile(picPath, options);
                    watermarkBitmap = MediaHelper.decodeSampledBitmapFromPath(picPath, video.getWidth(), video.getHeight());
                    if (options.outWidth != watermarkBitmap.getWidth() || options.outHeight != watermarkBitmap.getHeight()) {
                        Toast.makeText(getContext(), "Image resized to " + watermarkBitmap.getWidth() + "*" + watermarkBitmap.getHeight() + " to fit video.", Toast.LENGTH_SHORT).show();
                    }
                    iv_picWidth.setText(String.valueOf(watermarkBitmap.getWidth()));
                    iv_picHeight.setText(String.valueOf(watermarkBitmap.getHeight()));
                    //If you want, the MIME type will also be decoded (if possible)
                    //String type = options.outMimeType;
                    videoSamplePath=CacheUtil.saveBitmapInCache(context.getCacheDir(),video.getSampleFrame());
                    preview(posX, posY);
                    initializeControls();
                }
            }
        }
    }
    private void preview(int x, int y){
        new Thread(()->{
        watermarkModel.getSampledOverlay(
                 CacheUtil.saveBitmapInCache(context.getCacheDir(),watermarkBitmap)
                ,videoSamplePath
                ,x,y);
        }).start();
    }
    private void initializeControls(){
            setControlsEnabled(true);
            incPosX.setOnClickListener(v->{
                if (video.getWidth()-posX+watermarkBitmap.getWidth()+10>0) {
                    posX+=10;
                        preview(posX, posY);
                }
            });
            decPosX.setOnClickListener(v->{
                if (posX+10>0) {
                    posX-=10;
                    preview(posX, posY);
                }
            });
            incPosY.setOnClickListener(v -> {
                if (video.getHeight()-posY+watermarkBitmap.getHeight()+10>0) {
                    posY+=10;
                    preview(posX, posY);
                }
            });
            decPosY.setOnClickListener(v->{
                if (posY+10>0) {
                    posY-=10;
                    preview(posX, posY);
                }
            });
            top_left.setOnClickListener(v->{
                posX=10;
                posY=10;
                preview(posX,posY);
            });
            top_right.setOnClickListener(v->{
                posX=video.getWidth()-watermarkBitmap.getWidth()-10;
                posY=10;
                preview(posX,posY);
            });
            bottom_left.setOnClickListener(v->{
                posX=10;
                posY=video.getHeight()-watermarkBitmap.getHeight()-10;
                preview(posX,posY);
            });
            bottom_right.setOnClickListener(v->{
                posX=video.getWidth()-watermarkBitmap.getWidth()-10;
                posY=video.getHeight()-watermarkBitmap.getHeight()-10;
                preview(posX,posY);
            });
            center.setOnClickListener(v->{
                posX=video.getWidth()/2-watermarkBitmap.getWidth()/2;
                posY=video.getHeight()/2-watermarkBitmap.getHeight()/2;
                preview(posX,posY);
            });
            upScale.setOnClickListener(v->{
                int scaledWidth= (int) (watermarkBitmap.getWidth()+(watermarkBitmap.getWidth()*0.1));
                int scaledHeight= (int) (watermarkBitmap.getHeight()+(watermarkBitmap.getHeight()*0.1));
                if (scaledWidth<video.getWidth()&&scaledHeight<video.getHeight()){
                    watermarkBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picPath),scaledWidth,scaledHeight,true);
                    preview(posX,posY);
                }
            });
            downScale.setOnClickListener(v->{
                int scaledWidth= (int) (watermarkBitmap.getWidth()-(watermarkBitmap.getWidth()*0.1));
                int scaledHeight= (int) (watermarkBitmap.getHeight()-(watermarkBitmap.getHeight()*0.1));
                if (scaledWidth>0&&scaledHeight>0){
                    watermarkBitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picPath),scaledWidth,scaledHeight,true);
                    preview(posX,posY);
                }
            });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

