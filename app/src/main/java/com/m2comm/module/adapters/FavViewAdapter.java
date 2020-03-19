package com.m2comm.module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.kses_exercise.ContentListActivity;
import com.m2comm.kses_exercise.MyListActivity;
import com.m2comm.kses_exercise.R;
import com.m2comm.module.Common;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.FavDTO;

import java.util.ArrayList;
import java.util.Iterator;

public class FavViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<FavDTO> contentArray;
    boolean isDel = false;

    public FavViewAdapter(Context context, LayoutInflater layoutInflater , ArrayList<FavDTO> contentArray , boolean isDel) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.contentArray = contentArray;
        this.isDel = isDel;
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

            final FavDTO favDTO = contentArray.get(position);
            convertView  = this.layoutInflater.inflate(R.layout.fav_group_item,parent,false);
            TextView groupTitle = convertView.findViewById(R.id.groupTitle);
            TextView contentTitle = convertView.findViewById(R.id.content_title);
            final ImageView delBt = convertView.findViewById(R.id.delBt);

            if ( isDel ) {
                delBt.setVisibility(View.VISIBLE);
            } else {
                delBt.setVisibility(View.GONE);
            }

            groupTitle.setText(favDTO.getGroupTitle());
            contentTitle.setText(favDTO.getContent_title());

            if (Common.common_menuDTO_ArrayList != null && Common.common_menuDTO_ArrayList.size() > 0) {
                for(Iterator<FavDTO> it = Common.common_menuDTO_ArrayList.iterator(); it.hasNext() ; ) {
                    FavDTO row = it.next();
                    if ( row.getDepth2Num() == favDTO.getDepth2Num() && row.getNum() == favDTO.getNum() ) {
                        delBt.setImageResource(R.drawable.content_on);
                    }
                }
            }

            delBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Common.common_menuDTO_ArrayList.size() > 0) {
                        boolean isAdd = true;
                        for(Iterator<FavDTO> it = Common.common_menuDTO_ArrayList.iterator(); it.hasNext() ; ) {
                            FavDTO row = it.next();
                            if ( row.equals(favDTO) ) {
                                it.remove();
                                delBt.setImageResource(R.drawable.content_off);
                                isAdd = false;
                            }
                        }

                        if (isAdd) {
                            Common.common_menuDTO_ArrayList.add(favDTO);
                            delBt.setImageResource(R.drawable.content_on);
                        }
                    } else {
                        Common.common_menuDTO_ArrayList.add(favDTO);
                        delBt.setImageResource(R.drawable.content_on);
                    }
                    ((MyListActivity)context).changeCount(Common.common_menuDTO_ArrayList.size());
                }
            });



        return convertView;
    }



}
