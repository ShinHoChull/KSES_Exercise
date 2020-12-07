package com.m2comm.kses_exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.m2comm.module.Common;
import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.dao.FavDAO;
import com.m2comm.module.models.FavDTO;
import com.m2comm.module.models.MenuDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmList;

public class ContentListActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    PopTopActivity popTopActivity;
    BottomActivity bottomActivity;
    TextView title_tv, favText;
    TabLayout tabLayout;
    LinearLayout favBt;
    ViewPager viewPager;
    ContentViewPagerAdapter contentViewPagerAdapter;
    Custom_SharedPreferences csp;
    JSONArray menuDepth2JsonArray;
    ArrayList<String> leftArray;
    ArrayList<MenuDTO> rightArray;
    private boolean isFav = false;
    private String title;
    private int groupDefaultNum = 0;
    private int leftClick = 0;
    private FavDAO favDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        this.idSetting();
        this.getMenuDataSetting(this.groupDefaultNum);
        this.title_tv.setText(this.title);

        for (int i = 0, j = this.leftArray.size(); i < j; i++) {
            this.tabLayout.addTab(this.tabLayout.newTab().setText(this.leftArray.get(i)));
        }

        this.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#2483EA"));
        this.tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#2483EA"));

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

        this.contentViewPagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager(), this, this.leftArray.size(), isFav);
        this.viewPager.setAdapter(contentViewPagerAdapter);
        this.viewPager.setClipToPadding(false);
        this.viewPager.setPageMargin(132);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        Intent intent = getIntent();
        if (intent.getBooleanExtra("isFav", false)) {
            this.popTopActivity = new PopTopActivity(this, this, getLayoutInflater(), R.id.content_top, this.title);
            findViewById(R.id.bottom).setVisibility(View.INVISIBLE);
            this.favList();
            this.isFav = true;
        } else {
            this.contentTopActivity = new ContentTopActivity(this, this, getLayoutInflater(), R.id.content_top, this.title);
            this.bottomActivity = new BottomActivity(getLayoutInflater(), R.id.bottom, this, this);
        }
    }

    private void getMenuDataSetting(int groupNum) {
        this.leftArray = new ArrayList<>();
        this.rightArray = new ArrayList<>();
        this.leftClick = 0;
        this.groupDefaultNum = groupNum;
        try {
            JSONArray menuGroupJsonArray = new JSONArray(this.csp.getValue("menu", ""));

            JSONObject menuDepth2JObj = new JSONObject(menuGroupJsonArray.get(groupNum).toString());
            //MainTitle 가져오기.
            this.title = menuDepth2JObj.getString("TITLE");
            this.menuDepth2JsonArray = new JSONArray(menuDepth2JObj.getString("VALUES"));
            for (int i = 0, j = this.menuDepth2JsonArray.length(); i < j; i++) {
                JSONObject objTitle = new JSONObject(this.menuDepth2JsonArray.get(i).toString());
                this.leftArray.add(objTitle.getString("TITLE"));
            }

            JSONObject menuDepth3JObj = new JSONObject(this.menuDepth2JsonArray.get(0).toString());
            JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));
            for (int i = 0, j = menuDepth3JsonArray.length(); i < j; i++) {
                JSONObject objTitle = new JSONObject(menuDepth3JsonArray.get(i).toString());
                this.rightArray.add(new MenuDTO(objTitle.getString("TITLE"), objTitle.getString("URL"), objTitle.getInt("SID"), objTitle.getString("VALUE"),objTitle.getString("THUMBNAIL")));
            }

        } catch (Exception e) {
            Log.d("errror", e.toString());
            Toast.makeText(this, "Menu Paser Error1", Toast.LENGTH_SHORT).show();
        }
    }

    public class ContentViewPagerAdapter extends FragmentStatePagerAdapter {

        private Context context;
        private int pagerCount;
        private boolean isFav;

        public ContentViewPagerAdapter(FragmentManager fragmentManager, Context context, int pagerCount, boolean isFav) {
            super(fragmentManager);
            this.context = context;
            this.pagerCount = pagerCount;
            this.isFav = isFav;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<MenuDTO> arrs = new ArrayList<>();
            String depth2Title = "";
            try {
                JSONObject menuDepth3JObj = new JSONObject(menuDepth2JsonArray.get(position).toString());
                JSONArray menuDepth3JsonArray = new JSONArray(menuDepth3JObj.getString("VALUES"));
                depth2Title = menuDepth3JObj.getString("TITLE");
                for (int i = 0, j = menuDepth3JsonArray.length(); i < j; i++) {
                    JSONObject objTitle = new JSONObject(menuDepth3JsonArray.get(i).toString());
                    arrs.add(new MenuDTO(objTitle.getString("TITLE"), objTitle.getString("URL"), objTitle.getInt("SID"), objTitle.getString("VALUE"),objTitle.getString("THUMBNAIL")));
                }
            } catch (Exception e) {
                Log.d("leftClickError", e.toString());
            }
            return ContentFragment.newInstance(position, title, arrs, isFav, groupDefaultNum,depth2Title);
        }

        @Override
        public int getCount() {
            return this.pagerCount;
        }
    }

    public void changeCount(int count) {
        this.popTopActivity.changeCount(count);
    }

    private void favList() {
        Log.d("kkkkk","kkkkkkkkk");
        Common.common_menuDTO_ArrayList = this.favDAO.findGroup(this.groupDefaultNum);
        if (this.isFav) {
            this.favText.setText("즐겨찾기 선택");
        } else {
            this.favText.setText("즐겨찾기 추가");
            this.changeCount(Common.common_menuDTO_ArrayList.size());
        }
        this.isFav = !this.isFav;
        this.contentViewPagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager(), this, this.leftArray.size(), this.isFav);
        this.viewPager.setAdapter(contentViewPagerAdapter);
        this.contentViewPagerAdapter.notifyDataSetChanged();
        tabLayout.setScrollPosition(0, 0, true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.favBt:
                if ( this.isFav ) {
                    if (Common.common_menuDTO_ArrayList != null ) {
                            Common.common_menuDTO_ArrayList_copy = new ArrayList<>();
                            for ( FavDTO row :  Common.common_menuDTO_ArrayList) {
                                Common.common_menuDTO_ArrayList_copy.add(new FavDTO(0,row.getGroupNum(),row.getDepth2Num(),row.getDepth3Num(),row.getUrl(),row.getGroupTitle(),row.getContent_title()));
                            }
                            this.favDAO.deleteAll(this.groupDefaultNum);

                        return;
                    }
                } else {
                    Intent intent = new Intent(this, ContentListActivity.class);
                    intent.putExtra("isFav",true);
                    intent.putExtra("groupId",this.groupDefaultNum);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                }
                break;
        }
    }
    public void contentAdd() {
        for ( FavDTO row :  Common.common_menuDTO_ArrayList_copy) {
            favDAO.addFav(row);
        }
        Toast.makeText(this,"즐겨찾기에 추가되었습니다.",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void finish() {
        Common.common_menuDTO_ArrayList = new ArrayList<>();
        super.finish();
        if ( this.isFav )overridePendingTransition(0, R.anim.anim_slide_out_bottom_login);
        else overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    private void idSetting() {
        this.viewPager = findViewById(R.id.content_pager);
        this.tabLayout = findViewById(R.id.content_tab);
        this.title_tv = findViewById(R.id.content_title);
        this.favText = findViewById(R.id.favText);
        this.favBt = findViewById(R.id.favBt);
        this.favBt.setOnClickListener(this);
        this.csp = new Custom_SharedPreferences(this);
        Intent intent = getIntent();
        this.groupDefaultNum = intent.getIntExtra("groupId", 0);
        this.favDAO = new FavDAO(this);
    }
}
