package com.shadow.videobe.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.shadow.videobe.R;
import com.shadow.videobe.editing.EditingFragment;
import com.shadow.videobe.tools.ToolsFragment.ToolsFragmentListener;

public class ToolsRecyclerViewAdapter extends RecyclerView.Adapter<ToolsRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private int[] tools=new int[]{R.drawable.watermark,R.drawable.trim,
            R.drawable.adjust,R.drawable.audiotrack,R.drawable.transform,R.drawable.resize
    };
    private final ToolsFragmentListener mListener;
    private FragmentManager fragmentManager;

    ToolsRecyclerViewAdapter(Context context, ToolsFragmentListener listener, FragmentManager fragmentManager) {
        this.context=context;
        mListener = listener;
        this.fragmentManager=fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tool, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String tool=context.getResources().getResourceEntryName(tools[position]);
        holder.tv_name.setText(tool);
        setPic(holder.iv_content,tools[position]);
        holder.mView.findViewById(R.id.button_layout).setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onToolsInteraction(tool);
                fragmentManager
                        .beginTransaction()
                        .add(R.id.container,EditingFragment.newInstance(tool))
                        .addToBackStack(tool)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tools.length;
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
    private void setPic(ImageView imageView, int resource){
        Drawable drawable=context.getResources().getDrawable(resource);
        imageView.setImageDrawable(drawable);
    }
}
