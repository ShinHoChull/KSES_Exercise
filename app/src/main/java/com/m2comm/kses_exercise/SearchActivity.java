package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.m2comm.module.adapters.ContentListViewAdapter;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.MenuDTO;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    private ImageView search_img;
    private ListView listView;
    private ArrayList<MenuDTO> arrayList;

    private void idSetting() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"검색");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.search_img = findViewById(R.id.search_img);
        this.listView = findViewById(R.id.search_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.idSetting();
        this.search_img.setColorFilter(Color.WHITE);
        this.arrayList = new ArrayList<>();

        ContentListViewAdapter contentListViewAdapter = new ContentListViewAdapter(this , getLayoutInflater() , this.arrayList);
        listView.setAdapter(contentListViewAdapter);

    }
}
