package com.m2comm.module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.kses_exercise.R;
import com.m2comm.module.models.ContentDTO;

import java.util.ArrayList;

public class SearchViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<ContentDTO> contentArray;


    public SearchViewAdapter(Context context, LayoutInflater layoutInflater , ArrayList<ContentDTO> contentArray) {
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
            ContentDTO contentDTO = contentArray.get(position);
            convertView  = this.layoutInflater.inflate(R.layout.content_item,parent,false);
            ImageView content_item = convertView.findViewById(R.id.content_thumbnail);
            TextView tv = convertView.findViewById(R.id.content_title);
            tv.setText(contentDTO.getTitle() + contentDTO.getGroupSid());
        }

        return convertView;
    }



}
