package com.example.myapplication2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicShopActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private TextView account_name;
private ImageView picture_show;
    private RecyclerView recyclerView;
    private TextView account_address;
   private TextView account_introduce;
   private String account_id;
   private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_publicshop);
        Intent intent_load = getIntent();
        account_id = intent_load.getStringExtra("id");

        initUI();
     }

    @Override
    protected void onStart() {
        super.onStart();



        MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(PublicShopActivity.this);
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("user", new String[]{"name","address","introduce","picture"}, "account_id=?", new String[]{account_id}, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
           String introduce = cursor.getString(cursor.getColumnIndex("introduce"));
            String picture = cursor.getString(cursor.getColumnIndex("picture"));

            bitmap = stringToBitmap(picture);

            picture_show.setImageBitmap(bitmap);
            account_introduce.setText("个人简介：" + introduce);
            account_name.setText(name);
            account_address.setText("地址:" + address);

        }

        MyDataBaseHelper dataBaseHelper_1 = new MyDataBaseHelper(PublicShopActivity.this);
        SQLiteDatabase database_1 = dataBaseHelper_1.getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<>();

        Cursor cursor_1 = database.query("shop",new String[]{"shop_name","shop_id"},null,
                null,null,null,null);

        if (cursor_1.moveToFirst()){
            String name1 = cursor_1.getString(cursor_1.getColumnIndex("shop_name"));
            String shop_id1 = cursor_1.getString(cursor_1.getColumnIndex("shop_id"));
            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", name1);
            map1.put("shop_id",shop_id1);
            map1.put("his",account_id);
            list.add(map1);
            while (cursor_1.moveToNext()) {
                String name = cursor_1.getString(cursor_1.getColumnIndex("shop_name"));
                String shop_id = cursor_1.getString(cursor_1.getColumnIndex("shop_id"));
                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("shop_id",shop_id);
                map.put("his",account_id);
                list.add(map);
            }
        }
        cursor_1.close();
        database_1.close();

        recyclerView.setAdapter(new MyAdapter1(PublicShopActivity.this, list));//绑定适配器


    }

    @SuppressLint("WrongConstant")
    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.nav_view);


        View view = navigationView.inflateHeaderView(R.layout.layout_drawer_head);
        account_name = view.findViewById(R.id.account_name);
        account_address = view.findViewById(R.id.account_address);
       account_introduce = view.findViewById(R.id.account_introduce);
       picture_show = view.findViewById(R.id.imageView);

        recyclerView = findViewById(R.id.recyclerView);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PublicShopActivity.this);
        ((LinearLayoutManager) layoutManager).setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//垂直排列 , Ctrl+P

        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_openshop:
                Intent intent = new Intent(PublicShopActivity.this, OpenShopActivity.class);
                intent.putExtra("id", account_id);
                startActivity(intent);
                break;

            case R.id.nav_myshop: {
                Intent intent_2 = new Intent(PublicShopActivity.this, PrivateShopActivity.class);
                intent_2.putExtra("id", account_id);
                startActivity(intent_2);
                break;
            }
            case R.id.nav_changeinformation: {
                Intent intent_3 = new Intent(PublicShopActivity.this, ChangeAccountDateActivity.class);
                intent_3.putExtra("id", account_id);
                startActivity(intent_3);
                break;
            }
            case R.id.nav_changeaccount: {
                Intent intent_4 = new Intent(PublicShopActivity.this, LoginActivity.class);
                startActivity(intent_4);
                Toast.makeText(this, "切换用户", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
            case R.id.nav_history: {
                Intent intent_5 = new Intent(PublicShopActivity.this, HistoryDealActivity.class);
                intent_5.putExtra("account",account_id);
                startActivity(intent_5);
                Toast.makeText(this, "剁手吧剁手吧", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
