package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class ContentDetailActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    EditText detail_edittext;
    String title;

    private void idSetting() {
        Intent intent = getIntent();
        this.title = intent.getStringExtra("title");
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,this.title);
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.detail_edittext = findViewById(R.id.detail_edittext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        this.idSetting();

        this.detail_edittext.setFocusableInTouchMode(false);
        this.detail_edittext.clearFocus();

    }
}
