package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageButton goToConnect = (ImageButton) findViewById(R.id.connectButton);
        goToConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadConnectActivity = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intentLoadConnectActivity);
            }
        });

        ImageButton goToSetting = (ImageButton) findViewById(R.id.settingsButton);
        goToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentLoadSettingsActivity);

            }
        });
    }
}