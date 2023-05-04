package com.example.plantillatrobamot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    // Variables de lògica del joc
    private int lengthWord = 5;
    private int maxTry = 6;

    // Variables de construcció de la interfície
    public static String grayColor = "#D9E1E8";
    private int widthDisplay;
    private int heightDisplay;
    private UnsortedArrayMapping letters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Object to store display information
        DisplayMetrics metrics = new DisplayMetrics();
        // Get display information
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthDisplay = metrics.widthPixels;
        heightDisplay = metrics.heightPixels;

        crearInterficie();
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
    }

    private void crearInterficie() {
        crearGraella();
        crearTeclat();
    }

    private void crearGraella() {
        ConstraintLayout constraintLayout = findViewById(R.id.layout);

        // Definir les característiques del "pinzell"
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(3, Color.parseColor(grayColor));

        int textViewSize = 150;

        for (int i=0; i < maxTry; i++){
            for (int j=0; j < lengthWord; j++){
                // Crear un TextView
                TextView textView = new TextView(this);
                textView.setBackground(gd);
                textView.setId(Integer.valueOf(i+""+j));
                textView.setWidth(textViewSize);
                textView.setHeight(textViewSize);
                // Posicionam el TextView
                textView.setX((widthDisplay/3 - textViewSize/2)+j*textViewSize);
                textView.setY((heightDisplay/2 - textViewSize/2)+i*textViewSize);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                textView.setTextColor(Color.RED);
                textView.setTextSize(30);
                // Afegir el TextView al layout
                constraintLayout.addView(textView);
            }
        }

    }

    private void crearTeclat() {
        initAlphabet();
        ConstraintLayout constraintLayout = findViewById(R.id.layout);

        // Botó esborrar
        Button buttonEsborrar = new Button(this);
        buttonEsborrar.setText("Esborrar");
        //Botó enviar
        Button buttonEnviar = new Button(this);
        buttonEnviar.setText("Enviar");
        // Posicionar el botó
        int buttonWidth = 250;
        int buttonHeight = 100;
        int buttonKeyboardWidth = 250;
        int buttonKeyboardHeight = 100;
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.height = buttonHeight;
        params.width = buttonWidth;
        buttonEsborrar.setLayoutParams(params);
        buttonEsborrar.setY(heightDisplay -400 - buttonHeight);
        buttonEsborrar.setX(widthDisplay/2 -100 - buttonWidth/2);

        buttonEnviar.setLayoutParams(params);
        buttonEnviar.setY(heightDisplay -400 - buttonHeight);
        buttonEnviar.setX(widthDisplay/2 -50 + buttonWidth/2);
        // Afegir el botó al layout
        constraintLayout.addView(buttonEsborrar);
        constraintLayout.addView(buttonEnviar);


        // Afegir la funcionalitat al botó
        buttonEsborrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Esborrar!");
            }
        });
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Enviar!");
            }
        });

        Iterator<String> it = letters.iterator();
        int row = 1;
        int col = 0;
        while (it.hasNext()) {
            String l = it.next();
            Button button = new Button(this);
            button.setText(l);
            button.setLayoutParams(params);
            button.setX(col * buttonKeyboardWidth);
            button.setY(row * buttonKeyboardHeight);
            constraintLayout.addView(button);

            col++;
            if (col >= 10) {  // Cambiar de fila después de 10 botones
                col = 0;
                row++;
            }
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String text = ((Button) v).getText().toString();
                    String id = 0 +""+ 1 ; //remove hardcoding
                    TextView textView = findViewById(Integer.valueOf(id).intValue());
                    textView.setText(text);
                    //link position in mapping
                }
            });
        }

    }


    private void initAlphabet(){
        letters = new UnsortedArrayMapping<String, UnsortedLinkedListSet<String>>(26);

        for (int i=65; i<65+26;i++){
            char l = (char) i;
            letters.put(Character.toString(l), new UnsortedLinkedListSet<String>());
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE  // no posar amb notch
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}