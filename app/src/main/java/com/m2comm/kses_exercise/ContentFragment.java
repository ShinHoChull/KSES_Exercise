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

    private int page;
    private String top_title;
    private ArrayList<MenuDTO> arrayList;

    public static ContentFragment newInstance( int page , String title , ArrayList<MenuDTO> arrayList ) {

        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("title", title);
        args.putSerializable("item",arrayList);
        fragment.setArguments(args);
        Log.d("fragment","fragment="+page);

        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.page = getArguments().getInt("someInt", 0);
        this.top_title = getArguments().getString("title","");
        this.arrayList = (ArrayList<MenuDTO>) getArguments().getSerializable("item");
        Log.d("pageGETEGETGET","++"+this.page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_list_group, container, false);
        ListView listView = view.findViewById(R.id.content_listview);
        ContentListViewAdapter contentListViewAdapter = new ContentListViewAdapter(getContext() , getLayoutInflater() , this.arrayList);
        listView.setAdapter(contentListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity activity = getActivity();
                Intent intent = new Intent(getContext() , ContentDetailActivity.class);
                intent.putExtra("groupId",page);
                intent.putExtra("title",top_title);
                intent.putExtra("arr",arrayList);
                intent.putExtra("position",position);
                intent.putExtra("content_title",arrayList.get(position).getTitle());
                startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        return view;
    }


}
