package com.maximecrevel.simon;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;
import java.util.Random;

public class GameLib{
    private int checkPosition =0;
    private int roundCounter = 0 ;
    private final int [] colors = new int[50];
    private int sequencePosition = 0;
    private final double def_score;
    private double score = 0;
    private boolean vivant = false;
    private int life;
    private boolean win =false;

    //Variables spécifiques au mode chrono
    private final boolean chrono;
    private long timerSec = 0;
    private final TextView lbl_timer;
    private CountDownTimer timer;

    private final TextView lbl_life;
    private final Button btn_start;
    private final TextView lbl_round;

    //Tableau des scores
    private final TextView score_value;

    private final TextView life_TextView;


    private boolean end = false;

    //Variables
    private final int nb_bloc_start;
    private final int nb_bloc_4_win;
    private final int lvl;

    private final double poids_du_mode;

    //Boutons de couleur
    private final List<Button> buttons;
    //Listes des couleurs affichés
    private final int[] arrayColor;




    @SuppressLint("SetTextI18n")
    GameLib(int nb_bloc_start, int nb_bloc_4_win, int default_life, double poids_du_mode, List<Button> buttons, int[] arrayColor, int lvl, TextView lbl_score, TextView lbl_life, Button btn_start, TextView lbl_round, double score, boolean chrono,TextView lbl_timer) {

        life = default_life;
        this.nb_bloc_start = nb_bloc_start;
        this.nb_bloc_4_win = nb_bloc_4_win;

        this.poids_du_mode = poids_du_mode;
        this.buttons = buttons;
        this.arrayColor = arrayColor;

        this.lvl = lvl;


        this.lbl_life = lbl_life ;
        this.btn_start = btn_start;
        this.lbl_round = lbl_round;


        this.def_score = score;

        this.chrono = chrono;
        this.lbl_timer = lbl_timer;

        checkSiButtonExist();


        //Définir score
        score_value = lbl_score;
        score_value.setText("SCORE:" + score);

        //Définir les vies
        life_TextView = lbl_life;


        btn_start.setVisibility(View.INVISIBLE);



        //Gérer l'activation des boutons
        int i = 0;
        //boucle sur tout les boutons
        for (final Button myButton : buttons) {
            final int  myColor = arrayColor[i];
            myButton.setBackgroundColor(myColor);
            myButton.setAlpha(0.5f);
            i++;
        }

        //Activation du bouton start
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                newSequence();

            }

        });

        newGame();


        //Gérer l'activation des boutons
        i = 0;
        //boucle sur tous les boutons
        for (final Button myButton : buttons) {
            final int  myColor = arrayColor[i];

            //Pour gérer les clics
            myButton.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {

                    colorCheck(myColor);    //Vérifie la couleur sur laquelle on a cliqué
                    colorHighlight(myButton);   //Allume les boutons
                }

            });
            i++;
        }

        Thread thread = new Thread(){
            public void run(){

            }
        };

        thread.start();


    }


    //Vérifie l'existence des boutons
    private void checkSiButtonExist() {
        for (Button button : buttons) {
            String checking = button.getText().toString();
            Log.i("checkfor :", checking);
        }
    }

    //créer la séquence du simon
    private void newSequence() {
        // En cas de victoire
        if (vivant && roundCounter == nb_bloc_4_win) {
            win = true;
            message(btn_start, "You WIN");
            end();

        }else {

            Random rd = new Random();

            int[] possibleColors = arrayColor;

            //Préparation du 1er tour
            if (roundCounter == 0) {
                for (int i = 0; i < nb_bloc_start; i++) {
                    colors[roundCounter] = possibleColors[rd.nextInt(buttons.size())];
                    roundCounter++;
                }
            } else{
                //créer une nouvelle couleur et l'ajoute au tableau au numéro du round
                colors[roundCounter] = possibleColors[rd.nextInt(buttons.size())];


                roundCounter++;
            }

            //Gère affichage du round
            lbl_round.setText("ROUND: " + (roundCounter));


            //Affiche la séquence
            setSequence();

            vivant =true;

            checkPosition =0;
        }




    }




    //Affiche la sequence sur le bouton
    private void setSequence(){

        final View background= btn_start;

        showSequence(background);




        //set le boutton à gris et active les autres boutons
        background.postDelayed(new Runnable() {

            @Override

            public void run() {

                updateBackground(Color.GRAY);

                background.setAlpha(1f);


                for(Button myButton: buttons){
                    myButton.setEnabled(true);
                }

            }

        },(roundCounter +1)*500);

        if(chrono) {
            //Spécifique au mode chrono
            Log.i("Chrono :", "on");
            timerSec = roundCounter*2*1000+1000;
            startTimer();

        }

        //Désactive le bouton start
        btn_start.setEnabled(false);

    }

    //Démarre le chrono
    private void startTimer(){

        timer = new CountDownTimer(timerSec,100){
            @Override
            public void onTick(long millisUntilFinished) {
                timerSec = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                life--;
                //sinon mauvaise couleur vivant = faux (on meurt)
                life_TextView.setText("life :" + life);
                if (life <= 0)
                    vivant = false;
                if (life > 0)
                    newGame();
                isDead();
            }
        }.start();
    }
    //Met le chrono en pause
    private void pauseTimer(){
        timer.cancel();
    }

    private void updateTimer() {
        lbl_timer.setText(String.valueOf(timerSec).substring(0,String.valueOf(timerSec).length()-2));

    }


    //change le fond du bouton start
    private void updateBackground(int color){

         btn_start.setBackgroundColor(color);

    }

    //Début du jeu
    private void newGame() {
        //Fond du bouton start en gris
        Button startButton = btn_start;
        startButton.setBackgroundColor(Color.GRAY);

        //score et round remis à zéro
        roundCounter = 0;
        score = def_score;


        //Affiche le score
        score_value.setText("SCORE: " + score);

        //Affiche les vies
        life_TextView.setText("Life :" + life);

        //active le bouton start
        startButton.setEnabled(true);
        startButton.setVisibility(View.VISIBLE);

        //Affiche le round en cours
        lbl_round.setText("ROUND: " + roundCounter);

    }


    //Vérification de la couleur cliqué
    private void colorCheck(int color) {

        if (vivant) {
            //Si la couleur choisie est bonne
            if (color == colors[checkPosition]) {
                //bonne couleur : position+1. Ajout de point au score et affiche le score
                checkPosition++;
                // score += 10;
                // score_value.setText("SCORE: " + score);

            } else {
                if(chrono)
                    pauseTimer();
                //on perd une vie
                life--;
                life_TextView.setText("life :" + life);
                if (life <= 0)
                    vivant = false; //fin de partie
                if (life > 0)
                    newGame();  //on recommence
            }

            isDead();


        }
    }
    private void isDead(){
            //si pas de mort ajout des points et désactivation des boutons
            if (checkPosition == roundCounter && vivant) {
                if(chrono)
                    pauseTimer();
                //Ajout au score 2* le round
                // Log.v("SCORE : " , lvl +"*"+poids_du_mode );
                score += lvl * poids_du_mode;
                //actualise le score
                score_value.setText("SCORE: " + score);


                newSequence();

                //Désactive les boutons
                for (Button myButton : buttons)
                    myButton.setEnabled(false);
            }

            // Si le joueur a perdu:

        if(!vivant && roundCounter >0){
            if(chrono)
                pauseTimer();
            message(btn_start,"You lose");

            end();

        }

    }


    private void end(){
           end =  true;
    }


    //Envoie le message de fin de partie avec le bouton start
    private void message(final Button startButton,final String msg){
        startButton.postDelayed(new Runnable() {

            @Override

            public void run() {

                //Rend les boutons invisibles
                for(Button myButton: buttons)
                    myButton.setVisibility(View.INVISIBLE);


                //Efface le lbl round
                lbl_round.setVisibility(View.INVISIBLE);

                //Efface le lbl Life
                lbl_life.setVisibility(View.INVISIBLE);

                //Affiche message de défaite sur le bouton "start" :'(
                startButton.setBackgroundColor(Color.BLACK);
                startButton.setTextColor(Color.WHITE);
                startButton.setText(msg);

            }

        },50);

        startButton.postDelayed(new Runnable() {

            @Override

            public void run() {

                //reset le bouton start
                startButton.setText("Start");
                startButton.setBackgroundColor(Color.GRAY);
                startButton.setTextColor(Color.BLACK);


                //Affiche le round
                TextView t= lbl_round;
                t.setText("ROUND: 0");

                //Efface le lbl Life
                lbl_life.setVisibility(View.VISIBLE);

                //Rend le bouton visible
                for(Button myButton: buttons)
                    myButton.setVisibility(View.VISIBLE);

                t.setVisibility(View.VISIBLE);

            }

        },3500);


        //reset l'affichage du score
        score_value.setText("SCORE: "+ score);

        //Active le bouton start
        startButton.setEnabled(true);

    }

    //Highlight start button
    private void showSequence(final View background){

        sequencePosition =0;
        //Affiche chaque couleur
        for(int i = 0; i< roundCounter; i++) {

            //Degrade de couleurs
            if (colors[i] != 0) {

                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {
                        updateBackground(colors[sequencePosition]);

                        background.setAlpha(0.2f);

                    }

                }, (i+1) * 500);


                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        background.setAlpha(0.5f);

                    }

                },(500*(i+1))+100);

                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        background.setAlpha(0.9f);

                    }

                },(500*(i+1))+150);


                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        background.setAlpha(1f);

                    }

                },(500*(i+1))+250);

                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        background.setAlpha(0.9f);

                    }

                },(500*(i+1))+350);

                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        background.setAlpha(0.5f);

                    }

                },(500*(i+1))+400);


                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {

                        background.setAlpha(0.2f);


                        sequencePosition++;
                    }

                },(500*(i+1))+450);


            }
        }
    }

    private void colorHighlight(Button button) {

        final Button b = button;

        b.postDelayed(new Runnable() {

            @Override

            public void run() {
                b.setAlpha(0.5f);
            }

        }, 0);

        b.postDelayed(new Runnable() {

            @Override

            public void run() {
                b.setAlpha(0.75f);
            }

        }, 50);

        b.postDelayed(new Runnable() {

            @Override

            public void run() {
                b.setAlpha(1f);
            }

        }, 100);

        b.postDelayed(new Runnable() {

            @Override

            public void run() {
                b.setAlpha(0.75f);
            }

        }, 250);

        b.postDelayed(new Runnable() {

            @Override

            public void run() {
                b.setAlpha(0.5f);
            }

        }, 300);
    }

    public boolean getWin(){ return win; }
    public boolean getEnd(){ return end; }
    public double getScore(){ return score; }

}