package com.worldskills.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {
    public static final String DIFICULTAD="DIFICULTAD";
    public static final String TIEMPO= "TIEMPO";
    public static final String MODO = "MODO";
    public static final String PLAYER1 = "PLAYER1";
    public static final String PLAYER2= "PLAYER2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void partida(View view) {
        Intent intent= new Intent(Home.this, Partida.class);
        startActivity(intent);
    }
}
