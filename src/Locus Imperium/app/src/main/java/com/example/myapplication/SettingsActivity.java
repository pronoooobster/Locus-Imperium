package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    String [] item = {"Quiet", "Moderate", "Loud"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item= adapterView.getItemAtPosition(i).toString();
                Toast.makeText(SettingsActivity.this, "Item:" + item, Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton goToMain = (ImageButton) findViewById(R.id.mainButton);
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadMainActivity = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intentLoadMainActivity);

            }
        });

        ImageButton goToConnect = (ImageButton) findViewById(R.id.connectButton);
        goToConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadConnectActivity = new Intent(SettingsActivity.this, ConnectActivity.class);
                startActivity(intentLoadConnectActivity);
            }
        });
    }
}