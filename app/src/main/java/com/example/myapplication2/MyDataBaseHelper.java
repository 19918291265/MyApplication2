package com.example.myapplication2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public MyDataBaseHelper(Context context){
        super(context,"database",null,3);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //下面这句是简单的SQL语句,创建数据库的表
        String TABLE_USER = "create table user(account_id INTEGER PRIMARY KEY autoincrement,account text" +
                ",cipher text,name text,picture text,address text,introduce text);";
        sqLiteDatabase.execSQL(TABLE_USER);
        String TABLE_SHOP = "create table shop(shop_id INTEGER PRIMARY KEY autoincrement,shop_name " +
                "text,shop_owner text,shop_introduce text,shop_picture text,account_id text)";
        sqLiteDatabase.execSQL(TABLE_SHOP);
        String TABLE_GOOD = "create table good(good_id INTEGER PRIMARY KEY autoincrement,good_price " +
                "text,good_name text,good_picture text,shop_id text,good_introduce text)";
        sqLiteDatabase.execSQL(TABLE_GOOD);
        String TABLE_DEAL = "create table deal(deal_id INTEGER PRIMARY KEY autoincrement,deal_shop " +
                "text,deal_good text,deal_quantity text,deal_price text,account_id text)";
        sqLiteDatabase.execSQL(TABLE_DEAL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //更新数据库版本时才会执行onUpgrade
        sqLiteDatabase.execSQL("drop table if exists user");
        onCreate(sqLiteDatabase);
    }
}