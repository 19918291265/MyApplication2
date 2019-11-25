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

public class HistoryDealActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_historydeal);

        Intent intent_5 = getIntent();
        account_id = intent_5.getStringExtra("account");
    }

    @Override
    protected void onStart() {
        super.onStart();


        recyclerView = findViewById(R.id.recyclerView);
        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(HistoryDealActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<>();

        Cursor cursor = database.query("deal",new String[]{"deal_id","deal_shop","deal_good","deal_quantity","deal_price"},"account_id=?", new String[]{account_id},null,null,null);

        if (cursor.moveToFirst()){
            String deal_shop1 = cursor.getString(cursor.getColumnIndex("deal_shop"));
            String deal_good1 = cursor.getString(cursor.getColumnIndex("deal_good"));
            String deal_quantity1 = cursor.getString(cursor.getColumnIndex("deal_quantity"));
            String deal_price1 = cursor.getString(cursor.getColumnIndex("deal_price"));
            String deal_id1 = cursor.getString(cursor.getColumnIndex("deal_id"));
            Map<String, Object> map1 = new HashMap<>();
            map1.put("deal_shop", deal_shop1);
            map1.put("deal_good",deal_good1);
            map1.put("deal_quantity",deal_quantity1 );
            map1.put("deal_price",deal_price1);
            map1.put("deal_id",deal_id1);
            list.add(map1);
            while (cursor.moveToNext()) {
                String deal_shop = cursor.getString(cursor.getColumnIndex("deal_shop"));
                String deal_good = cursor.getString(cursor.getColumnIndex("deal_good"));
                String deal_quantity = cursor.getString(cursor.getColumnIndex("deal_quantity"));
                String deal_price = cursor.getString(cursor.getColumnIndex("deal_price"));
                String deal_id = cursor.getString(cursor.getColumnIndex("deal_id"));
                Map<String, Object> map = new HashMap<>();
                map.put("deal_shop", deal_shop);
                map.put("deal_good",deal_good);
                map.put("deal_quantity",deal_quantity );
                map.put("deal_price",deal_price);
                map.put("deal_id",deal_id);
                list.add(map);
            }
        }
        cursor.close();
        database.close();
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryDealActivity.this));//垂直排列
        recyclerView.setAdapter(new MyAdapter5(HistoryDealActivity.this, list));//绑定适配器
    }


}