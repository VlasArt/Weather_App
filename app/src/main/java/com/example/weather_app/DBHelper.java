package com.example.weather_app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "citydb.db";
    final static String TABLE_NAME = "cities";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME + " (_id INT PRIMARY KEY, name VARCHAR(30) NOT NULL, country VARCHAR(3) NOT NULL)");
        initSimpleValues(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void initSimpleValues(SQLiteDatabase db){
        db.execSQL("INSERT INTO cities VALUES (473537, \"Vinogradovo\", \"RU\");");
        db.execSQL("INSERT INTO cities VALUES (2023469, \"Irkutsk\", \"RU\");");
        db.execSQL("INSERT INTO cities VALUES (524901, \"Moscow\", \"RU\");");
    }

    public void addCity(SQLiteDatabase db, int id, String cityName, String countryCode){
        db.execSQL("INSERT INTO cities VALUES (" + Integer.toString(id) + ", \"" + cityName + "\", \"" + countryCode + "\");");
    }

    public void removeAll(SQLiteDatabase db){
        db.execSQL("DELETE FROM " + TABLE_NAME + ";" );
    }
}
