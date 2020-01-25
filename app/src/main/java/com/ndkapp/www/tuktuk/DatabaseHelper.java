package com.ndkapp.www.tuktuk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_name = "time.db";
    public static final String table_name = "data";

    public static final String col_1 = "NAME";
    public static final String col_2 = "NUMBER";
    public static final String col_3 = "EMAIL";


    public DatabaseHelper(Context context) {
        super(context, Database_name, null, 1);//creates Database
        //  SQLiteDatabase db = this.getWritableDatabase(); //creates database and table
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table_name + " (NAME TEXT ,NUMBER TEXT, EMAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public boolean insertData(String name, String mobile, String email) {
        SQLiteDatabase db = this.getWritableDatabase();//creates database and table
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, name);
        contentValues.put("NUMBER", mobile);
        contentValues.put(col_3, email);
        long result = db.insert(table_name, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public String getNumber(String pkg) {
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("select NUMBER from " + table_name + " where EMAIL='" + pkg.trim() + "';", null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("NUMBER");
        if (cursor != null && cursor.getCount() > 0) {
            String lim = cursor.getString(index);
            cursor.close();
            return lim;
        }
        return "-1";
    }

    public String getName(String pkg) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select NAME from " + table_name + " where EMAIL='" + pkg + "';", null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("NAME");
        if (cursor != null && cursor.getCount() > 0) {
            String lim = cursor.getString(index);
            cursor.close();
            return lim;
        }
        return "-1";
    }

    public Integer deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name, "NAME = ?", new String[]{name});
    }
}
