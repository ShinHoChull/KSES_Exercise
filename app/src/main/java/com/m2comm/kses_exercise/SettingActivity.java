package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.init();

    }

    private void init() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"Setting");
        this.versionText = findViewById(R.id.versionText);
        this.versionText.setText("V "+ BuildConfig.VERSION_NAME);
    }
}