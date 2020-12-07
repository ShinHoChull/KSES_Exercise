package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.FullScreenableChromeClient;
import com.m2comm.module.dao.FavDAO;
import com.m2comm.module.models.FavDTO;
import com.m2comm.module.models.MenuDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ContentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    EditText detail_edittext;
    String title,content_title_txt , groupTitle;
    TextView content_title ;
    private ArrayList<MenuDTO> arrayList;
    private int groupNum , depth2Num , depth3Num;
    private ImageView backBt , nextBt;
    private FavDTO favDTO , findDTO;
    private FavDAO favDAO;
    private LinearLayout favBt;
    private WebView webview;

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
        this.favBt = findViewById(R.id.favBt);
        this.favBt.setOnClickListener(this);
        this.webview = findViewById(R.id.webview);

        //this.nextBt = findViewById(R.id.nextBt);
        //this.nextBt.setOnClickListener(this);
        //this.backBt = findViewById(R.id.backBt);
        //this.backBt.setOnClickListener(this);

        this.webview.setWebViewClient(new WebviewCustomClient());
        webview.setWebChromeClient(new FullScreenableChromeClient(this));
        this.webview.getSettings().setUseWideViewPort(true);
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setLoadWithOverviewMode(true);
        this.webview.getSettings().setDefaultTextEncodingName("utf-8");
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.getSettings().setSupportMultipleWindows(false);
        this.webview.getSettings().setDomStorageEnabled(true);
        this.webview.getSettings().setBuiltInZoomControls(true);
        this.webview.getSettings().setDisplayZoomControls(false);
        this.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.webview.getSettings().setTextZoom(90);

        Log.d("urlll=",this.arrayList.get(this.depth3Num).getUrl());

        this.webview.loadUrl(this.arrayList.get(this.depth3Num).getUrl());
        this.detail_edittext.setText(this.arrayList.get(this.depth3Num).getValue().trim());
        //this.webview.loadUrl("https://live360.co.kr/kses/20200710/3-3.html");
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public void setScreenLan() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void setScreenPor() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private class WebviewCustomClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            String[] urlCut = url.split("/");
            Log.d("NowUrl",url);

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("onPageStarted",url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d("onLoadResource",url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("onPageFinished",url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(getApplicationContext(), "서버와 연결이 끊어졌습니다", Toast.LENGTH_SHORT ).show();
            view.loadUrl("about:blank");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        this.idSetting();
        this.detail_edittext.setFocusableInTouchMode(false);
        this.detail_edittext.clearFocus();


        this.content_title.setText(this.content_title_txt);
        this.getFindDTO();
    }

    public void getFindDTO () {
        this.findDTO = this.favDAO.find(this.groupNum , this.depth2Num , this.depth3Num);
        if ( findDTO == null ) {
            this.favBt.setBackgroundResource(R.drawable.content_fav_radius_off);
        } else {
            this.favBt.setBackgroundResource(R.drawable.content_fav_radius);
        }
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
                if ( findDTO != null ) return;
                this.getMenuDataSetting();
                this.favDAO.addFav(this.favDTO);
                Toast.makeText(this, "즐겨찾기에 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                this.getFindDTO();
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
                    tt.getString("THUMBNAIL") , depth1Title + " > "+depth2Title,depth3Title);



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
