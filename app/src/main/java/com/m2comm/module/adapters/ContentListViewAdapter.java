package com.m2comm.module.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m2comm.kses_exercise.R;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.MenuDTO;

import java.util.ArrayList;

public class ContentListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MenuDTO> contentArray;


    public ContentListViewAdapter(Context context, LayoutInflater layoutInflater , ArrayList<MenuDTO> contentArray) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.contentArray = contentArray;
    }

    @Override
    public int getCount() {
        return this.contentArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null ) {
            MenuDTO contentDTO = contentArray.get(position);
            convertView  = this.layoutInflater.inflate(R.layout.content_item,parent,false);
            ImageView content_item = convertView.findViewById(R.id.content_thumbnail);
            TextView tv = convertView.findViewById(R.id.content_title);
            tv.setText(contentDTO.getTitle() + contentDTO.getTitle());
        }

        return convertView;
    }



}
