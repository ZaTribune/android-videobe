package com.shadow.videobe.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.shadow.videobe.R;


/**
 * Created by Raid_2209ee on 29/03/2017.
 */

public class AllAdapter extends BaseAdapter {
    private Context context;
    private int[] drawables;
    private String[] texts;

    public AllAdapter(Context context, int[] drawables, String[] texts){
        this.context=context;
        this.drawables = drawables;
        this.texts=texts;
    }

    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_list_item_1,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(texts[position]);
        Drawable drawable=context.getResources().getDrawable(drawables[position]);
        drawable.setBounds(0,0,50,50);
        holder.textView.setCompoundDrawables(drawable,null,null,null);
        holder.textView.refreshDrawableState();

        return convertView;
    }
    private class ViewHolder {
        final View mView;
        final TextView textView;
        ViewHolder(View view){
            mView = view;
            textView = view.findViewById(R.id.list_item_1_txt);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + textView.getText() + "'";
        }
    }

}

