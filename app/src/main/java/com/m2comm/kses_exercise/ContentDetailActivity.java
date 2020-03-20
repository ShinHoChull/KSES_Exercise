package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.dao.FavDAO;
import com.m2comm.module.models.FavDTO;
import com.m2comm.module.models.MenuDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    EditText detail_edittext;
    String title,content_title_txt , groupTitle;
    TextView content_title;
    private ArrayList<MenuDTO> arrayList;
    private int groupNum , depth2Num , depth3Num;
    private ImageView backBt , nextBt;
    private FavDTO favDTO;
    private FavDAO favDAO;

    Custom_SharedPreferences csp;

    private void idSetting() {
        Intent intent = getIntent();
        this.groupTitle = intent.getStringExtra("groupTitle");
        this.title = intent.getStringExtra("title");
        this.content_title_txt = intent.getStringExtra("content_title");
        this.arrayList = (ArrayList<MenuDTO>)intent.getSerializableExtra("arr");
        this.groupNum = intent.getIntExtra("groupNum",0);
        this.depth2Num = intent.getIntExtra("depth2Num",0);
        this.depth3Num = intent.getIntExtra("depth3Num",0);
        this.csp = new Custom_SharedPreferences(this);
        this.favDAO = new FavDAO(this);

        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,this.title);
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.detail_edittext = findViewById(R.id.detail_edittext);
        this.content_title = findViewById(R.id.title);
        findViewById(R.id.favBt).setOnClickListener(this);

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
            case R.id.favBt:
                this.getMenuDataSetting();
                this.favDAO.addFav(this.favDTO);
                Toast.makeText(this, "즐겨찾기에 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.nextBt:
//                this.chagenVideo(1);
//                break;
//            case R.id.backBt:
//                this.chagenVideo(-1);
//                break;
        }
    }

    private void getMenuDataSetting ( ) {
        try {
            JSONArray menuGroupJsonArray = new JSONArray(this.csp.getValue("menu",""));

            JSONObject menuDepth2JObj = new JSONObject(menuGroupJsonArray.get(groupNum).toString());

            final String depth1Title =  menuDepth2JObj.getString("TITLE");
            Log.d("title=",depth1Title);

            JSONArray menuDepth2JsonArray = new JSONArray(menuDepth2JObj.getString("VALUES"));
            JSONObject menuDepth3JObj = new JSONObject(menuDepth2JsonArray.get(depth2Num).toString());
            String depth2Title = menuDepth3JObj.getString("TITLE");
            Log.d("title2=",depth2Title);
            JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));
            JSONObject tt = new JSONObject(menuDepth3JsonArray.get(depth3Num).toString());

            String depth3Title = tt.getString("TITLE");
            Log.d("title3=",depth3Title);
            this.favDTO = new FavDTO(0,groupNum , depth2Num , depth3Num ,
                    tt.getString("URL") , depth1Title + " > "+depth2Title,depth3Title);



        } catch (Exception e) {
            Log.d("errror",e.toString());
            Toast.makeText(this , "Menu Paser Error1",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
