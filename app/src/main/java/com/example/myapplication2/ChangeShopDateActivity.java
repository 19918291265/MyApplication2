package com.example.myapplication2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChangeShopDateActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button delete;
    private EditText et_1;
    private EditText et_2;
    private EditText et_3;

    private String shop_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_shopdate);

        Intent intent_3 = getIntent();
        shop_id = intent_3.getStringExtra("id");
        initUI();

        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);

    }

    private void initUI() {
        bt_1 = findViewById(R.id.bt_1);
        bt_2 = findViewById(R.id.bt_2);
        bt_3 = findViewById(R.id.bt_3);

        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        et_3 = findViewById(R.id.et_3);



    }

    @Override
    public void onClick(View view) {
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ChangeShopDateActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        switch(view.getId()){

            case R.id.bt_1:
            {
                String name = et_1.getText().toString();
                if (name.length()==0){Toast.makeText(this, "店铺名称不得为空！", Toast.LENGTH_SHORT).show();return;}
                else {
                ContentValues values = new ContentValues();
                values.put("shop_name",name );//第一个"name" 是字段名字  第二个是对应字段的数据
                database.update("shop", values, "shop_id=?", new String[]{shop_id});
                database.close();
                Toast.makeText(this, "昵称修改成功！", Toast.LENGTH_SHORT).show();
                break;}
            }
            case R.id.bt_2:
            {String owner = et_2.getText().toString();
                if (owner.length()==0){Toast.makeText(this, "请确定店铺所有者！", Toast.LENGTH_SHORT).show();return;}
                else {
                ContentValues values = new ContentValues();
                values.put("shop_owner", owner);//第一个"name" 是字段名字  第二个是对应字段的数据
                database.update("shop", values, "shop_id=?", new String[]{shop_id});
                database.close();
                Toast.makeText(this, "所有者修改成功！", Toast.LENGTH_SHORT).show();
                break;}}
            case R.id.bt_3:
            {String introduce = et_3.getText().toString();
                if (introduce.length()==0){Toast.makeText(this, "店铺简介不得为空！", Toast.LENGTH_SHORT).show();return;}
                else {
                ContentValues values = new ContentValues();
                values.put("shop_introduce", introduce);//第一个"name" 是字段名字  第二个是对应字段的数据
                database.update("shop",values, "shop_id=?", new String[]{shop_id});
                database.close();
                Toast.makeText(this, "简介修改成功！", Toast.LENGTH_SHORT).show();
                break;}}


        }

    }
}