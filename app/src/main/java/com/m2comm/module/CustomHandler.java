package com.m2comm.module;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.m2comm.kses_exercise.ContentListActivity;
import com.m2comm.kses_exercise.IntroActivity;
import com.m2comm.kses_exercise.MainActivity;


public class CustomHandler extends Handler {

    public static final int ALERT_WINDOW_CODE = 1;
    public static final int PROGRESS_UPDATE1 = 2;
    public static final int PROGRESS_UPDATE2 = 3;
    public static final int CONTENTLIST_FAV = 4;



    private Context c;

    public CustomHandler(Context c) {
        this.c = c;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {

            case CONTENTLIST_FAV :
                ((ContentListActivity) this.c).contentAdd();
                break;

            case PROGRESS_UPDATE1 :
                ((com.m2comm.kses_exercise.CalendarActivity)this.c).updateProgress();
                break;

            case PROGRESS_UPDATE2 :
                ((com.m2comm.kses_exercise.MainActivity)this.c).updateProgress();
                break;


        }
    }

    //기본알럿창
    private void baseAlertMessage (String subject , String content) {
        new MaterialDialog.Builder(this.c).title(subject)
                .content(content)
                .positiveText("OK").negativeText("cancel").theme(Theme.LIGHT).show();

    }

}
