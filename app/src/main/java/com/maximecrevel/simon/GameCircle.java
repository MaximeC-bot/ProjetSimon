package com.maximecrevel.simon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GameCircle extends AppCompatActivity {

    private int nb_bloc_start;
    private int nb_bloc_4_win;
    private int default_life;
    private double poids_du_mode;
    private double score;
    private boolean chrono;
    private int lvl;

    //@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int numButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_game);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // On affecte nos variables
            nb_bloc_start = extras.getInt("nb_bloc_start");
            nb_bloc_4_win = extras.getInt("nb_bloc_4_win");
            default_life = extras.getInt("default_life",2);
            poids_du_mode = extras.getDouble("poids_du_mode");
            lvl = extras.getInt("lvl",1);
            score = extras.getDouble("score",0);
            chrono = extras.getBoolean("chrono",false);
        }

        if(chrono)
            findViewById(R.id.timer).setVisibility(View.VISIBLE); //rend le chrono visible

        numButton = lvl + 3;

        //Liste des couleurs pour les boutons
        final int[] arrayColor = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,Color.CYAN,Color.MAGENTA,Color.rgb(255,102,204),Color.rgb(102,51,51),Color.rgb(51,51,0),Color.rgb(102,153,153)};
        List<Button> buttons = new ArrayList<>();

        final FrameLayout main = findViewById(R.id.main);

        // Gérer l'affichage selon la taille de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int size;
        if(height<=width){
            size = height;
        }else{
            size =width;
        }



        for(int i = 0; i < numButton; i++)
        {
            /* Permet de créer des Textviews pour les placer */
            Button button = new Button(this);
            buttons.add(button);

            // Permet de centrer le texte
            button.setText(getString(R.string.couleur)+ i);
            button.setGravity(Gravity.CENTER);
            button.setBackgroundColor(arrayColor[i]);

            // Permet de standardiser la taille d'affichage
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)(size*0.30),(int) (size*0.15));

            // Permet de centrer l'affichage
            lp.gravity = Gravity.CENTER;

            // Définit la mise en page
            button.setLayoutParams(lp);

            // Permet de calculer les angles pour positionner le jeu
            float angleDeg = i * 360.0f / numButton - 90.0f;
            float angleRad = (float)(angleDeg * Math.PI / 180.0f);

            // Force le positionnement du jeu à partir des angles calculés
            button.setTranslationX((int)(size*0.3 * (float)Math.cos(angleRad)));
            button.setTranslationY((int)(size*0.3   * (float)Math.sin(angleRad)));

            // Rotation
            button.setRotation(angleDeg + 90.0f);
            main.addView(button);
        }

        //Affichage des infos en cours de partie
        TextView lbl_score = findViewById(R.id.lbl_score);
        TextView lbl_life = findViewById(R.id.lbl_life);
        Button btn_start = findViewById(R.id.btn_start);
        TextView lbl_round = findViewById(R.id.lbl_round);
        TextView lbl_timer = findViewById(R.id.timer);

        TextView lbl_lvl = findViewById(R.id.lbl_lvl);
        lbl_lvl.setText(getString(R.string.level) + lvl);

        Log.e("score", String.valueOf(score));

        final GameLib game = new GameLib(nb_bloc_start,nb_bloc_4_win, default_life, poids_du_mode, buttons, arrayColor, lvl, lbl_score, lbl_life, btn_start,  lbl_round,score,chrono,lbl_timer);



        // Prévenir timeouts
        Thread thread = new Thread(){
            public void run(){
                do {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(!game.getEnd() );

                if(game.getWin()){
                    //En cas de victoire passer au niveau suivant
                    final Intent startGame = getIntent();
                    startGame.putExtra("lvl",lvl+1);
                    startGame.putExtra("score", game.getScore());
                    startActivityForResult(startGame,lvl+1);
                    Log.i("scorend", String.valueOf(game.getScore()));
                }else{
                    // Quitter la partie
                    Intent intent = new Intent();
                    intent.putExtra("exit", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }
        };

        thread.start();


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == lvl+1) {
            if(resultCode == RESULT_OK) {
                boolean exit = data.getExtras().getBoolean("exit");
                if(exit){
                    Intent intent = new Intent();
                    intent.putExtra("exit", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        }
    }

    // Sortir du jeu
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("exit", true);
        setResult(RESULT_OK, intent);
        finish();

    }




}
