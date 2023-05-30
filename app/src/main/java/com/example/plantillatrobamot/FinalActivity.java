package com.example.plantillatrobamot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class FinalActivity extends AppCompatActivity {
    private boolean win;
    private String word, restricciones, posiblesSoluciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent intent = this.getIntent();
        win = intent.getBooleanExtra("VICTORIA",false);
        word = intent.getStringExtra("WORD").toLowerCase();
        restricciones = intent.getStringExtra("RESTRICCIONES");
        posiblesSoluciones = intent.getStringExtra("POSIBLES_SOLUCIONES");

        finalScreen();
    }

    private void finalScreen() {
        TextView finalText = findViewById(R.id.finalText);
        TextView textoPalabra = findViewById(R.id.wordText);

        textoPalabra.setText(word);
        if (win) {
            finalText.setText("Enhorabona!");
            TextView posible = findViewById(R.id.textPossibleSol);
            TextView restric = findViewById(R.id.textRestrictions);
            restric.setEnabled(false);
        } else {
            finalText.setText("Oh oh oh oh...");
            modificarStrings();
        }
        Thread thread = new Thread ( new Runnable () {
            @Override
            public void run () {
                try {
                    TextView definicion = findViewById(R.id.textoDefPalabra);
                    definicion.setMovementMethod(new ScrollingMovementMethod());
                    definicion.setText(Html.fromHtml(AgafaHTML().toString()));

                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
    private void modificarStrings() {
        // Dar formato al texto (negrita)
        SpannableString ss = new SpannableString(posiblesSoluciones);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Poner texto
        TextView posible = findViewById(R.id.textPossibleSol);
        posible.setMovementMethod(new ScrollingMovementMethod());
        posible.setText(ss);

        /*SpannableString ss2 = new SpannableString(restricciones);
        StyleSpan boldSpan2 = new StyleSpan(Typeface.BOLD);
        ss2.setSpan(boldSpan2, 0, restricciones.indexOf(":"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Poner texto
        TextView restric = findViewById(R.id.textRestrictions);
        restric.setMovementMethod(new ScrollingMovementMethod());
        restric.setText(ss2);*/

    }

    private String AgafaHTML() {
        String direccion = "https://www.vilaweb.cat/paraulogic/?diec=" + word;

        try {
            // Enlace a internet
            URL definicion = new URL(direccion);
            BufferedReader in = new BufferedReader(new InputStreamReader(definicion.openStream()));
            StringBuffer texto = new StringBuffer();
            String line = in.readLine();
            while(line != null) {
                texto.append(line);
                line = in.readLine();
            }
            if (texto.toString().equals("[]")) {
                //in.close();
                return "Without definition";
            } else {
                JSONObject jObject = new JSONObject(texto.toString());
                String def = jObject.getString("d");
                //in.close();
                return def;
            }
        } catch (Exception e) {
            return "Link not found";
        }
    }

}