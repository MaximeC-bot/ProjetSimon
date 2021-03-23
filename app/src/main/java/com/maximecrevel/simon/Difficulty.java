package com.maximecrevel.simon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Difficulty extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.difficulty);
        super.onCreate(savedInstanceState);

      /*  //get next view  for game
        final Intent startGame = new Intent(this, Game.class);

        //Change view on click facile
        Button facile = findViewById(R.id.facile);
        facile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame);
            }
        });*/
        //get next view  for game
        final Intent startGame1 = new Intent(this, GameCircle.class);
        startGame1.putExtra("nb_bloc_start",1);
        startGame1.putExtra("nb_bloc_4_win",7);
        startGame1.putExtra("poids_du_mode",(double) 1);
        //Change view on click difficile
        Button facile = findViewById(R.id.facile);
        facile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame1);
            }
        });




        //get next view  for game
        final Intent startGame2 = new Intent(this, GameCircle.class);
        startGame2.putExtra("nb_bloc_start",3);
        startGame2.putExtra("nb_bloc_4_win",10);
        startGame2.putExtra("poids_du_mode", 1.5);

        //Change view on click difficile
        Button difficile = findViewById(R.id.difficile);
        difficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame2);
            }
        });


        //get next view  for game
        final Intent startGame3 = new Intent(this, GameCircle.class);
        startGame3.putExtra("nb_bloc_start",4);
        startGame3.putExtra("nb_bloc_4_win",12);
        startGame3.putExtra("poids_du_mode",(double) 2);

        //Change view on click expert
        Button expert = findViewById(R.id.expert);
        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame3);
            }
        });



        //get next view  for game
        final Intent startGame4 = new Intent(this, GameCircle.class);
        startGame4.putExtra("nb_bloc_start",1);
        startGame4.putExtra("nb_bloc_4_win",8);
        startGame4.putExtra("default_life",3);
        startGame4.putExtra("poids_du_mode", 1.5);
        startGame4.putExtra("chrono",true);

        //Change view on click chrono
        Button chrono = findViewById(R.id.chrono);
        chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame4);
            }
        });

    }
}



