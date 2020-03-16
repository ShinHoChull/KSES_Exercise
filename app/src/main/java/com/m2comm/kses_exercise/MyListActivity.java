package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.m2comm.module.Common;
import com.m2comm.module.adapters.FavViewAdapter;
import com.m2comm.module.dao.FavDAO;
import com.m2comm.module.models.FavDTO;

import java.util.ArrayList;
import java.util.Iterator;

public class MyListActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    ListView listView;
    FavViewAdapter favViewAdapter;
    ArrayList<FavDTO> arrayList;
    FavDAO favDAO;
    LinearLayout deleteBt1 , deleteBt2;
    boolean isDel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        this.idSetting();
        this.adapterChange();
    }

    public void adapterChange(){
        this.arrayList = new ArrayList<>();
        this.arrayList = this.favDAO.getAllList();
        if ( this.arrayList != null ) {
            this.favViewAdapter = new FavViewAdapter(this , getLayoutInflater() , this.arrayList , isDel);
            this.listView.setAdapter(this.favViewAdapter);
            this.favViewAdapter.notifyDataSetChanged();
        }
        Log.d("arraySize2",this.arrayList.size()+"");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.step_1_delete:
                Common.common_menuDTO_ArrayList = new ArrayList<>();
                this.deleteBt1.setVisibility(View.GONE);
                this.deleteBt2.setVisibility(View.VISIBLE);
                this.isDel = true;
                this.adapterChange();
                break;

            case R.id.step_2_delete:
                if (Common.common_menuDTO_ArrayList != null && Common.common_menuDTO_ArrayList.size() > 0) {
                    for(Iterator<FavDTO> it = Common.common_menuDTO_ArrayList.iterator(); it.hasNext() ; ) {
                        FavDTO row = it.next();
                        it.remove();
                        this.favDAO.delete(row.getNum());
                    }
                    Toast.makeText(this , "삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
                this.deleteBt1.setVisibility(View.VISIBLE);
                this.deleteBt2.setVisibility(View.GONE);
                this.isDel = false;
                break;

        }

    }

    private void idSetting() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"즐겨찾기");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.listView = findViewById(R.id.listView);
        this.deleteBt1 = findViewById(R.id.step_1_delete);
        this.deleteBt1.setOnClickListener(this);
        this.deleteBt2 = findViewById(R.id.step_2_delete);
        this.deleteBt2.setOnClickListener(this);

        this.arrayList = new ArrayList<>();
        this.favDAO = new FavDAO(this);


    }
}
