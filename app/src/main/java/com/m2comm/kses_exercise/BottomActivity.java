package com.m2comm.kses_exercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

public class BottomActivity implements View.OnClickListener {

    private LayoutInflater inflater;
    private int ParentID;
    private FrameLayout parent;
    private Context context;
    private Activity activity;

    public BottomActivity(LayoutInflater inflater, int parentID, Context context, Activity activity) {
        this.inflater = inflater;
        ParentID = parentID;
        this.context = context;
        this.activity = activity;
        this.init();
    }

    private void init () {
        this.parent = this.activity.findViewById(this.ParentID);
        View view = inflater.inflate(R.layout.activity_bottom,this.parent,true);
        view.findViewById(R.id.bottomBt1).setOnClickListener(this);
        view.findViewById(R.id.bottomBt2).setOnClickListener(this);
        view.findViewById(R.id.bottomBt3).setOnClickListener(this);
        view.findViewById(R.id.bottomBt4).setOnClickListener(this);
        view.findViewById(R.id.bottomBt5).setOnClickListener(this);
        this.parent.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch ( v.getId() ) {

            case R.id.bottomBt1:
                intent = new Intent(this.activity , CalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                this.activity.startActivity(intent);
                this.activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.bottomBt2:
                intent = new Intent(this.activity , AlarmListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                this.activity.startActivity(intent);
                this.activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.bottomBt3:
                intent = new Intent(this.activity , MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                this.activity.startActivity(intent);
                this.activity.overridePendingTransition(R.anim.anim_slide_in_left,0);
                break;

            case R.id.bottomBt4:
                intent = new Intent(this.activity , SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                this.activity.startActivity(intent);
                this.activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.bottomBt5:
                intent = new Intent(this.activity , MyListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                this.activity.startActivity(intent);
                this.activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

        }

    }
}
