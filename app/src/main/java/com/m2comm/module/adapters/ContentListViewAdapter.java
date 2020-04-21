package com.m2comm.module.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.kses_exercise.ContentListActivity;
import com.m2comm.kses_exercise.R;
import com.m2comm.module.Common;
import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.FavDTO;
import com.m2comm.module.models.MenuDTO;

import java.util.ArrayList;
import java.util.Iterator;

public class ContentListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MenuDTO> contentArray;
    boolean isFav = false;
    private Custom_SharedPreferences csp;
    int groupNum , depth2Num;
    String group_title , depth2Title;

    public ContentListViewAdapter(Context context, LayoutInflater layoutInflater, ArrayList<MenuDTO> contentArray, boolean isFav , int groupNum , int depth2Num , String group_title, String depth2Title) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.contentArray = contentArray;
        this.isFav = isFav;
        this.groupNum = groupNum;
        this.depth2Num = depth2Num;
        this.group_title = group_title;
        this.depth2Title = depth2Title;
        this.csp = new Custom_SharedPreferences(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final MenuDTO contentDTO = contentArray.get(position);
            convertView = this.layoutInflater.inflate(R.layout.content_item, parent, false);
            ImageView content_item = convertView.findViewById(R.id.content_thumbnail);
            final ImageView favBt = convertView.findViewById(R.id.favBt);
            TextView tv = convertView.findViewById(R.id.content_title);

            tv.setText(contentDTO.getTitle());
            if (this.isFav) {
                favBt.setVisibility(View.VISIBLE);
            } else {
                favBt.setVisibility(View.GONE);
            }
            final FavDTO favDTO = new FavDTO(0,this.groupNum , this.depth2Num , position , contentDTO.getUrl(),this.group_title+">"+this.depth2Title, contentDTO.getTitle());

            if (Common.common_menuDTO_ArrayList != null && Common.common_menuDTO_ArrayList.size() > 0) {
                for(Iterator<FavDTO> it = Common.common_menuDTO_ArrayList.iterator(); it.hasNext() ; ) {
                    FavDTO row = it.next();
                    if ( row.getGroupNum() == this.groupNum && row.getDepth2Num() == this.depth2Num && row.getDepth3Num() == position ) {
                        favBt.setImageResource(R.drawable.content_on);
                    }
                }
            }

            favBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Common.common_menuDTO_ArrayList.size() > 0) {
                        boolean isAdd = true;
                        for(Iterator<FavDTO> it = Common.common_menuDTO_ArrayList.iterator(); it.hasNext() ; ) {
                            FavDTO row = it.next();
                            if ( row.getGroupNum() == favDTO.getGroupNum() && row.getDepth2Num() == favDTO.getDepth2Num() && row.getDepth3Num() == favDTO.getDepth3Num() ) {
                                it.remove();
                                favBt.setImageResource(R.drawable.content_off);
                                isAdd = false;
                            }
                        }
                        if (isAdd) {
                            Common.common_menuDTO_ArrayList.add(favDTO);
                            favBt.setImageResource(R.drawable.content_on);
                        }

                    } else {
                        Common.common_menuDTO_ArrayList.add(favDTO);
                        favBt.setImageResource(R.drawable.content_on);
                    }
                    ((ContentListActivity)context).changeCount(Common.common_menuDTO_ArrayList.size());
                }
            });

        }

        return convertView;
    }


}
