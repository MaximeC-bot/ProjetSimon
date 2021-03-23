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

        //Stats du mode facile
        final Intent startGame1 = new Intent(this, GameCircle.class);
        startGame1.putExtra("nb_bloc_start",1);
        startGame1.putExtra("nb_bloc_4_win",10);
        startGame1.putExtra("poids_du_mode",(double) 1);
        //Ecran du mode facile
        Button facile = findViewById(R.id.facile);
        facile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame1);
            }
        });




        //Stats du mode difficile
        final Intent startGame2 = new Intent(this, GameCircle.class);
        startGame2.putExtra("nb_bloc_start",3);
        startGame2.putExtra("nb_bloc_4_win",15);
        startGame2.putExtra("poids_du_mode", 1.5);

        //Ecran du mode difficile
        Button difficile = findViewById(R.id.difficile);
        difficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame2);
            }
        });


        //Stats du mode expert
        final Intent startGame3 = new Intent(this, GameCircle.class);
        startGame3.putExtra("nb_bloc_start",5);
        startGame3.putExtra("nb_bloc_4_win",20);
        startGame3.putExtra("default_life",3);
        startGame3.putExtra("poids_du_mode",(double) 3);

        //Ecran du mode expert
        Button expert = findViewById(R.id.expert);
        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame3);
            }
        });



        //Stats du mode chrono + activation du chrono
        final Intent startGame4 = new Intent(this, GameCircle.class);
        startGame4.putExtra("nb_bloc_start",1);
        startGame4.putExtra("nb_bloc_4_win",20);
        startGame4.putExtra("default_life",3);
        startGame4.putExtra("poids_du_mode", 2.0);
        startGame4.putExtra("chrono",true);

        //Ecran du mode chrono
        Button chrono = findViewById(R.id.chrono);
        chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame4);
            }
        });

    }
}



