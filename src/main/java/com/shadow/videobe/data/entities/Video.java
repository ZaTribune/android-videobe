package com.shadow.videobe.data.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    private String path;
    private String title;
    private int width = 0;
    private int height = 0;
    private long time = 0L;
    private Bitmap sampleFrame;
    public static final Parcelable.Creator<Video> CREATOR =
            new Parcelable.Creator<Video>() {
                public Video createFromParcel(Parcel in) {
                    return new Video(in);
                }
                public Video[] newArray(int size) {
                    return new Video[size];
                }
            };
    public Video(){

    }
    public Video(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel in) {
        path = in.readString();
        title = in.readString();
        width = in.readInt();
        height = in.readInt();
        time = in.readLong();
        sampleFrame = in.readParcelable(getClass().getClassLoader());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = sampleFrame.getHeight();
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = sampleFrame.getHeight();
    }

    public long getDurationInSec() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Bitmap getSampleFrame() {
        return sampleFrame;
    }

    public void setSampleFrame(Bitmap sampleFrame) {
        this.sampleFrame = sampleFrame;
        this.width = this.sampleFrame.getWidth();
        this.height = this.sampleFrame.getHeight();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(title);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeLong(time);
        parcel.writeParcelable(sampleFrame, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
    }
}
