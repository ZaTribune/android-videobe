package com.shadow.videobe.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shadow.videobe.R;
import com.shadow.videobe.data.entities.Video;
import java.util.List;

import static com.shadow.videobe.SplashActivity.videoProvider;

public class HomeVideosAdapter extends RecyclerView.Adapter<HomeVideosAdapter.ViewHolder> {
    private Context context;
    private final HomeVideosListener mListener;
    private List<Video>videos;

    HomeVideosAdapter(Context context, HomeVideosListener listener) {
        this.context=context;
        mListener = listener;
        videos= videoProvider.getVideos();
    }
    public void refresh(){
        videoProvider.refresh();
        videos=videoProvider.getVideos();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Video video=videos.get(position);
        holder.tv_name.setText(video.getTitle());
        holder.iv_content.setImageBitmap(video.getSampleFrame());
        holder.mView.findViewById(R.id.button_layout).setOnClickListener(v -> {
            if (null != mListener) {
                mListener.playVideo(video.getPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView tv_name;
        final ImageView iv_content;

        ViewHolder(View view) {
            super(view);
            mView = view;
            tv_name = view.findViewById(R.id.tv_name);
            iv_content=view.findViewById(R.id.iv_content);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + tv_name.getText() + "'";
        }
    }
}
