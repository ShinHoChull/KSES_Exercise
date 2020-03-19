package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.adapters.MenuLeftAdapter;
import com.m2comm.module.adapters.MenuRightAdapter;
import com.m2comm.module.models.MenuDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView leftList,rightList;
    private MenuLeftAdapter menuLeftAdapter;
    private MenuRightAdapter menuRightAdapter;

    private int groupDefaultNum = 0;
    private int leftClick = 0;
    private TextView closeBt;
    ArrayList<String> leftArray;
    ArrayList<MenuDTO> rightArray;
    JSONArray menuDepth2JsonArray;
    private Custom_SharedPreferences csp;


    int[] group_imgsId = {
            R.id.group1_img,
            R.id.group2_img,
            R.id.group3_img,
            R.id.group4_img
    };
    int[] groupImgResourceOn = {
            R.drawable.menu_icon01_on,
            R.drawable.menu_icon02_on,
            R.drawable.menu_icon03_on,
            R.drawable.menu_icon04_on,
    };

    int[] groupImgResourceOff = {
            R.drawable.menu_icon01_off,
            R.drawable.menu_icon02_off,
            R.drawable.menu_icon03_off,
            R.drawable.menu_icon04_off,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.idSetting();
        //Group Menu
        this.groupImageChange(this.groupDefaultNum);
        this.getMenuDataSetting(this.groupDefaultNum);

        this.leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuLeftAdapter.clickNum = position;
                rightArray = new ArrayList<>();
                leftClick = position;
                try {
                    JSONObject menuDepth3JObj = new JSONObject(menuDepth2JsonArray.get(position).toString());
                    JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));
                    for ( int i = 0 , j =  menuDepth3JsonArray.length(); i < j ; i++ ) {
                        JSONObject objTitle = new JSONObject(menuDepth3JsonArray.get(i).toString());
                        rightArray.add(new MenuDTO(objTitle.getString("TITLE") , objTitle.getString("VALUE"),objTitle.getInt("SID")));
                    }
                    menuChange();
                } catch (Exception e){
                    Log.d("leftClickError",e.toString());
                }
            }
        });

        this.rightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext() , ContentDetailActivity.class);
                intent.putExtra("groupNum",groupDefaultNum);
                intent.putExtra("depth2Num",leftClick);
                intent.putExtra("depth3Num",position);
                intent.putExtra("arr",rightArray);
                intent.putExtra("title",leftArray.get(leftClick));
                intent.putExtra("content_title",rightArray.get(position).getTitle());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        });
    }

    private void menuChange() {
        this.menuLeftAdapter = new MenuLeftAdapter(this , getLayoutInflater() , this.leftClick , this.leftArray);
        this.leftList.setAdapter(this.menuLeftAdapter);
        this.menuLeftAdapter.notifyDataSetChanged();

        this.menuRightAdapter = new MenuRightAdapter(this , getLayoutInflater() , this.rightArray);
        this.rightList.setAdapter(this.menuRightAdapter);
        this.menuRightAdapter.notifyDataSetChanged();
    }


    private void getMenuDataSetting ( int groupNum ) {

        this.leftArray = new ArrayList<>();
        this.rightArray = new ArrayList<>();
        this.leftClick = 0;
        this.groupDefaultNum = groupNum;
        try {
            JSONArray menuGroupJsonArray = new JSONArray(this.csp.getValue("menu",""));

            JSONObject menuDepth2JObj = new JSONObject(menuGroupJsonArray.get(groupNum).toString());
            this.menuDepth2JsonArray = new JSONArray(menuDepth2JObj.getString("VALUES"));
            for ( int i = 0 , j =  this.menuDepth2JsonArray.length(); i < j ; i++ ) {
                JSONObject objTitle = new JSONObject(this.menuDepth2JsonArray.get(i).toString());
                this.leftArray.add(objTitle.getString("TITLE"));
            }

            JSONObject menuDepth3JObj = new JSONObject(this.menuDepth2JsonArray.get(0).toString());
            JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));
            for ( int i = 0 , j =  menuDepth3JsonArray.length(); i < j ; i++ ) {
                JSONObject objTitle = new JSONObject(menuDepth3JsonArray.get(i).toString());
                this.rightArray.add(new MenuDTO(objTitle.getString("TITLE") , objTitle.getString("VALUE"),objTitle.getInt("SID")));
            }

            this.menuChange();

        } catch (Exception e) {
            Log.d("errror",e.toString());
            Toast.makeText(this , "Menu Paser Error1",Toast.LENGTH_SHORT).show();
        }
    }


    private void reSetGroupImage () {
        for ( int i = 0 , j = this.group_imgsId.length; i < j ; i++) {
            ImageView img = findViewById(this.group_imgsId[i]);
            img.setImageResource(this.groupImgResourceOff[i]);
        }
    }
    
    private void groupImageChange (int num) {
        this.reSetGroupImage();
        ImageView imageView = findViewById(this.group_imgsId[num]);
        imageView.setImageResource(this.groupImgResourceOn[num]);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_groupBt1:
                this.groupImageChange(0);
                getMenuDataSetting(0);
                break;
            case R.id.menu_groupBt2:
                this.groupImageChange(1);
                getMenuDataSetting(1);
                break;
            case R.id.menu_groupBt3:
                this.groupImageChange(2);
                getMenuDataSetting(2);
                break;
            case R.id.menu_groupBt4:
                this.groupImageChange(3);
                getMenuDataSetting(3);
                break;
            case R.id.menu_closeBt:
                finish();
                break;

            case R.id.menu_search_bt:
                intent = new Intent(this , SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_scheduleBt:
                intent = new Intent(this , CalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_favBt:
                intent = new Intent(this , MyListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.homeBt:
                intent = new Intent(this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.anim_slide_out_bottom_login);
    }

    private void idSetting () {
        this.leftList = findViewById(R.id.menu_left_list);
        this.rightList = findViewById(R.id.menu_right_list);
        findViewById(R.id.menu_groupBt1).setOnClickListener(this);
        findViewById(R.id.menu_groupBt2).setOnClickListener(this);
        findViewById(R.id.menu_groupBt3).setOnClickListener(this);
        findViewById(R.id.menu_groupBt4).setOnClickListener(this);
        findViewById(R.id.menu_closeBt).setOnClickListener(this);
        findViewById(R.id.menu_search_bt).setOnClickListener(this);
        findViewById(R.id.menu_favBt).setOnClickListener(this);
        findViewById(R.id.menu_scheduleBt).setOnClickListener(this);
        findViewById(R.id.homeBt).setOnClickListener(this);
        this.csp = new Custom_SharedPreferences(this);
    }

}
