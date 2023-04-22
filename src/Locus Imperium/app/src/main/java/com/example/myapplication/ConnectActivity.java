package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ImageButton goToMain = (ImageButton) findViewById(R.id.mainButton);
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadMainActivity = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(intentLoadMainActivity);

            }
        });

        ImageButton goToSetting = (ImageButton) findViewById(R.id.settingsButton);
        goToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadSettingsActivity = new Intent(ConnectActivity.this, SettingsActivity.class);
                startActivity(intentLoadSettingsActivity);

            }
        });
    }
}