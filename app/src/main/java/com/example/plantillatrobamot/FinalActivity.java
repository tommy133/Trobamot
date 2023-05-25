package com.example.plantillatrobamot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FinalActivity extends AppCompatActivity {
    private int widthDisplay;
    private int heightDisplay;
    private final int textViewSize = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);


        // Object to store display information
        DisplayMetrics metrics = new DisplayMetrics();
        // Get display information
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthDisplay = metrics.widthPixels;
        heightDisplay = metrics.heightPixels;

        Intent intent = getIntent() ;
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        ConstraintLayout constraintLayout = findViewById(R.id.layout_final);
        TextView textView = new TextView(this);

        textView.setWidth(textViewSize);
        textView.setHeight(textViewSize);
        // Posicionam el TextView
        textView.setX((widthDisplay/3 - textViewSize/2)+textViewSize);
        textView.setY((heightDisplay/2 - textViewSize/2)+textViewSize);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(30);

        try {
            textView.setText(agafaHTML("cadira"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Afegir el TextView al layout
        constraintLayout.addView(textView);
    }

    public String agafaHTML ( String paraula ) throws IOException {
        URL definition = new URL("https://www.vilaweb.cat/paraulogic/?diec="+ paraula);
        BufferedReader br = new BufferedReader (new InputStreamReader(definition.openStream()));
        return llegirBuffer(br).toString();
    }

    private StringBuffer llegirBuffer(BufferedReader br) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String line;

        while ((line = br.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator()); // Afegir un salt de línia després de cada línia llegida
        }

        br.close();

        return buffer;
    }
}