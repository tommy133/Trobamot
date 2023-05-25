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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    // Variables de lògica del joc
    private int lengthWord = 5;
    private int maxTry = 6;

    private int id_nsol = Integer.valueOf(maxTry+""+lengthWord);
    private String guess = "BLANA";

    private int highlightedRow = 0;
    private int highlightedColumn = 0;
    private final int textViewSize = 150;

    // Variables de construcció de la interfície
    public static String grayColor = "#D9E1E8";
    private int widthDisplay;
    private int heightDisplay;
    private UnsortedArrayMapping letters, restrictions;
    private HashMap<String, String> wordMap ; //clau paraula sense accents, valor paraula amb accents
    private HashSet<String> possibleSol;

    private class UserLetter {
        boolean isContained;
        UnsortedLinkedListSet<Integer> positions;

        public UserLetter(boolean isContained, UnsortedLinkedListSet<Integer> positions) {
            this.isContained = isContained;
            this.positions = positions;
        }
    }

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

        try {
            iniciarDiccionari();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                textView.setX((widthDisplay/3 - textViewSize/2 -130)+j*(textViewSize+10));
                textView.setY((heightDisplay/2 - 6*textViewSize)+i*(textViewSize+10));
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(30);
                // Afegir el TextView al layout
                constraintLayout.addView(textView);
            }
        }
        /*
        //Highlighteam sa primera casella
        String id = highlightedRow + "" +highlightedColumn ;
        TextView textView = findViewById(Integer.valueOf(id).intValue());
        GradientDrawable gdFirst = (GradientDrawable) textView.getBackground();
        gdFirst.setStroke(3, Color.YELLOW);
        System.out.println("TAMARE");*/
    }

    private void crearNombreSol(){
        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        TextView textView = new TextView(this);
        textView.setId(id_nsol);
        textView.setWidth(textViewSize*5);
        textView.setHeight(textViewSize);
        textView.setX((widthDisplay/3 - textViewSize/2) -100);
        textView.setY((heightDisplay/2 - textViewSize/2) + textViewSize);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(20);

        //textView.setTextSize(30);
        textView.setText("Hi ha "+possibleSol.size()+" solucions disponibles");
        // Afegir el TextView al layout
        constraintLayout.addView(textView);
    }
    private void updateViewNombreSol(){
        TextView textView = findViewById(id_nsol);
        textView.setText("Hi ha "+possibleSol.size()+" solucions disponibles");
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
        int buttonKeyboardWidth = 120;
        int buttonKeyboardHeight = 120;
        ConstraintLayout.LayoutParams paramsControlBtn = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        paramsControlBtn.height = buttonHeight;
        paramsControlBtn.width = buttonWidth;
        buttonEsborrar.setLayoutParams(paramsControlBtn);
        buttonEsborrar.setY(heightDisplay -350 - buttonHeight);
        buttonEsborrar.setX(widthDisplay/2 -100 - buttonWidth/2);

        buttonEnviar.setLayoutParams(paramsControlBtn);
        buttonEnviar.setY(heightDisplay -350 - buttonHeight);
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
                    textView.setText("");
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
                    discoverRestrictions();
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
            button.setY(row * buttonKeyboardHeight +1600);
            constraintLayout.addView(button);

            col++;
            if (col >= 9) {
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
        wordMap = new HashMap<>();
        possibleSol = new HashSet<>();
        String s=r.readLine();
        String withoutAccents;
        String withAccents;
        while(s != null){
            withAccents = s.split(";")[0];
            withoutAccents = s.split(";")[1];
            if (withoutAccents.length() == lengthWord){
                wordMap.put(withoutAccents, withAccents);
                possibleSol.add(withAccents);
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
        return wordMap.containsKey(input);
    }
    private void discoverRestrictions(){
        restrictions = new UnsortedArrayMapping<String, UserLetter>(lengthWord);

        String letter="";
        int row = highlightedRow - 1;
        for (int i=0; i < lengthWord; i++){
            String id = row + "" +i ;
            TextView textView = findViewById(Integer.valueOf(id).intValue());
            letter = ""+textView.getText();
            UnsortedLinkedListSet set = (UnsortedLinkedListSet) letters.get(letter);
            if (set.isEmpty()){
                restrictions.put(letter, new UserLetter(false, set));
                updatePossibleSol(letter, false, i);
                System.out.println("LA LLETRA "+letter+" NO ES TROBA CONTINGUDA A LA PARAULA A ENDEVINAR");
                textView.setBackgroundColor(Color.RED);
            } else if (set.contains(i+1)){
                restrictions.put(letter, new UserLetter(true, set));

                updatePossibleSol(letter, true, i);

                System.out.println("LA LLETRA "+letter+" TÉ LA POSICIÓ ASSOCIADA "+i);
                textView.setBackgroundColor(Color.GREEN);

            } else {
                UnsortedLinkedListSet setvoid = new UnsortedLinkedListSet<Integer>();
                setvoid.add(-1);
                restrictions.put(letter, new UserLetter(true,setvoid));

                updatePossibleSol(letter, true, -1);
                System.out.println("LA LLETRA ES TROBA CONTINGUDA A LA PARAULA A ENDEVINAR");
                textView.setBackgroundColor(Color.YELLOW);
            }
        }
        updateViewNombreSol();
    }
    /*
    private void updatePossibleSolBasedRestrictions(){
        Iterator<UnsortedArrayMapping.Pair> it = restrictions.iterator();

        while (it.hasNext()){
            UnsortedArrayMapping.Pair p =  it.next();
            String letter = (String) p.getKey();
            UserLetter userLetter = (UserLetter) p.getValue();
            updatePossibleSol(letter, userLetter.isContained, -1);
        }

        updateViewNombreSol();
    }*/

    private void updatePossibleSol(String letter, boolean isContained, int pos){
        Iterator<String> it = possibleSol.iterator();

            while (it.hasNext()) {
                String word = it.next();
                if (pos != -1) { //si la lletra introduida per l'usuari té posicions
                    //si aquesta lletra es troba continguda a la paraula a adivinar i no coincideix amb
                    // la lletra i-èsima de la paraula de posibles solucions
                    if (isContained && word.charAt(pos) != letter.toLowerCase().charAt(0)){
                        it.remove();
                    }
                    else if (!isContained && word.charAt(pos) == letter.toLowerCase().charAt(0)){
                        it.remove();
                    }
                } else  if (!word.contains(letter.toLowerCase())){
                    it.remove();
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
        letters = new UnsortedArrayMapping<String, UnsortedLinkedListSet<Integer>>(27);

        for (int i=65; i<65+26;i++){
            char l = (char) i;
            letters.put(Character.toString(l), new UnsortedLinkedListSet<Integer>());
        }
        letters.put(Character.toString('Ç'), new UnsortedLinkedListSet<Integer>());

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