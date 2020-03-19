package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.module.models.MenuDTO;

import java.util.ArrayList;

public class ContentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    EditText detail_edittext;
    String title,content_title_txt;
    TextView content_title;
    private ArrayList<MenuDTO> arrayList;
    private int groupNum , depth2Num , depth3Num;
    private ImageView backBt , nextBt;

    private void idSetting() {
        Intent intent = getIntent();
        this.title = intent.getStringExtra("title");
        this.content_title_txt = intent.getStringExtra("content_title");
        this.arrayList = (ArrayList<MenuDTO>)intent.getSerializableExtra("arr");
        this.groupNum = intent.getIntExtra("groupNum",0);
        this.depth2Num = intent.getIntExtra("depth2Num",0);
        this.depth3Num = intent.getIntExtra("depth3Num",0);

        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,this.title);
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.detail_edittext = findViewById(R.id.detail_edittext);
        this.content_title = findViewById(R.id.title);
        //this.nextBt = findViewById(R.id.nextBt);
        //this.nextBt.setOnClickListener(this);
        //this.backBt = findViewById(R.id.backBt);
        //this.backBt.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        this.idSetting();

        this.detail_edittext.setFocusableInTouchMode(false);
        this.detail_edittext.clearFocus();

        this.content_title.setText(this.content_title_txt);
    }

    private void chagenVideo( int num ) {
        this.depth3Num = this.depth3Num + num;
        if (this.depth3Num < 0) {
            this.depth3Num = 0;
        } else if ( this.depth3Num >= this.arrayList.size() ) {
            this.depth3Num = this.arrayList.size() - 1;
        }
        this.content_title.setText(this.arrayList.get(this.depth3Num).getTitle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.nextBt:
//                this.chagenVideo(1);
//                break;
//            case R.id.backBt:
//                this.chagenVideo(-1);
//                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
