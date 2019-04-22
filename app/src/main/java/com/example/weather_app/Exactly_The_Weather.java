package com.example.weather_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Exactly_The_Weather extends AppCompatActivity {
    TextView city,time_daylong,time_sunrise,time_sunset,temperature,windSide,humadity,presure;
    String cityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exactly__the__weather);

        init();

        Intent intent = getIntent();
        cityID = intent.getStringExtra("cityID");

        GetWaetherTask task = new GetWaetherTask();
        Log.d("myTeg", "res - ");
        task.execute();
        Log.d("myTeg", "res - ");
    }

    void init(){
        city            = findViewById(R.id.city);
        time_daylong    = findViewById(R.id.time_daylong);
        time_sunrise    = findViewById(R.id.time_sunrise);
        time_sunset     = findViewById(R.id.time_sunset);
        temperature     = findViewById(R.id.temperature);
        windSide        = findViewById(R.id.windSide);
        humadity        = findViewById(R.id.humadity);
        presure         = findViewById(R.id.presure);
    }

    class GetWaetherTask extends AsyncTask<Integer,Void,OpenWeatherMap> {
        String ApiKey = "ff8bfc954e3dcff558deb21cd9a5fca5";

        @Override
        protected OpenWeatherMap doInBackground(Integer... integers) {
            String result = "";
            Log.d("myTeg", "res - " + result);
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=" + cityID + "&appid=" + ApiKey);//integers[0] + "&appid=" + ApiKey);
                Scanner in = new Scanner((InputStream) url.getContent());
                Log.d("myTeg", "res - " + result);
                while (in.hasNext()) {
                    result += in.nextLine();
                }
                Log.d("myTeg", "res - " + result);
                Gson gson = new Gson();
                if (result.length() > 0) {
                    OpenWeatherMap owm = gson.fromJson(result, OpenWeatherMap.class);
                    return owm;
                }
            } catch (IOException e) {}
            return new OpenWeatherMap();
        }

        @Override
        protected void onPostExecute(OpenWeatherMap openWeatherMap) {
            openWeatherMap.normalize();
            city.setText(openWeatherMap.name);
            time_daylong.setText("Продолжительнооть дня: " + getTime(Math.abs(openWeatherMap.sys.sunrise - openWeatherMap.sys.sunset)));
            time_sunrise.setText("Время восхода: " + getDate(openWeatherMap.sys.sunrise));
            time_sunset.setText("Время захода: " + getDate(openWeatherMap.sys.sunset));
            temperature.setText("Температура: " + openWeatherMap.main.temp + "°C");
            windSide.setText("Направление ветра: " + getWindDirection(openWeatherMap.wind.deg,openWeatherMap.wind.speed));
            humadity.setText("Влажность: " + openWeatherMap.main.humidity + "%");
            presure.setText("Давление: " + getPresure(openWeatherMap.main.pressure));
        }

        protected String getPresure(int value){
            return Long.toString(Math.round(value * 0.750064))  + "мм. рт. ст";
        }

        protected String getWindDirection(int value, float speed){
            if (speed == 0){
                return "Нет ветра!";
            }else if (value > 348 || value < 12){
                return "N";
            }else if (value < 34){
                return "NNE";
            }else if (value < 57){
                return "NE";
            }else if (value < 79){
                return "ENE";
            }else if (value < 102){
                return "E";
            }else if (value < 124){
                return "ESE";
            }else if (value < 147){
                return "SE";
            }else if (value < 169){
                return "SSE";
            }else if (value < 192){
                return "S";
            }else if (value < 214){
                return "SSW";
            }else if (value < 237){
                return "SW";
            }else if (value < 259){
                return "WSW";
            }else if (value < 282){
                return "W";
            }else if (value < 304){
                return "WNW";
            }else if (value < 327){
                return "NW";
            }else if (value < 349){
                return "NNW";
            }else {
                return "Эээээм, что-то не то";
            }
        }

        protected String getTime(int value){
            value /= 60;
            String min;
            int minutes = value % 60;
            if (minutes < 10){
                min = "0" + Integer.toString(minutes);
            }else {
                min = Integer.toString(minutes);
            }
            value /= 60;
            int hours = value % 24;
            String hr;
            if (hours < 10){
                hr = "0" + Integer.toString(hours);
            }else {
                hr = Integer.toString(hours);
            }
            return hr + ":" + min;
        }

        protected String getDate(int value){
            Date date = new Date(value * 1000);
            SimpleDateFormat sfd = new SimpleDateFormat("HH:mm");
            return sfd.format(date);
        }
    }
}

class OpenWeatherMap{
    //Weather[] weathers;
    M main;
    Wind wind;
    String name;
    Sys sys;

    public void normalize(){
        this.main.temp = Math.round(this.main.temp - (float) 273.15);
    }
}

class Weather{
    int id;
    String main, description, icon;
}

class M{
    float temp;
    int pressure, humidity, grnd_level;

    @Override
    public String toString() {
        return "M{" +
                "temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}

class Wind{
    float speed;
    int deg;
}

class Sys{
    int sunrise,sunset;
}