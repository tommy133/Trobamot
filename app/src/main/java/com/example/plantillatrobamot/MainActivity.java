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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    // Variables de lògica del joc
    private int lengthWord = 5;
    private int maxTry = 6;
    private String guess = "CABLE";

    private int highlightedRow = 0;
    private int highlightedColumn = 0;
    private final int textViewSize = 150;

    // Variables de construcció de la interfície
    public static String grayColor = "#D9E1E8";
    private int widthDisplay;
    private int heightDisplay;
    private UnsortedArrayMapping letters;
    private  BSTSet treeSet;

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
        try {
            iniciarDiccionari();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
    }

    private void crearInterficie() {
        crearGraella();
        crearTeclat();
        crearNombreSol();
    }

    private void crearGraella() {
        ConstraintLayout constraintLayout = findViewById(R.id.layout);

        // Definir les característiques del "pinzell"
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(3, Color.parseColor(grayColor));

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
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(30);
                // Afegir el TextView al layout
                constraintLayout.addView(textView);
            }
        }

    }

    private void crearNombreSol(){
        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        TextView textView = new TextView(this);
        textView.setId(0);
        textView.setWidth(textViewSize*2);
        textView.setHeight(textViewSize);
        textView.setX((widthDisplay/3 - textViewSize/2)+textViewSize);
        textView.setY((heightDisplay/2 - textViewSize/2)+textViewSize);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        //textView.setTextSize(30);
        textView.setText("Hi ha 5058 solucions disponibles");
        // Afegir el TextView al layout
        constraintLayout.addView(textView);
    }

    private void crearTeclat() {
        initAlphabet();
        setPositionsGuessLetters();
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
        int buttonKeyboardWidth = 100;
        int buttonKeyboardHeight = 100;
        ConstraintLayout.LayoutParams paramsControlBtn = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        paramsControlBtn.height = buttonHeight;
        paramsControlBtn.width = buttonWidth;
        buttonEsborrar.setLayoutParams(paramsControlBtn);
        buttonEsborrar.setY(heightDisplay -400 - buttonHeight);
        buttonEsborrar.setX(widthDisplay/2 -100 - buttonWidth/2);

        buttonEnviar.setLayoutParams(paramsControlBtn);
        buttonEnviar.setY(heightDisplay -400 - buttonHeight);
        buttonEnviar.setX(widthDisplay/2 -50 + buttonWidth/2);
        // Afegir el botó al layout
        constraintLayout.addView(buttonEsborrar);
        constraintLayout.addView(buttonEnviar);


        // Afegir la funcionalitat al botó
        buttonEsborrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                highlightedColumn--;
                if (highlightedColumn < 0) {
                    highlightedColumn = lengthWord;
                    highlightedRow--;
                }
                String id = highlightedRow + "" +highlightedColumn ;
                TextView textView = findViewById(Integer.valueOf(id).intValue());
                if (textView!=null){
                    String letter = (String) textView.getText();
                    textView.setText("");

                    //delete position in mapping
                    UnsortedLinkedListSet set = (UnsortedLinkedListSet) letters.get(letter);
                    set.remove(Integer.valueOf(id).intValue());
                    letters.put(letter, set);
                }

            }
        });
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isLongOk()){
                    System.out.println("ERROR TE FALTEN LLETRES TT");
                } else if (isParaula()) {
                    //FI DE JOC
                } else if (isValid()){
                    System.out.println("HEM TROBAT PARAULA VÁLIDA");
                    descobrirPistes();
                } else {
                    System.out.println("PARAULA NO VALIDA");
                }
            }
        });

        Iterator<UnsortedArrayMapping.Pair> it = letters.iterator();
        int row = 1;
        int col = 0;
        while (it.hasNext()) {
            UnsortedArrayMapping.Pair p =  it.next();
            String l = (String) p.getKey();
            Button button = new Button(this);
            ConstraintLayout.LayoutParams paramsKeyboardBtn = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            paramsKeyboardBtn.height = buttonKeyboardHeight;
            paramsKeyboardBtn.width = buttonKeyboardWidth;
            button.setText(l);
            button.setLayoutParams(paramsKeyboardBtn);
            button.setX(col * buttonKeyboardWidth);
            button.setY(row * buttonKeyboardHeight);
            constraintLayout.addView(button);

            col++;
            if (col >= 10) {
                col = 0;
                row++;
            }

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String text = ((Button) v).getText().toString();
                    String id = highlightedRow + "" +highlightedColumn ;

                    TextView textView = findViewById(Integer.valueOf(id).intValue());
                    textView.setText(text);

                    highlightedColumn++;
                    if (highlightedColumn >= lengthWord) {
                        highlightedColumn = 0;
                        highlightedRow++;
                    }
                }
            });
        }

    }

    public void iniciarDiccionari() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.paraules) ;
        BufferedReader r = new BufferedReader (new InputStreamReader(is)) ;
        treeSet= new BSTSet<String>();
        String s=r.readLine();
        String processed;
        while(s != null){
            processed = s.split(";")[1];
            if (processed.length() == lengthWord){
                treeSet.add(processed);
            }

            s=r.readLine();

        }

        r.close();
    }
    //comprova longitud de la paraula enviada
    private boolean isLongOk(){
        return highlightedColumn==0 & highlightedRow > 0;
    }

    private boolean isParaula(){return false;}
    private boolean isValid(){
        String input = getWordSent().toLowerCase();
        return treeSet.contains(input);
    }
    private void descobrirPistes(){
        String letter="";
        int row = highlightedRow - 1;
        for (int i=0; i < lengthWord; i++){
            String id = row + "" +i ;
            TextView textView = findViewById(Integer.valueOf(id).intValue());
            letter = ""+textView.getText();
            UnsortedLinkedListSet set = (UnsortedLinkedListSet) letters.get(letter);
            if (set.isEmpty()){
                System.out.println("LA LLETRA "+letter+" NO TÉ POSICIÓ ASSOCIADA");
                textView.setBackgroundColor(Color.RED);
            } else if (set.contains(i+1)){
                System.out.println("LA LLETRA "+letter+" TÉ LA POSICIÓ ASSOCIADA "+i);
                textView.setBackgroundColor(Color.GREEN);
            } else {
                System.out.println("LA LLETRA ES TROBA CONTINGUDA A LA PARAULA A ENDEVINAR");
                textView.setBackgroundColor(Color.YELLOW);
            }
        }
    }
    private String getWordSent(){ //suposam que té la llargàriar adecuada
        String s="";
        int row = highlightedRow - 1;
        for (int i=0; i < lengthWord; i++){
            String id = row + "" +i ;
            TextView textView = findViewById(Integer.valueOf(id).intValue());
            s += textView.getText();
        }
        return s;
    }


    private void initAlphabet(){
        letters = new UnsortedArrayMapping<String, UnsortedLinkedListSet<Integer>>(26);

        for (int i=65; i<65+26;i++){
            char l = (char) i;
            letters.put(Character.toString(l), new UnsortedLinkedListSet<Integer>());
        }
    }
    //retorna la llista enllaçada amb les posicions de la lletra a endevinar o una llista buida
    private void setPositionsGuessLetters(){
        char [] guessLetters = guess.toCharArray();
        for (int i=0; i < guessLetters.length; i++){
            String letter = String.valueOf(guessLetters[i]);
            UnsortedLinkedListSet set = (UnsortedLinkedListSet) letters.get(letter);
            set.add(i+1);
            letters.put(letter, set);
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