package com.example.weather_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String ApiKey = "ff8bfc954e3dcff558deb21cd9a5fca5";
    Button ShowWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void ShowWether(View view){
        Intent intent = new Intent(MainActivity.this, Exactly_The_Weather.class);
        startActivity(intent);
    }
}
