package com.shadow.videobe.editing;

public interface EditingContract {
    interface View{
    void setPreview(String samplePath);
    void onStartEditing();
    void onFinishedEditing(String videoPath);
    }
}
