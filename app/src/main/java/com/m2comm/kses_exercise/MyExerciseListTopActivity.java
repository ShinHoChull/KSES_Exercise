package com.m2comm.kses_exercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyExerciseListTopActivity implements View.OnClickListener {

    private Context context;
    private Activity activity;
    private FrameLayout parent;
    private LayoutInflater inflater;
    private int ParentID;
    private String title;
    private TextView titleView;
    private LinearLayout back;

    public MyExerciseListTopActivity(Context context, Activity activity, LayoutInflater inflater, int parentID, String title ) {
        this.context = context;
        this.activity = activity;
        this.inflater = inflater;
        ParentID = parentID;
        this.title = title;
        this.init();
    }

    private void idSetting () {
        this.parent = this.activity.findViewById(this.ParentID);
        View view = inflater.inflate(R.layout.activity_content_top,this.parent,true);
        this.titleView = view.findViewById(R.id.top_title);
        this.back = view.findViewById(R.id.top_back);
    }

    private void init () {
        this.idSetting();
        this.parent.setBackgroundColor(this.context.getResources().getColor(R.color.content_top_color));
        this.titleView.setText(this.title);
        this.titleView.setOnClickListener(this);
        this.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.top_back:
                intent = new Intent(this.activity , CalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.activity.startActivity(intent);
                this.activity.finish();
                this.activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

                break;
            case R.id.top_title:
                intent = new Intent(this.activity , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                this.activity.startActivity(intent);

                break;
        }

    }
}
