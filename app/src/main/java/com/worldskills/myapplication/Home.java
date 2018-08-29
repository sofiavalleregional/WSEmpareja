package com.worldskills.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    public static final String DIFICULTAD="DIFICULTAD";
    public static final String TIEMPO= "TIEMPO";
    public static final String MODO = "MODO";
    public static final String PLAYER1 = "PLAYER1";
    public static final String PLAYER2= "PLAYER2";
    Animation aparecer, puntos;
    TextView player1, player2, t1,t2,t3,t4;
    Dialog settings, scores, game, names;
    private String nick1, nick2, puntajes[];
    private int player;
    private boolean modo, ingreso;
    RadioButton activado, desactivado;
    private long tiempo;
    LinearLayout dialogsetti, dialogscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        // Recibo los parametros del splash para verificar por una booleana que sea la primera vez que ingresa, de no ser asi el menu se mostrará
        // y ya tendra los nombres mostrados.
        Bundle bundle= getIntent().getExtras();

        if(bundle!=null){
            ingreso=bundle.getBoolean("ingreso", false);
            if(ingreso){
                ingresarNombres();
                player=1;
            }
        }
    }



    ///// Metodo para encontrar todos los componentes en el xml o inicarlos
    public void init (){
        /////////DIALOGOS //////////

        settings= new Dialog(this);
        settings.setContentView(R.layout.dialog_settings);
        settings.setCanceledOnTouchOutside(false);

        scores= new Dialog(this);
        scores.setContentView(R.layout.dialog_scores);
        scores.setCanceledOnTouchOutside(false);

        names= new Dialog(this);
        names.setContentView(R.layout.dialog_names);
        names.setCanceledOnTouchOutside(false);

        game= new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        game.setContentView(R.layout.dialog_juego);
        game.setCanceledOnTouchOutside(false);

        player1= findViewById(R.id.user1);
        player2= findViewById(R.id.user2);



        activado= settings.findViewById(R.id.ractivado);
        desactivado= settings.findViewById(R.id.rdesactivado);

        dialogscores= scores.findViewById(R.id.dialogscores);
        dialogsetti= scores.findViewById(R.id.dialogsett);

        puntos= AnimationUtils.loadAnimation(this, R.anim.puntajes);
        aparecer= AnimationUtils.loadAnimation(this, R.anim.aparecer);

        puntos.setFillAfter(true);
        aparecer.setFillAfter(true);
    }


/* Metodo para ingresar los nombres, se usa el player para usar el mismo dialog pero con ambos usuarios y asi guardarlos los dos. Luego de que ambos
digiten se cierra el dialogo y se muestra el menu*/
    private void ingresarNombres() {
        final EditText editText= names.findViewById(R.id.textnames);


        if(player==2){
            editText.setText("Jugador 2");
        }

        Button confirmar = names.findViewById(R.id.confirmar_nombres);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player==1){
                    nick1= editText.getText().toString();
                    player=2;
                    ingresarNombres();
                } else {
                    nick2= editText.getText().toString();
                    names.dismiss();
                    player2.setText(nick2);
                }
            }
        });

        names.show();
        player1.setText(nick1);

    }



    /*Metodo donde el usuario dara click al boton de partida, se mostrara el dialog de dificultad y enviara un parametro para que reciba parida
    con la cantidad de cartas que son. */
    public void partida(View view) {
        Button bfacil, bmedio, bdificil;
        bfacil= game.findViewById(R.id.easy);
        bmedio= game.findViewById(R.id.medio);
        bdificil= game.findViewById(R.id.hard);


        bfacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, Partida.class);
                intent.putExtra(DIFICULTAD, 8);
                startActivity(intent);
                game.dismiss();
                finish();
            }
        });

        bmedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, Partida.class);
                intent.putExtra(DIFICULTAD, 12);
                startActivity(intent);
                game.dismiss();
                finish();
            }
        });

        bdificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, Partida.class);
                intent.putExtra(DIFICULTAD, 16);
                startActivity(intent);
                game.dismiss();
                finish();
            }
        });

        game.show();
    }



// Metodo de acción de vista para mostrar el dialog del tiempo, el usuario puede configurar el tiempo y activarla partida
    public void settingsShow (View v){
        final EditText edittiempo= settings.findViewById(R.id.text_time);

        Button confirm = settings.findViewById(R.id.confirmar_time);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiempo= Long.parseLong(edittiempo.getText().toString());
                settings.dismiss();
            }
        });

        settings.show();
    }

// Metodo para darle accion a los radiosbotones y activen la partida con tiempo o sin tiempo
    public void onChecked(View v){
        boolean checked = ((RadioButton)v).isChecked();
        switch (v.getId()){
            case R.id.ractivado:
                if(checked) modo=true;
                break;
            case R.id.rdesactivado:
                if(checked) modo=false;
                break;
        }
    }


// Metodo para darle acción a la vista, se muestra el dialogo con los botones, cada boton tiene una acción donde muestra un resource y hace nuevamente
  //  la busqueda de la base de datos, se recarga y vuevlve a la animación
    public void scoresShow(View v){
        t1= scores.findViewById(R.id.text1);
        t2= scores.findViewById(R.id.texto2);
        t3= scores.findViewById(R.id.texto3);
        t4= scores.findViewById(R.id.texto4);
        final Button facil, medio, dificil, salir;
        salir= scores.findViewById(R.id.cancel);
        facil= scores.findViewById(R.id.scoreseasy);
        medio= scores.findViewById(R.id.scoresmedio);
        dificil= scores.findViewById(R.id.scoreshard);




         puntajes= new String[4];
         defaultScores();
         consultaDatos(8);
        organizar();


        facil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facil.setBackgroundResource(R.drawable.boton_amarillo);
                medio.setBackgroundResource(R.drawable.boton_selec);
                dificil.setBackgroundResource(R.drawable.boton_selec);

               defaultScores();
               consultaDatos(8);
                organizar();


            }
        });

        medio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facil.setBackgroundResource(R.drawable.boton_selec);
                medio.setBackgroundResource(R.drawable.boton_amarillo);
                dificil.setBackgroundResource(R.drawable.boton_selec);

              defaultScores();
              consultaDatos(12);
              organizar();
            }
        });


        dificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facil.setBackgroundResource(R.drawable.boton_selec);
                medio.setBackgroundResource(R.drawable.boton_selec);
                dificil.setBackgroundResource(R.drawable.boton_amarillo);

              defaultScores();
               consultaDatos(16);
                organizar();
            }
        });


        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores.dismiss();
            }
        });

        scores.show();
    }




    public void defaultScores(){
        for (int i=0; i<puntajes.length; i++){
            puntajes[i]= ".  Jugador 0 ";
        }
    }

    public void  consultaDatos(int dificultad){
        DataBase db = new DataBase(this);

        Cursor cursor= db.load(dificultad);
        int c=0;

        if(cursor==null){


        }else {
            if(cursor.moveToFirst()){
                do {
                    if(c==0){
                        puntajes[0]= (c+1)+ ".  " + cursor.getString(0) + "\n " + cursor.getInt(1);
                    } else  puntajes[c]= (c+1)+ ".  " + cursor.getString(0) + " " + cursor.getInt(1);

                    c++;
                } while (cursor.moveToNext());
            }
        }

    }


    public void organizar(){
        Toast.makeText(Home.this, "ENTRO", Toast.LENGTH_SHORT).show();
        t1.setText(puntajes[0]);
        t2.setText(puntajes[1]);
        t3.setText(puntajes[2]);
        t4.setText(puntajes[3]);

        t1.startAnimation(puntos);
        t2.startAnimation(puntos);
        t3.startAnimation(puntos);
        t4.startAnimation(puntos);

    }



}
