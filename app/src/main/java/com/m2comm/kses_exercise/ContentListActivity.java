package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.models.MenuDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentListActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    TextView title_tv;
    TabLayout tabLayout;
    ViewPager viewPager;
    ContentViewPagerAdapter contentViewPagerAdapter;
    Custom_SharedPreferences csp;
    JSONArray menuDepth2JsonArray;
    ArrayList<String> leftArray;
    ArrayList<MenuDTO> rightArray;
    private String title;
    private int groupDefaultNum = 0;
    private int leftClick = 0;

    private void idSetting() {
        this.viewPager = findViewById(R.id.content_pager);
        this.tabLayout = findViewById(R.id.content_tab);
        this.title_tv = findViewById(R.id.content_title);
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.csp = new Custom_SharedPreferences(this);
        Intent intent = getIntent();
        this.groupDefaultNum = intent.getIntExtra("groupId",0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        this.idSetting();
        this.getMenuDataSetting(this.groupDefaultNum);
        this.title_tv.setText(this.title);
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,this.title);

        for ( int i = 0 , j = this.leftArray.size(); i < j ; i ++ ) {
            this.tabLayout.addTab(this.tabLayout.newTab().setText(this.leftArray.get(i)));
        }

        this.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#2483EA"));
        this.tabLayout.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#2483EA"));

        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        this.contentViewPagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager() ,this, this.leftArray.size());
        this.viewPager.setAdapter(contentViewPagerAdapter);
        this.viewPager.setClipToPadding(false);
        this.viewPager.setPageMargin(132);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position,0,true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void getMenuDataSetting ( int groupNum ) {
        this.leftArray = new ArrayList<>();
        this.rightArray = new ArrayList<>();
        this.leftClick = 0;
        this.groupDefaultNum = groupNum;
        try {
            JSONArray menuGroupJsonArray = new JSONArray(this.csp.getValue("menu",""));

            JSONObject menuDepth2JObj = new JSONObject(menuGroupJsonArray.get(groupNum).toString());
            //MainTitle 가져오기.
            this.title = menuDepth2JObj.getString("TITLE");
            this.menuDepth2JsonArray = new JSONArray(menuDepth2JObj.getString("VALUES"));
            for ( int i = 0 , j =  this.menuDepth2JsonArray.length(); i < j ; i++ ) {
                JSONObject objTitle = new JSONObject(this.menuDepth2JsonArray.get(i).toString());
                this.leftArray.add(objTitle.getString("TITLE"));
            }

            JSONObject menuDepth3JObj = new JSONObject(this.menuDepth2JsonArray.get(0).toString());
            JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));
            for ( int i = 0 , j =  menuDepth3JsonArray.length(); i < j ; i++ ) {
                JSONObject objTitle = new JSONObject(menuDepth3JsonArray.get(i).toString());
                this.rightArray.add(new MenuDTO(objTitle.getString("TITLE") , objTitle.getString("VALUE"),0));
            }

        } catch (Exception e) {
            Log.d("errror",e.toString());
            Toast.makeText(this , "Menu Paser Error1",Toast.LENGTH_SHORT).show();
        }
    }

    public class ContentViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;
        private int pagerCount;

        public ContentViewPagerAdapter(FragmentManager fragmentManager , Context context, int pagerCount) {
            super(fragmentManager);
            this.context = context;
            this.pagerCount = pagerCount;
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<MenuDTO> arrs = new ArrayList<>();
            try {
                JSONObject menuDepth3JObj = new JSONObject(menuDepth2JsonArray.get(position).toString());
                JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));
                for ( int i = 0 , j =  menuDepth3JsonArray.length(); i < j ; i++ ) {
                    JSONObject objTitle = new JSONObject(menuDepth3JsonArray.get(i).toString());
                    arrs.add(new MenuDTO(objTitle.getString("TITLE") , objTitle.getString("VALUE"),0));
                }
            } catch (Exception e){
                Log.d("leftClickError",e.toString());
            }
            return ContentFragment.newInstance(position,"HEOO",arrs);
        }

        @Override
        public int getCount() {
            return this.pagerCount;
        }


    }
}
