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

public class ChangeAccountDateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button cipher;
    private Button name;
    private Button introduce;
    private Button address;
    private EditText cipher_1;
    private EditText name_1;
    private EditText introduce_1;
    private EditText address_1;
    private String account_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_accountdate);

        Intent intent_3 = getIntent();
        account_id = intent_3.getStringExtra("id");
        initUI();

        cipher.setOnClickListener(this);
        name.setOnClickListener(this);
        introduce.setOnClickListener(this);
        address.setOnClickListener(this);
    }

    private void initUI() {
        cipher = findViewById(R.id.bt_1);
        name = findViewById(R.id.bt_2);
        introduce = findViewById(R.id.bt_3);
        address = findViewById(R.id.bt_4);
        cipher_1 = findViewById(R.id.et_1);
        name_1 = findViewById(R.id.et_2);
        introduce_1 = findViewById(R.id.et_3);
        address_1 = findViewById(R.id.et_4);
    }

    @Override
    public void onClick(View v) {
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(ChangeAccountDateActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        switch (v.getId()) {

            case R.id.bt_1: {
                String cipher = cipher_1.getText().toString();
                if (cipher.length() < 4) {
                    Toast.makeText(this, "密码至少4位！", Toast.LENGTH_SHORT).show();
                    return;}
                    else if (!Util.isEmpty(cipher))
                    {Toast.makeText(ChangeAccountDateActivity.this, "密码中输入了非法字符！", Toast.LENGTH_SHORT).show(); return;}
                 else {
                    ContentValues values = new ContentValues();
                    values.put("cipher", cipher);//第一个"name" 是字段名字  第二个是对应字段的数据
                    database.update("user", values, "account_id=?", new String[]{account_id});
                    database.close();
                    Toast.makeText(this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            case R.id.bt_2: {
                String name = name_1.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(this, "昵称不得为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    database.update("user", values, "account_id=?", new String[]{account_id});
                    database.close();
                    Toast.makeText(this, "昵称修改成功！", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            case R.id.bt_3: {
                String introduce = introduce_1.getText().toString();
                if (introduce.length() == 0) {
                    Toast.makeText(this, "密码至少4位！", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put("introduce", introduce);
                    database.update("user", values, "account_id=?", new String[]{account_id});
                    database.close();
                    Toast.makeText(this, "简介修改成功！", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            case R.id.bt_4: {
                String address = address_1.getText().toString();
                if (address.length() == 0) {
                    Toast.makeText(this, "密码至少4位！", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put("address", address);//第一个"name" 是字段名字  第二个是对应字段的数据
                    database.update("user", values, "account_id=?", new String[]{account_id});
                    database.close();
                    Toast.makeText(this, "地址修改成功！", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }

    }
}

