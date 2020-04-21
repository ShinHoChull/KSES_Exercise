package com.m2comm.kses_exercise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopTopActivity implements View.OnClickListener {

    private Context context;
    private Activity activity;
    private FrameLayout parent;
    private LayoutInflater inflater;
    private int ParentID;
    private String title;
    private TextView titleView , mainTitle;
    private ImageView back;

    public PopTopActivity(Context context, Activity activity, LayoutInflater inflater, int parentID, String title ) {
        this.context = context;
        this.activity = activity;
        this.inflater = inflater;
        ParentID = parentID;
        this.title = title;
        this.init();
    }

    private void idSetting () {
        this.parent = this.activity.findViewById(this.ParentID);
        View view = inflater.inflate(R.layout.activity_pop_top,this.parent,true);
        this.titleView = view.findViewById(R.id.pop_count);
        this.back = view.findViewById(R.id.back);
        this.mainTitle = view.findViewById(R.id.top_title);
        if ( !this.title.equals("") ) {
            this.mainTitle.setText(this.title);
            this.mainTitle.setVisibility(View.VISIBLE);
            this.titleView.setVisibility(View.GONE);
        }
    }

    private void init () {
        this.idSetting();
        this.parent.setBackgroundColor(this.context.getResources().getColor(R.color.content_top_color));
        this.back.setOnClickListener(this);
    }

    public void changeCount (int count) {
        this.titleView.setVisibility(View.VISIBLE);
        this.titleView.setText(count + "개 선택");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.activity.finish();
                this.activity.overridePendingTransition(0, R.anim.anim_slide_out_bottom_login);
                break;

        }

    }
}
