package com.example.myapplication2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class BuyGoodActivity extends AppCompatActivity implements View.OnClickListener {

    private Button add;
    private Button minus;
    private TextView quantity;
    private TextView name_1;
    private TextView price_1;
    private TextView introduce_1;
    private Button buy;
    private String good_id;
    private String shop_id;
    private String account_id;
    private String price;
    private String name;
    private String shop_name;
    private Bitmap bitmap;
    private ImageView picture_show;
    int i = 1;
    float price_add;
    String price_add_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buy_good);

        Intent intent_2 = getIntent();
        good_id = intent_2.getStringExtra("good_id");
        shop_id = intent_2.getStringExtra("shop_id");
        account_id = intent_2.getStringExtra("his");


        initUI();
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(BuyGoodActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("good", new String[]{"good_name","good_price","good_introduce","good_picture"}, "good_id=?", new String[]{good_id}, null, null, null);

        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex("good_name"));
            price = cursor.getString(cursor.getColumnIndex("good_price"));
            String introduce = cursor.getString(cursor.getColumnIndex("good_introduce"));
            String picture = cursor.getString(cursor.getColumnIndex("good_picture"));

            bitmap = stringToBitmap(picture);

            picture_show.setImageBitmap(bitmap);
            name_1.setText(name);
            price_1.setText("单价：" + price);
            introduce_1.setText("商品简介：" + introduce);

        }


        add.setOnClickListener(this);
        minus.setOnClickListener(this);
        buy.setOnClickListener(this);

    }

    private void initUI() {
        add = findViewById(R.id.layout_add);
        minus = findViewById(R.id.layout_minus);
        buy = findViewById(R.id.buy_buy_buy);
        quantity = findViewById(R.id.tv_buy_quantity);
        name_1 = findViewById(R.id.name);
        price_1 = findViewById(R.id.price);
        introduce_1 = findViewById(R.id.introduce);
        picture_show = findViewById(R.id.picture);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.layout_add: {
                i++;
                quantity.setText(i + "");
                return;
            }
            case R.id.layout_minus: {
                if (i > 1) {
                    i--;
                    quantity.setText(i + "");
                } else {
                    Toast.makeText(BuyGoodActivity.this, "您至少购买一件商品", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case R.id.buy_buy_buy: {
                float price_1 = Integer.parseInt(price);
                price_add = price_1 * i;
                price_add_1=new DecimalFormat(".00").format(price_add);



                Toast.makeText(BuyGoodActivity.this, "购买成功！", Toast.LENGTH_SHORT).show();
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(BuyGoodActivity.this);
                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                Cursor cursor = database.query("shop",new String[]{"shop_name"},"shop_id=?",new String[]{shop_id},null,null,null);
                if (cursor.moveToFirst()){
                    shop_name = cursor.getString(cursor.getColumnIndex("shop_name"));
                }

                database.execSQL("insert into deal(deal_good,deal_price,deal_quantity,deal_shop,account_id) values('" + name + "','" + price_add_1 + "','" + i + "','" + shop_name + "','" + account_id + "');");
                cursor.close();
                database.close();
                break;
            }
        }
    }
    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}