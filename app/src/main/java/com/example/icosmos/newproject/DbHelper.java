package com.example.icosmos.newproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
    public DbHelper(Context context){
        super(context,"myDB.db",null,1);
    }

    // 앱의 즐겨찾기로 저장하는 물품명들을 저장하는 DB
    // 이미 DB명이 존재하면 만들지 않음
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists my_table (_id integer primary key autoincrement, "
                + "name text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
};