package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateShopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_private_shop);

        Intent intent_2 = getIntent();
        account_id = intent_2.getStringExtra("id");
    }

    @Override
    protected void onStart() {
        super.onStart();


    recyclerView = findViewById(R.id.recyclerView);
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(PrivateShopActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<>();

        Cursor cursor = database.query("shop",new String[]{"shop_name","shop_id"},"account_id=?", new String[]{account_id},null,null,null);

        if (cursor.moveToFirst()){
            String name1 = cursor.getString(cursor.getColumnIndex("shop_name"));
            String shop_id1 = cursor.getString(cursor.getColumnIndex("shop_id"));
            Map<String, Object> map1 = new HashMap<>();
            map1.put("shop_name", name1);
            map1.put("shop_id",shop_id1);
            map1.put("his",account_id);
            list.add(map1);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("shop_name"));
                String shop_id = cursor.getString(cursor.getColumnIndex("shop_id"));
                Map<String, Object> map = new HashMap<>();
                map.put("shop_name", name);
                map.put("shop_id",shop_id);
                map.put("his",account_id);
                list.add(map);
            }
        }
        cursor.close();
        database.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(PrivateShopActivity.this));//垂直排列
        recyclerView.setAdapter(new MyAdapter3(PrivateShopActivity.this, list));//绑定适配器
    }


}
