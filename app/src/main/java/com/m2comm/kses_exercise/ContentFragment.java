package com.m2comm.kses_exercise;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.m2comm.module.adapters.ContentListViewAdapter;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.MenuDTO;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ContentFragment extends Fragment {

    private int page;
    private ArrayList<MenuDTO> arrayList;

    public static ContentFragment newInstance( int page , String title , ArrayList<MenuDTO> arrayList ) {

        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
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
        this.arrayList = (ArrayList<MenuDTO>) getArguments().getSerializable("item");
        Log.d("pageGETEGETGET","++"+this.page);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("zzzzz","enenenen_"+page);
        View view = inflater.inflate(R.layout.content_list_group, container, false);
        ListView listView = view.findViewById(R.id.content_listview);

        ContentListViewAdapter contentListViewAdapter = new ContentListViewAdapter(getContext() , getLayoutInflater() , this.arrayList);
        listView.setAdapter(contentListViewAdapter);

        return view;
    }


}
