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

import com.example.myapplication2.Util.Util;

public class ChangeGoodDateActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button delete_good;

    private EditText et_1;
    private EditText et_2;
    private EditText et_3;

    private String good_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_gooddate);

        Intent intent_1 = getIntent();
        good_id = intent_1.getStringExtra("id");
        initUI();
delete_good.setOnClickListener(this);
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

        delete_good = findViewById(R.id.delete);

    }

    @Override
    public void onClick(View view) {
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ChangeGoodDateActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        switch(view.getId()){

            case R.id.bt_1:
            {
                String name = et_1.getText().toString();
                if (name.length()==0){Toast.makeText(this, "名称不能为空！", Toast.LENGTH_SHORT).show();return;}
                else {
                ContentValues values = new ContentValues();
                values.put("good_name",name );//第一个"name" 是字段名字  第二个是对应字段的数据
                database.update("good", values, "good_id=?", new String[]{good_id});
                database.close();
                Toast.makeText(this, "名称修改成功！", Toast.LENGTH_SHORT).show();
                break;}
            }
            case R.id.bt_2:
            {String price = et_2.getText().toString();
                if (price.length()==0){Toast.makeText(this, "请确定价格！", Toast.LENGTH_SHORT).show();return;}
                else if (!Util.isPrice_1(price))
                { Toast.makeText(ChangeGoodDateActivity.this, "请输入合法的商品价格！", Toast.LENGTH_SHORT).show();return; }
                else {
                ContentValues values = new ContentValues();
                values.put("good_price", price);
                database.update("good", values, "good_id=?", new String[]{good_id});
                database.close();
                Toast.makeText(this, "价格修改成功！", Toast.LENGTH_SHORT).show();
                break;}}
            case R.id.bt_3:
            {String introduce = et_3.getText().toString();
                if (introduce.length()==0){Toast.makeText(this, "简介不能为空！", Toast.LENGTH_SHORT).show();return;}
                else {
                ContentValues values = new ContentValues();
                values.put("good_introduce", introduce);
                database.update("good",values, "good_id=?", new String[]{good_id});
                database.close();
                Toast.makeText(this, "简介修改成功！", Toast.LENGTH_SHORT).show();
                break;}}
            case R.id.delete:

                MyDataBaseHelper dataBaseHelper_1 = new MyDataBaseHelper(ChangeGoodDateActivity.this);
                SQLiteDatabase database_1 = dataBaseHelper_1.getReadableDatabase();
                database_1.delete("good", "good_id=?", new String[]{good_id});
                database_1.close();
                Toast.makeText(ChangeGoodDateActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        }
    }
