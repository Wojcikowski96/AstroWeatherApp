package com.example.astroapp.LocationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "locations.db" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table FavCities " +
                        "(Id integer primary key, Name text, WOEID integer, Longitude real, Latitude real)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS FavCities");
        onCreate(db);
    }

    public boolean insert (String name, Long woeid, Double longitude, Double latitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("WOEID", woeid);
        contentValues.put("Longitude", longitude);
        contentValues.put("Latitude", latitude);
        db.insert("FavCities", null, contentValues);
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "FavCities");
    }

    public boolean update (Integer id, String name, Long woeid, Double longitude, Double latitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("WOEID", woeid);
        contentValues.put("Longitude", longitude);
        contentValues.put("Latitude", latitude);
        db.update("FavCities", contentValues, "Id = ? ", new String[] { Integer.toString(id) });
        return true;
    }

    public Integer delete (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("FavCities", "Id = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAll() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from FavCities", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex("Name")));
            res.moveToNext();
        }
        res.close();
        System.out.println("Lista miast w bazie"+array_list);
        return array_list;
    }

    public Integer deleteAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("FavCities",null,null);
    }

    public String getData(String name, String data) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query="select * from FavCities where Name=?";
        Cursor res = db.rawQuery( query, new String[] { name } );
        res.moveToFirst();
        String dbResult = res.getString(res.getColumnIndex(data));
        res.moveToNext();
        res.close();
        return dbResult;
    }

    public boolean checkIfRecordExists(String name) {

        SQLiteDatabase db = this.getReadableDatabase();
        String allcities = "select * from FavCities where Name=?";
        String query="select * from FavCities where Name=?";
        Cursor res = db.rawQuery( query, new String[] { name } );
        int queryResultCount=res.getCount();
        res.close();
        return queryResultCount > 0;
    }
}
