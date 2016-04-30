package com.example.balamasthang.smartattendance;

/**
 * Created by Balamasthan  G on 19-12-2015.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class DBController  extends SQLiteOpenHelper {

    public DBController(Context mainActivity) {
        super(mainActivity, "user.db", null, 1);
    }
    //Creates Table



    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
       // query = "CREATE TABLE user ( userId INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, sync INTEGER DEFAULT 0  )";
        query = "CREATE TABLE user ( Sl INTEGER PRIMARY KEY AUTOINCREMENT, stuId TEXT, stuName TEXT, sync INTEGER DEFAULT 0  )";
        database.execSQL(query);
     /*   query = "CREATE TABLE selt ( userId INTEGER, userName TEXT)";
        database.execSQL(query);*/
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS user";
        database.execSQL(query);
        onCreate(database);
       /* query= "DROP TABLE IF EXISTS selt";
        database.execSQL(query);*/
    }

    public void Delete()
    {
        SQLiteDatabase database;
        database = this.getWritableDatabase();
       //database.delete("user",null,null);
        String query;
        query = "DROP TABLE IF EXISTS user";
        database.execSQL(query);
        onCreate(database);

    }


    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertUser(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stuId", queryValues.get("userId"));
        values.put("stuName", queryValues.get("userName"));
        database.insert("user", null, values);
        database.close();
    }


    public void update(int i,int j){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement st = db.compileStatement("UPDATE user SET sync=? WHERE Sl=?");
        st.bindString(1, String.valueOf(i));
        st.bindString(2, String.valueOf(j));
        st.execute();

    }
    public void updateall(int i,int j){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement st = db.compileStatement("UPDATE user SET sync=? WHERE sync=?");
        st.bindString(1, String.valueOf(i));
        st.bindString(2,String.valueOf(j));
        st.execute();

    }

    public String send(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
       /* String selectQuery = "SELECT  * FROM users where Dat = 'datetime()'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);*/
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(true,"user",new String[]{"stuId","stuName"},"sync"+"=?",new String[]{"1"},null,null,null,null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("stuId", cursor.getString(0));
                map.put("stuName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);

    }

    public int dbSyncCount(){
        int count = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(true,"user",new String[]{"stuId","stuName"},"sync"+"=?",new String[]{"1"},null,null,null,null);
        count = cursor.getCount();
        database.close();
        return count;
    }


    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  stuId,stuName FROM user";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("stuId", cursor.getString(0));
                map.put("stuName", cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getAllsel() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
      //  String selectQuery = "SELECT  * FROM user where userName=hameed";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(true,"user",new String[]{"stuId","stuName"},"sync"+"=?",new String[]{"1"},null,null,null,null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("stuId", cursor.getString(0));
                map.put("stuName", cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }




        database.close();
        return usersList;
    }



}

