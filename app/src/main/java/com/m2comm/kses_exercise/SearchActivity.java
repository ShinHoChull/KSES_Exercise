package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.adapters.ContentListViewAdapter;
import com.m2comm.module.adapters.FavViewAdapter;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.FavDTO;
import com.m2comm.module.models.MenuDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    private ImageView search_img;
    private ListView listView;
    private ArrayList<FavDTO> copyList;
    private ArrayList<FavDTO> realList;
    private Custom_SharedPreferences csp;
    private FavViewAdapter favViewAdapter;
    private EditText searchText;
    private TextView searchCount;
    ArrayList<MenuDTO> rightArray;
    private String depth2Title , groupTitle;


    private void idSetting() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"검색");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.search_img = findViewById(R.id.search_img);
        this.listView = findViewById(R.id.search_list);
        this.searchText = findViewById(R.id.searchText);
        this.searchCount = findViewById(R.id.searchCount);
        this.csp = new Custom_SharedPreferences(this);
        this.realList = new ArrayList<>();
        this.copyList = new ArrayList<>();
        this.rightArray = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.idSetting();
        this.search_img.setColorFilter(Color.WHITE);

        for ( int i = 0 , j = 4 ; i < j ; i ++ ) {
            this.getMenuDataSetting( i );
        }

        this.search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyList = new ArrayList<>();
                if ( searchText.getText().toString().equals("") ) {
                    copyList.addAll(realList);
                } else {
                    for ( FavDTO row : realList ) {
                        if( row.getGroupTitle().toLowerCase().contains(searchText.getText().toString()) || row.getContent_title().toLowerCase().contains(searchText.getText().toString()) ) {
                            copyList.add(row);
                        }
                    }
                }
                changeAdapter();
            }
        });
        this.changeAdapter();

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavDTO favDTO = copyList.get(position);
                getMenuDataSetting(favDTO.getGroupNum(),favDTO.getDepth2Num());
                Log.d("favContent",favDTO.getContent_title());
                Log.d("favNum",favDTO.getGroupNum()+"/"+favDTO.getDepth2Num()+"/"+favDTO.getDepth3Num());

                for ( MenuDTO row : rightArray) {
                    Log.d("rowrow",row.getTitle());
                }
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

    private void changeAdapter() {
        searchCount.setText(copyList.size()+"");
        this.favViewAdapter = new FavViewAdapter(this , getLayoutInflater() , this.copyList , false);
        this.listView.setAdapter(this.favViewAdapter);
    }

    private void getMenuDataSetting ( int groupNum ) {
        try {
            JSONArray menuGroupJsonArray = new JSONArray(this.csp.getValue("menu",""));

            JSONObject menuDepth2JObj = new JSONObject(menuGroupJsonArray.get(groupNum).toString());

            final String depth1Title =  menuDepth2JObj.getString("TITLE");
            Log.d("title=",depth1Title);

            JSONArray menuDepth2JsonArray = new JSONArray(menuDepth2JObj.getString("VALUES"));

            for ( int i = 0 , j =  menuDepth2JsonArray.length(); i < j ; i++ ) {
                JSONObject menuDepth3JObj = new JSONObject(menuDepth2JsonArray.get(i).toString());
                String depth2Title = menuDepth3JObj.getString("TITLE");
                int depth2Num = menuDepth3JObj.getInt("SID");
                Log.d("title2=",depth2Title);
                JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));

                for ( int k = 0 , l =  menuDepth3JsonArray.length(); k < l ; k++ ) {
                    JSONObject tt = new JSONObject(menuDepth3JsonArray.get(k).toString());
                    String depth3Title = tt.getString("TITLE");
                    int depth3Num = tt.getInt("SID");
                    Log.d("title3=",depth3Title);
                    realList.add(new FavDTO(0,groupNum , depth2Num , depth3Num ,
                            tt.getString("URL") , depth1Title + " > "+depth2Title,depth3Title));
                }
            }

        } catch (Exception e) {
            Log.d("errror",e.toString());
            Toast.makeText(this , "Menu Paser Error1",Toast.LENGTH_SHORT).show();
        }
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
