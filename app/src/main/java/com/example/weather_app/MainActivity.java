package com.example.weather_app;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    String ApiKey = "ff8bfc954e3dcff558deb21cd9a5fca5";
    SQLiteDatabase db;
    DBHelper dbHelper;
    ListView citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        citiesList = findViewById(R.id.citiesList);

        dbHelper= new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        upgradeCities(false);
        GetCities task = new GetCities();
        task.execute();
    }

    private void upgradeCities(boolean withParam){
        String sortingString = "ORDER BY country, name, _id ASC LIMIT 15;";
        if (withParam){
            EditText searchEdit = findViewById(R.id.search_text);
            String searchString = searchEdit.getText().toString();
            sortingString = "WHERE (name like \"%" + searchString + "%\") " + sortingString;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM cities " + sortingString, null);
        String[] fields = {"_id", "name", "country"};
        int[] resIds = {R.id.id, R.id.city_name, R.id.country};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, fields, resIds,0);
        citiesList.setAdapter(adapter);
    }

    public void ShowWeather(View view){
        Intent intent = new Intent(MainActivity.this, Exactly_The_Weather.class);
        TextView id = view.findViewById(R.id.id);
        intent.putExtra("cityID", id.getText().toString());
        startActivity(intent);

        Log.d("myTeg", "Showing weather by " + id.getText().toString());
    }

    public void ShowDefultWether(View view){
        Intent intent = new Intent(MainActivity.this, Exactly_The_Weather.class);
        intent.putExtra("cityID", "2023469");
        startActivity(intent);
    }

    public void searchCityes(View view){
        upgradeCities(true);
    }

    public void cancelSearch(View view){
        upgradeCities(false);
    }

    class GetCities extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String result = "";

            try {
                InputStream ourJson = getAssets().open("city.list.json");
                int size = ourJson.available();
                byte[] buffer = new byte[size];
                ourJson.read(buffer);
                ourJson.close();
                result = new String(buffer);

                JSONArray jsonArray = new JSONArray(result);
                Gson gson = new Gson();
                dbHelper.removeAll(db);

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    CityData element = gson.fromJson(object.toString(),CityData.class);
                    dbHelper.addCity(db, element.id,element.name,element.country);
                }

            } catch (IOException e){
            } catch (JSONException je){}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            upgradeCities(false);
        }
    }
}

class CityData{
    int id;
    String name, country;
}