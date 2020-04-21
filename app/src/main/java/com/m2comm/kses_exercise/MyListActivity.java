package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.m2comm.module.Common;
import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.adapters.FavViewAdapter;
import com.m2comm.module.dao.FavDAO;
import com.m2comm.module.models.FavDTO;
import com.m2comm.module.models.MenuDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MyListActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    PopTopActivity popTopActivity;
    ListView listView;
    FavViewAdapter favViewAdapter;
    ArrayList<FavDTO> arrayList;
    FavDAO favDAO;
    LinearLayout deleteBt1 , deleteBt2;
    boolean isDel = false ;
    ArrayList<MenuDTO> rightArray;
    String depth2Title;
    private Custom_SharedPreferences csp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        Common.common_menuDTO_ArrayList = new ArrayList<>();
        this.idSetting();
        Intent intent = getIntent();
        if (intent.getBooleanExtra("isFav", false)) {
            this.delList();
            this.popTopActivity = new PopTopActivity(this, this, getLayoutInflater(), R.id.content_top, "");
            findViewById(R.id.bottom).setVisibility(View.INVISIBLE);
        } else {
            this.contentTopActivity = new ContentTopActivity(this, this, getLayoutInflater(), R.id.content_top, "즐겨찾기");
            this.bottomActivity = new BottomActivity(getLayoutInflater(), R.id.bottom, this, this);
        }

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavDTO favDTO = arrayList.get(position);
                getMenuDataSetting(favDTO.getGroupNum(),favDTO.getDepth2Num());
                Intent intent = new Intent(getApplicationContext() , ContentDetailActivity.class);
                intent.putExtra("groupNum",favDTO.getGroupNum());
                intent.putExtra("depth2Num",favDTO.getDepth2Num());
                intent.putExtra("depth3Num",favDTO.getDepth3Num());
                intent.putExtra("arr",rightArray);
                intent.putExtra("title",depth2Title);
                intent.putExtra("content_title",favDTO.getContent_title());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }

    private void getMenuDataSetting ( int groupNum , int depth2Num  ) {
        this.rightArray = new ArrayList<>();
        try {
            JSONArray menuGroupJsonArray = new JSONArray(this.csp.getValue("menu",""));
            JSONObject menuDepth2JObj = new JSONObject(menuGroupJsonArray.get(groupNum).toString());
            JSONArray menuDepth2JsonArray = new JSONArray(menuDepth2JObj.getString("VALUES"));

            JSONObject menuDepth3JObj = new JSONObject(menuDepth2JsonArray.get(depth2Num).toString());
            this.depth2Title = menuDepth3JObj.getString("TITLE");
            JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));

            for ( int k = 0 , l =  menuDepth3JsonArray.length(); k < l ; k++ ) {
                JSONObject tt = new JSONObject(menuDepth3JsonArray.get(k).toString());
                rightArray.add(new MenuDTO(tt.getString("TITLE") , tt.getString("VALUE"), tt.getInt("SID")));
            }

        } catch (Exception e) {
            Log.d("errror",e.toString());
            Toast.makeText(this , "Menu Paser Error1",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.adapterChange();
    }

    public void changeCount(int count) {
        this.popTopActivity.changeCount(count);
    }

    public void adapterChange(){
        this.arrayList = new ArrayList<>();
        this.arrayList = this.favDAO.getAllList();
        if ( this.arrayList != null ) {
            this.favViewAdapter = new FavViewAdapter(this , getLayoutInflater() , this.arrayList , isDel);
            this.listView.setAdapter(this.favViewAdapter);
            this.favViewAdapter.notifyDataSetChanged();
        }
    }

    public void delList() {
        this.deleteBt1.setVisibility(View.GONE);
        this.deleteBt2.setVisibility(View.VISIBLE);
        this.isDel = true;
        this.adapterChange();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.step_1_delete:
                Common.common_menuDTO_ArrayList = new ArrayList<>();
                Intent intent = new Intent(this, MyListActivity.class);
                intent.putExtra("isFav",true);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                break;

            case R.id.step_2_delete:
                if (Common.common_menuDTO_ArrayList != null && Common.common_menuDTO_ArrayList.size() > 0) {
                    for(Iterator<FavDTO> it = Common.common_menuDTO_ArrayList.iterator(); it.hasNext() ; ) {
                        FavDTO row = it.next();
                        it.remove();
                        this.favDAO.delete(row.getNum());
                    }
                    Toast.makeText(this , "삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
        }
    }
    @Override
    public void finish() {
        Common.common_menuDTO_ArrayList = new ArrayList<>();
        super.finish();
        if ( this.isDel )overridePendingTransition(0, R.anim.anim_slide_out_bottom_login);
        else overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

    }

    private void idSetting() {

        this.listView = findViewById(R.id.listView);
        this.deleteBt1 = findViewById(R.id.step_1_delete);
        this.deleteBt1.setOnClickListener(this);
        this.deleteBt2 = findViewById(R.id.step_2_delete);
        this.deleteBt2.setOnClickListener(this);

        this.arrayList = new ArrayList<>();
        this.favDAO = new FavDAO(this);
        this.csp = new Custom_SharedPreferences(this);


    }

}
