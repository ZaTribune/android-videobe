package com.shadow.videobe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheUtil {
    public static String saveBitmapInCache(File cacheDir, Bitmap bitmap){
        File outputImageFile;
        try {
            outputImageFile = File.createTempFile("videobe-", ".png", cacheDir);
            FileOutputStream out = new FileOutputStream(outputImageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.i("image file size",""+out.getChannel().size()/1024+" KB");
            Log.i("cache file path",""+outputImageFile.getPath());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Log.i("CACHE",outputImageFile.getPath());
        return outputImageFile.getPath();
    }
    public static String createTempImage(File cacheDir) {
        try {
            File file = File.createTempFile("vid", ".png", cacheDir);
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
