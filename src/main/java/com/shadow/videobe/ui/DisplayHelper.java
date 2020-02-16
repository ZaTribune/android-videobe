package com.shadow.videobe.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shadow.videobe.R;


public abstract class DisplayHelper {

    public static Offset centerOnParent(AppCompatActivity activity, View view){
        DisplayMetrics metrics=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthP=metrics.widthPixels;
        int heightP=metrics.heightPixels;
        int x = 0,y=0;
        view.measure(x,y);
        return new Offset((widthP-view.getMeasuredWidth())/2,(heightP-view.getMeasuredHeight())/2);
    }
    public static AlertDialog createProgressDialog(Context context,final String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View dialogView = inflater.inflate(R.layout.dialog_progress, null);
            builder.setView(dialogView);
            TextView textView = dialogView.findViewById(R.id.progressDialogText);
            if (textView != null) {
                textView.setText(text);
            }
        }
        builder.setCancelable(false);
        return builder.create();
    }
}
