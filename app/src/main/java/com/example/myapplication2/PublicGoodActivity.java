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

public class PublicGoodActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String shop_id;
    private String account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_publicgood);

        Intent intent = getIntent();
        shop_id = intent.getStringExtra("id");
        account_id = intent.getStringExtra("his");
    }

    @Override
    protected void onStart() {
        super.onStart();


        recyclerView = findViewById(R.id.recyclerView);
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(PublicGoodActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<>();

        Cursor cursor = database.query("good",new String[]{"good_name","good_id"},"shop_id=?", new String[]{shop_id},null,null,null);

        if (cursor.moveToFirst()){
            String name1 = cursor.getString(cursor.getColumnIndex("good_name"));
            String good_id1 = cursor.getString(cursor.getColumnIndex("good_id"));
            Map<String, Object> map1 = new HashMap<>();
            map1.put("good_name", name1);
            map1.put("good_id",good_id1);
            map1.put("shop_id",shop_id);
            map1.put("his",account_id);
            list.add(map1);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("good_name"));
                String good_id = cursor.getString(cursor.getColumnIndex("good_id"));
                Map<String, Object> map = new HashMap<>();
                map.put("good_name", name);
                map.put("good_id",good_id);
                map.put("shop_id",shop_id);
                map.put("his",account_id);
                list.add(map);
            }
        }
        cursor.close();
        database.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(PublicGoodActivity.this));//垂直排列
        recyclerView.setAdapter(new MyAdapter2(PublicGoodActivity.this, list));//绑定适配器
    }


}
