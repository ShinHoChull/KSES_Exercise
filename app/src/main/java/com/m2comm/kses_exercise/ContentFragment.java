package com.m2comm.kses_exercise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.m2comm.module.adapters.ContentListViewAdapter;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.MenuDTO;

import org.w3c.dom.Text;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ContentFragment extends Fragment {

    private int depth2Num,groupNum;
    private String top_title , depth2Title;
    private ArrayList<MenuDTO> arrayList;
    private boolean isFav;



    public static ContentFragment newInstance( int page , String title , ArrayList<MenuDTO> arrayList ,boolean isFav , int groupNum ,String depth2Title) {

        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt("groupNum",groupNum);
        args.putInt("depth2Num", page);
        args.putString("depth2Title",depth2Title);
        args.putString("title", title);
        args.putBoolean("isFav",isFav);
        args.putSerializable("item",arrayList);
        fragment.setArguments(args);
        Log.d("fragment","fragment="+page);

        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.groupNum = getArguments().getInt("groupNum", 0);
        this.depth2Num = getArguments().getInt("depth2Num", 0);
        this.top_title = getArguments().getString("title","");
        this.depth2Title = getArguments().getString("depth2Title", "");
        this.isFav = getArguments().getBoolean("isFav",false);
        this.arrayList = (ArrayList<MenuDTO>) getArguments().getSerializable("item");
        Log.d("pageGETEGETGET","++"+this.depth2Num);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_list_group, container, false);
        ListView listView = view.findViewById(R.id.content_listview);
        ContentListViewAdapter contentListViewAdapter = new ContentListViewAdapter(getContext() , getLayoutInflater() , this.arrayList ,
                this.isFav , this.groupNum , this.depth2Num,this.top_title , this.depth2Title);
        listView.setAdapter(contentListViewAdapter);
        contentListViewAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = getActivity();
                Intent intent = new Intent(getContext() , ContentDetailActivity.class);
                intent.putExtra("groupNum",groupNum);
                intent.putExtra("depth2Num",depth2Num);
                intent.putExtra("depth3Num",position);
                intent.putExtra("title",top_title);
                intent.putExtra("arr",arrayList);
                intent.putExtra("content_title",arrayList.get(position).getTitle());
                startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        return view;
    }


}
