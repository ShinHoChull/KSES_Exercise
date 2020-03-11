package com.m2comm.module.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m2comm.kses_exercise.R;
import com.m2comm.module.models.MenuDTO;

import java.util.ArrayList;

public class MenuRightAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MenuDTO> arrayList;




    public MenuRightAdapter(Context context, LayoutInflater layoutInflater , ArrayList<MenuDTO> arrayList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
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
            convertView  = this.layoutInflater.inflate(R.layout.menu_right_group,parent,false);
            TextView title = convertView.findViewById(R.id.menu_right_text);
            title.setText(this.arrayList.get(position).getTitle());
        }

        return convertView;
    }
}
