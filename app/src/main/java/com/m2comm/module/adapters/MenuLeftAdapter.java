package com.m2comm.module.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m2comm.kses_exercise.R;

import java.util.ArrayList;

public class MenuLeftAdapter extends BaseAdapter {

    private ArrayList<String> arrayList;
    public int clickNum = 0;
    Context context;
    LayoutInflater layoutInflater;

    public MenuLeftAdapter(Context context, LayoutInflater layoutInflater , int type , ArrayList<String> arrayList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.clickNum = type;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null ) {
            convertView  = this.layoutInflater.inflate(R.layout.menu_left_group,parent,false);
            TextView title = convertView.findViewById(R.id.menu_left_text);
            LinearLayout img = convertView.findViewById(R.id.menu_choice_bg);
            BitmapDrawable background = new BitmapDrawable(this.getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.menu_choice_bg)));
            img.setBackground(background);
            if (this.clickNum == position) {
                title.setTextColor(Color.parseColor("#ffffff"));
                img.setVisibility(View.VISIBLE);
            }
            title.setText(this.arrayList.get(position));
        }

        return convertView;
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth()/3; // 테두리 곡률 설정

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        canvas.drawRect(0 , bitmap.getHeight(),bitmap.getWidth()/2,0,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return output;
    }

}
