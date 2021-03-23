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
    private int secuencePosition = 0;
    private final double def_score;
    private double score = 0;
    private boolean vivant = false;
    private int life;
    private boolean win =false;

    //MODE CHRONO ONLY
    private final boolean chrono;
    private long timerSec = 0;
    private final TextView lbl_timer;
    private CountDownTimer timer;


    //Declaration of buttons:
   /* public Button btnBlue;
    public Button btnGreen;
    public Button btnYellow;
    public Button btnRed;
    private Button btnStart;
*/

    private final TextView lbl_life;
    private final Button btn_start;
   // private Button btn_new;
    private final TextView lbl_round;


    //Scoreboards / txt info
    private final TextView score_value;

    private final TextView life_TextView;


    private boolean end = false;




    //Need config
    private final int nb_bloc_start;
    private final int nb_bloc_4_win;
    private final int lvl;

    private final double poids_du_mode;

    //list of gaming buttons
    private final List<Button> buttons;
    //list of color in same order than gaming button
    private final int[] arrayColor;




    @SuppressLint("SetTextI18n")
    GameLib(int nb_bloc_start, int nb_bloc_4_win, int default_life, double poids_du_mode, List<Button> buttons, int[] arrayColor, int lvl, TextView lbl_score, TextView lbl_life, Button btn_start, TextView lbl_round, double score, boolean chrono,TextView lbl_timer) {
      //  super.onCreate(savedInstanceState);
       // setContentView(R.layout.game);

        life = default_life;
        this.nb_bloc_start = nb_bloc_start;
        this.nb_bloc_4_win = nb_bloc_4_win;

        this.poids_du_mode = poids_du_mode;
        this.buttons = buttons;
        this.arrayColor = arrayColor;

        this.lvl = lvl;


        this.lbl_life = lbl_life ;
        this.btn_start = btn_start;
       // this.btn_new = btn_new;
        this.lbl_round = lbl_round;


        this.def_score = score;

        this.chrono = chrono;
        this.lbl_timer = lbl_timer;
        //The value of the buttons and scoreboard is asigned by its id in the content_main.xml file
        //fetchButtons();

      //  buttons = Arrays.asList(btnBlue, btnGreen, btnYellow, btnRed);

        checkSiButtonExist();


        //Set score
        score_value = lbl_score;
        score_value.setText("SCORE:" + score);

        //Set life
        life_TextView = lbl_life;


        btn_start.setVisibility(View.INVISIBLE);



        //Manages button activation
        int i = 0;
        //loop sur tout les bouttons
        for (final Button myButton : buttons) {
            final int  myColor = arrayColor[i];
            myButton.setBackgroundColor(myColor);
            myButton.setAlpha(0.5f);
            i++;
        }

        //Manages start button activation
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                newSequence();

            }

        });

        //Manages new game button activation
        /*btn_new.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                newGame();
            }
        });*/

        newGame();


        //Manages button activation
        i = 0;
        //loop sur tout les bouttons
        for (final Button myButton : buttons) {
            final int  myColor = arrayColor[i];

            //set event onclick
            myButton.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {

                    colorCheck(myColor);
                    colorHighlight(myButton);
                }

            });
            i++;
        }

    /*    final int final_nb_bloc_4_win = nb_bloc_4_win;
        final Button final_btn_start = btn_start;





        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                // 4 the win :
                if (vivant && roundCounter == final_nb_bloc_4_win) {
                    final Button message =  final_btn_start;
                    win = true;
                    message(message, "You WIN");

                }else {
                    newSequence();
                }
            }

        });*/



        Thread thread = new Thread(){
            public void run(){

            }
        };

        thread.start();


    }





    //Verif si les button existe
    private void checkSiButtonExist() {
        for (Button button : buttons) {
            String checking = button.getText().toString();
            Log.i("checkfor :", checking);
        }
    }

    //creer la sequence du  simon
    private void newSequence() {
        // 4 the win :
        if (vivant && roundCounter == nb_bloc_4_win) {
            win = true;
            message(btn_start, "You WIN");
            end();

        }else {

            Random rd = new Random();

            int[] possibleColors = arrayColor;



            //Setup tour 1 avec le  nb_bloc_start
            if (roundCounter == 0) {
                for (int i = 0; i < nb_bloc_start; i++) {
                    colors[roundCounter] = possibleColors[rd.nextInt(buttons.size())];
                    roundCounter++;
                }
            } else{
                //creer une nouvelle couleurs est l ajoute au tableau au numero du round
                colors[roundCounter] = possibleColors[rd.nextInt(buttons.size())];


                roundCounter++;
            }

            //Gere affichage du round
            lbl_round.setText("ROUND: " + (roundCounter));


            //Affiche la séquence
            setSequence();

            vivant =true;

            checkPosition =0;
        }




    }




    //Affiche la sequence sur le boutton
    private void setSequence(){

        final View background= btn_start;

        showSequence(background);




        //set le boutton à gris et active les autres boutton
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
            Log.i("Chrono :", "on");
            timerSec = roundCounter*2*1000+1000;
            startTimer();

        }

        //Disable le button start
        btn_start.setEnabled(false);

    }

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
    private void pauseTimer(){
        timer.cancel();
    }

    private void updateTimer() {
        lbl_timer.setText(String.valueOf(timerSec).substring(0,String.valueOf(timerSec).length()-2));

    }


    //change le background du buttonStart
    private void updateBackground(int color){

         btn_start.setBackgroundColor(color);

    }



    //cette methode commence le jeux
    private void newGame() {
        //Set le background du start à gris
        Button startButton = btn_start;
        startButton.setBackgroundColor(Color.GRAY);

        //reset les score et round
        roundCounter = 0;
        score = def_score;


        //Affiche le score
        score_value.setText("SCORE: " + score);

        //Affiche les vie
        life_TextView.setText("Life :" + life);

        //active le btn start
        startButton.setEnabled(true);
        startButton.setVisibility(View.VISIBLE);

        //Affiche le round
        lbl_round.setText("ROUND: " + roundCounter);

    }


    //Verif si la couleur clicker est correct sinon affiche win/lose
    private void colorCheck(int color) {


        //verif si game a start et non mort
        if (vivant) {
            //Si la couleur bonne
            if (color == colors[checkPosition]) {
                //bonne couleur : possition+1. Ajout de point au score et affiche le score
                checkPosition++;
                // score += 10;
                // score_value.setText("SCORE: " + score);

            } else {
                if(chrono)
                    pauseTimer();
                //sinon mauvaise couleur vivant = faux (on meurt)
                life--;
                life_TextView.setText("life :" + life);
                if (life <= 0)
                    vivant = false;
                if (life > 0)
                    newGame();
            }

            isDead();


        }
    }
    private void isDead(){
            //si on est pas mort ajouts des points et désactivation des button
            if (checkPosition == roundCounter && vivant) {
                if(chrono)
                    pauseTimer();
                //Ajout au score 2* le round
                // Log.v("SCORE : " , lvl +"*"+poids_du_mode );
                score += lvl * poids_du_mode;
                //actualise le score
                score_value.setText("SCORE: " + score);


                newSequence();

                /*//Active le button start
                startButton.setEnabled(true);*/
                //Disable les button de "jeux"
                for (Button myButton : buttons)
                    myButton.setEnabled(false);
            }

            // Si mort :

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


    //Send des MSG fin de partie avec le boutton "start"
    private void message(final Button startButton,final String msg){
        startButton.postDelayed(new Runnable() {

            @Override

            public void run() {

                //Efface les buttons de "jeux"
                for(Button myButton: buttons)
                    myButton.setVisibility(View.INVISIBLE);


                //Efface le lbl round
                lbl_round.setVisibility(View.INVISIBLE);

                //Efface le lbl Life
                lbl_life.setVisibility(View.INVISIBLE);

                //Affiche you lose sur le button "start"
                startButton.setBackgroundColor(Color.BLACK);
                startButton.setTextColor(Color.WHITE);
                startButton.setText(msg);

            }

        },50);

        startButton.postDelayed(new Runnable() {

            @Override

            public void run() {

                //reset le button start
                startButton.setText("Start");
                startButton.setBackgroundColor(Color.GRAY);
                startButton.setTextColor(Color.BLACK);


                //Affiche le round
                TextView t= lbl_round;
                t.setText("ROUND: 0");

                //Efface le lbl Life
                lbl_life.setVisibility(View.VISIBLE);

                //Set le button de "jeux" sur visible
                for(Button myButton: buttons)
                    myButton.setVisibility(View.VISIBLE);

                t.setVisibility(View.VISIBLE);

            }

        },3500);


        //reset l'affichage du score
        score_value.setText("SCORE: "+ score);

        //Start new Game
       // newGame();

        //Active le button start
        startButton.setEnabled(true);

    }



    //Highlight start button
    private void showSequence(final View background){

        secuencePosition =0;
        //Affiche chaque couleur
        for(int i = 0; i< roundCounter; i++) {

            //Degrade de couleur
            if (colors[i] != 0) {

                background.postDelayed(new Runnable() {

                    @Override

                    public void run() {
                        updateBackground(colors[secuencePosition]);

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


                        secuencePosition++;
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


//extrat string