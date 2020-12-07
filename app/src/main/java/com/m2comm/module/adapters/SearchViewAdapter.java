package com.m2comm.module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.m2comm.kses_exercise.R;
import com.m2comm.module.models.ContentDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<ContentDTO> contentArray;
    ViewHodel viewHolder;


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
            this.viewHolder = new ViewHodel();
            convertView  = this.layoutInflater.inflate(R.layout.content_item,parent,false);

            this.viewHolder.img = convertView.findViewById(R.id.content_thumbnail);
            this.viewHolder.tv = convertView.findViewById(R.id.content_title);
            convertView.setTag(this.viewHolder);
        } else {
            this.viewHolder = (ViewHodel) convertView.getTag();
        }
        ContentDTO row = contentArray.get(position);
        Picasso.get().load(row.getImgUrl()).into(this.viewHolder.img);
        this.viewHolder.tv.setText(row.getTitle() + row.getGroupSid());

        return convertView;
    }

    class ViewHodel {
        public ImageView img;
        public TextView tv;
    }



}
