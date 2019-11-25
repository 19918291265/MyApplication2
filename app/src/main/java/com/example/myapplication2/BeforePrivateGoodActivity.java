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

import androidx.appcompat.app.AppCompatActivity;

public class BeforePrivateGoodActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_good;
    private Button bt_delete;
    private Button bt_manage;
    private Button bt_changedata;
    private TextView shop_name;
    private TextView shop_owner;
    private TextView shop_introduce;
    private String shop_id ;
    private String account_id;
    private Bitmap bitmap;
    private ImageView picture_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_beforeprivategood);

        Intent intent = getIntent();
        shop_id = intent.getStringExtra("id");
        account_id = intent.getStringExtra("his");
    }

    @Override
    protected void onStart() {
        super.onStart();


    initUI();
        bt_good.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        bt_changedata.setOnClickListener(this);
        bt_manage.setOnClickListener(this);



        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(BeforePrivateGoodActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("shop",new String[]{"shop_owner","shop_introduce","shop_picture"},"shop_id=?",new String[]{shop_id},null,null,null);
        if (cursor.moveToFirst()){
            String owner = cursor.getString(cursor.getColumnIndex("shop_owner"));
            String introduce = cursor.getString(cursor.getColumnIndex("shop_introduce"));
            String picture = cursor.getString(cursor.getColumnIndex("shop_picture"));

            bitmap = stringToBitmap(picture);

            picture_show.setImageBitmap(bitmap);
            shop_owner.setText(owner + "的店铺");
            shop_introduce.setText("店铺简介:" + introduce);
            cursor.close();
            database.close();
        }

    }
    private void initUI() {
        shop_owner = findViewById(R.id.owner);
        shop_introduce = findViewById(R.id.introduce);
        bt_good = findViewById(R.id.enter);
        bt_delete = findViewById(R.id.delete);
        bt_manage = findViewById(R.id.manage);
        bt_changedata = findViewById(R.id.change);
        picture_show = findViewById(R.id.picture);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.enter:
                Intent intent_1 = new Intent(BeforePrivateGoodActivity.this,PrivateGoodActivity.class);
                intent_1.putExtra("id",shop_id);
                intent_1.putExtra("his",account_id);
                startActivity(intent_1);
                break;
            case R.id.delete:
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(BeforePrivateGoodActivity.this);
                SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                database.delete("shop", "shop_id=?", new String[]{shop_id});
                database.close();
                Toast.makeText(this, "店铺删除成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.manage:
                Intent intent_2 = new Intent(BeforePrivateGoodActivity.this, OpenGoodActivity.class);
                intent_2.putExtra("id",shop_id);
                startActivity(intent_2);
                break;
            case R.id.change:
                Intent intent_3 = new Intent(BeforePrivateGoodActivity.this,ChangeShopDateActivity.class);
                intent_3.putExtra("id",shop_id);
                startActivity(intent_3);
                break;
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
