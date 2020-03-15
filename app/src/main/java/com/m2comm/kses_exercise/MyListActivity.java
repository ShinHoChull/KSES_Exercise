package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.m2comm.module.adapters.FavViewAdapter;
import com.m2comm.module.models.FavDTO;

import java.util.ArrayList;

public class MyListActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    ListView listView;
    FavViewAdapter favViewAdapter;
    ArrayList<FavDTO> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        this.idSetting();

        this.arrayList.add(new FavDTO(0,0,0,0,"","",""));
        this.arrayList.add(new FavDTO(0,0,0,0,"","",""));
        this.arrayList.add(new FavDTO(0,0,0,0,"","",""));

        this.favViewAdapter = new FavViewAdapter(this , getLayoutInflater() , this.arrayList);
        this.listView.setAdapter(this.favViewAdapter);
    }

    private void idSetting() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"즐겨찾기");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.listView = findViewById(R.id.listView);
        this.arrayList = new ArrayList<>();


    }
}
