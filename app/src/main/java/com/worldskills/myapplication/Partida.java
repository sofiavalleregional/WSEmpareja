package com.worldskills.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.worldskills.myapplication.Modulos.AdapterCartas;
import com.worldskills.myapplication.Modulos.ItemCarta;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Partida extends AppCompatActivity {

    private final int TAPAR=R.drawable.fondo_tapar_carta;
    private final int DESTAPAR=android.R.color.transparent;

    private MediaPlayer win, lose;

    private GridView gridView;
    private AdapterCartas adapterCartas;
    private ArrayList<ItemCarta> cartas;

    private TextView viewJugador1, viewJugador2, viewPuntaje1, viewPuntaje2, viewTemporizador;
    private Chronometer viewChronometer;

    private View view1, view2;
    private int posicion1, posicion2;
    private boolean turno, clickCarta, temporizadorActivado;

    private int puntaje1, puntaje2, tiempoPartida, dificultad;
    private String nomJ1, nomJ2;

    private CountDownTimer timerCarta, timerPartida;

    private Animation animDestapar, animDesaparecer, animAparecer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);
        findViews();
        loadAnimations();

        Bundle datos=getIntent().getExtras();
        if (datos!=null){
            dificultad=datos.getInt(Home.DIFICULTAD);
        }
        dificultad=8;//borrarLuego

        puntaje1=0;
        puntaje2=0;
        tiempoPartida=0;
        clickCarta=true;

        win=MediaPlayer.create(this,R.raw.win);
        lose=MediaPlayer.create(this,R.raw.lose);

    }
    /*Metodo para encontrar las vistas que se encuentran en el layout que le
    pertenece a esta actividad e guardarlas para ser modificadas durante la partida*/
    public void findViews(){
        viewJugador1=findViewById(R.id.partida_jugador1);
        viewJugador2=findViewById(R.id.partida_jugador2);
        viewPuntaje1=findViewById(R.id.partida_puntaje1);
        viewPuntaje2=findViewById(R.id.partida_puntaje2);
        viewTemporizador=findViewById(R.id.partida_temporizador);
        viewChronometer=findViewById(R.id.partida_chronometer);
        gridView=findViewById(R.id.partida_gridview);
    }


    /*Metodo con el fin de guardar las animaciones en una objeto animation para ser cargadas
    * durante el juego cuando sea necesario*/
    public void loadAnimations(){
        animDestapar= AnimationUtils.loadAnimation(this,R.anim.destapar);
        animDestapar.setFillAfter(true);
        animDesaparecer=AnimationUtils.loadAnimation(this,R.anim.desaparecer);
        animDesaparecer.setFillAfter(true);
        animAparecer=AnimationUtils.loadAnimation(this,R.anim.aparecer);
        animAparecer.setFillAfter(true);
    }

    /*Metodo con el fin de cargar las cartas en la pantalla ya organizadas el azar*/
    public void cargarCartas(){
        try {
            cartas = new ArrayList<>();
            adapterCartas=new AdapterCartas(this, R.layout.item_carta, cartas);
            gridView.setAdapter(adapterCartas);
        }catch (Exception e){}

        organizaAzarCartas(dificultad/2);
        adapterCartas.notifyDataSetChanged();
        clickCartas();
    }



    /*Metodo con el fin de organizar las figuras de manera aleatoria y guardarlas al final el el ArrayList de ItemCarta*/
    public void organizaAzarCartas(int parejas){
        Toast.makeText(this, "si", Toast.LENGTH_SHORT).show();
        int[] numeros=new int[dificultad];
        for (int i=0; i<numeros.length; i++)numeros[i]=-1;

        int base=0;
        boolean comprueba;

        do{
            int position=new Random().nextInt(numeros.length);
            if (numeros[position]==-1){
                numeros[position]=base;
                base++;
                if (base==parejas)base=0;
            }

            comprueba=false;
            for (int i=0; i<numeros.length; i++)if (numeros[i]==-1)comprueba=true;


        }while (comprueba);

        for (int i=0; i<numeros.length;i++)cartas.add(new ItemCarta(numeros[i],TAPAR));
    }



    /*Metodo para que cada item (carta) en la pantalla haga su respectivo cambio cuando esta es precionada*/

    public void clickCartas(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (clickCarta){

                    if (view1!=null && view2!=null){
                        view1.clearAnimation();
                        view2.clearAnimation();
                    }

                    view1=view;
                    posicion1=position;

                    cartas.get(posicion1).setFondoTapar(DESTAPAR);
                    view1.startAnimation(animDestapar);

                    clickCarta=false;

                }else if (cartas.get(position).getFondoTapar()!=DESTAPAR){

                    view1.clearAnimation();
                    view2=view;
                    posicion2=position;

                    cartas.get(position).setFondoTapar(DESTAPAR);
                    view2.startAnimation(animDestapar);
                    clickCarta=true;
                    compruebaCartas();
                }
                adapterCartas.notifyDataSetChanged();
            }
        });
    }
    /*Al precionar las dos cartas este medoto se encargara de vefiricar si las cartas precionadas
    * con correctas o incorrectas*/
    public void compruebaCartas(){
        gridView.setOnItemClickListener(null);

        timerCarta=new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (cartas.get(posicion1).getNumero()==cartas.get(posicion2).getNumero()){

                    win.start();
                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                    view1.startAnimation(animDesaparecer);
                    view2.startAnimation(animDesaparecer);

                    if (turno) puntaje1+=100;
                    else puntaje2+=100;

                }else{
                    lose.start();

                    cartas.get(posicion1).setFondoTapar(TAPAR);
                    cartas.get(posicion2).setFondoTapar(TAPAR);
                    view1.startAnimation(animDestapar);
                    view2.startAnimation(animDestapar);

                    if (turno){
                        if (puntaje1!=0){
                            puntaje1-=2;
                        }
                        turno=false;
                    }else{
                        if (puntaje2!=0){
                            puntaje2-=2;
                        }

                        turno=true;
                    }

                }

                adapterCartas.notifyDataSetChanged();


                actualizaPantalla();

                if (compruebaFinalPartida())clickCartas();
                else finPartida();

            }
        }.start();
    }
    /*Metodo con el fin de actualizar todos los datos que van cambiardo durante el juego*/
    public void actualizaPantalla(){
        viewJugador1.setText(nomJ1);
        viewJugador2.setText(nomJ2);
        viewPuntaje1.setText(puntaje1+"");
        viewPuntaje2.setText(puntaje2+"");

        if (turno){
            viewJugador1.setBackgroundColor(getResources().getColor(R.color.negro_claro,null));
            viewPuntaje1.setBackgroundColor(getResources().getColor(R.color.negro_claro,null));

            viewJugador2.setBackgroundColor(getResources().getColor(R.color.gris,null));
            viewPuntaje2.setBackgroundColor(getResources().getColor(R.color.gris,null));
        }else{
            viewJugador2.setBackgroundColor(getResources().getColor(R.color.negro_claro,null));
            viewPuntaje2.setBackgroundColor(getResources().getColor(R.color.negro_claro,null));

            viewPuntaje1.setBackgroundColor(getResources().getColor(R.color.gris,null));
            viewJugador1.setBackgroundColor(getResources().getColor(R.color.gris,null));

        }
    }
    /*Metodo con el fin de veficicar si todas las cartas ya han desaparecido de la pantalla*/
    public boolean compruebaFinalPartida(){
        for (int i=0; i<cartas.size(); i++)if (cartas.get(i).getFondoTapar()==TAPAR) return true;
        return false;
    }


    /*Metodo para finalizar la partida cuando ya se haya verificado que no haya ninguna carta*/
    public void finPartida(){

        try{
            timerPartida.cancel();
        }catch (Exception e){}
        try {
            timerCarta.cancel();
        }catch (Exception e){}

        if (!temporizadorActivado){
            tiempoPartida=(int)(viewChronometer.getBase()-SystemClock.elapsedRealtime())/-1000;
            viewChronometer.stop();
        }

        abreDialogFinal();
        guardaDatos();
    }

    /*Metodo que se llamara cuando en los ajustes del inicio de activa el temporizador*/
    public void tiempoPartida(long tiempoP){
        timerPartida=new CountDownTimer(tiempoP,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time=(int)(millisUntilFinished/1000);

                if (time<9)viewTemporizador.setText("0"+time);
                else viewTemporizador.setText(""+time);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }




    /*Metodo para guardar el puntaje y el nombre de cada jugador en la base de datos*/
    public void guardaDatos(){
        if (!temporizadorActivado){
            DataBase db=new DataBase(this);
            db.save(nomJ1,puntaje1,dificultad);
            db.save(nomJ2,puntaje2,dificultad);
        }
    }

    public void onResume(){
        super.onResume();
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);

        nomJ1=datos.getString(Home.PLAYER1,"jugador1");
        nomJ2=datos.getString(Home.PLAYER2,"jugador2");

        temporizadorActivado=datos.getBoolean(Home.MODO,false);

        if (temporizadorActivado){
            tiempoPartida(datos.getLong(Home.TIEMPO,10000));
            viewChronometer.getLayoutParams().height=0;
        }
        else viewChronometer.start();


        actualizaPantalla();
        cargarCartas();
    }


    /*Metodo para abrir el dialog final que mustra el resultado del juego*/
    public void abreDialogFinal(){
        Dialog dialogFin=new Dialog(this);
        dialogFin.setContentView(R.layout.dialog_final_partida);
        dialogFin.setCanceledOnTouchOutside(false);
        dialogFin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView ganador, perdedor, punt1, punt2, tiempoT, empateIcon, empateText;
        ganador=dialogFin.findViewById(R.id.final_ganador);
        perdedor=dialogFin.findViewById(R.id.final_perdedor);
        punt1=dialogFin.findViewById(R.id.final_puntaje_ganador);
        punt2=dialogFin.findViewById(R.id.final_puntaje_perdedor);
        tiempoT=dialogFin.findViewById(R.id.final_tiempo);
        empateIcon=dialogFin.findViewById(R.id.final_item_empate);
        empateText=dialogFin.findViewById(R.id.final_text_empate);



        if (puntaje1>puntaje2){
            ganador.setText(nomJ1);
            perdedor.setText(nomJ2);
            punt1.setText(puntaje1+"");
            punt2.setText(puntaje2+"");

        }else if (puntaje2>puntaje1){
            ganador.setText(nomJ2);
            perdedor.setText(nomJ1);
            punt1.setText(puntaje2+"");
            punt2.setText(puntaje1+"");
        }else{
            empateIcon.setBackgroundResource(R.drawable.icon_empate);
            empateText.setText("EMPATE");
            ganador.setText(nomJ1);
            perdedor.setText(nomJ2);
            punt1.setText(puntaje1+"");
            punt2.setText(puntaje2+"");

            punt1.setBackgroundResource(R.drawable.item_empate);
            punt2.setBackgroundResource(R.drawable.item_empate);

        }

        tiempoT.setText(tiempoPartida+"");


        LinearLayout layout=dialogFin.findViewById(R.id.layout_dialog_final);
        layout.startAnimation(animAparecer);


        dialogFin.show();
        dialogFin.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent=new Intent(getApplicationContext(),Partida.class);
                intent.putExtra(Home.DIFICULTAD,dificultad);
                finish();
                startActivity(intent);
            }
        });
    }


    /*Este metodo es para obtener el boton presionado en el dialogo final para asi realizar la accion correspondiente
    * */
    public void botonesDialogFinal(View v){

        Intent intent;
        switch (v.getId()){
            case R.id.final_bton_home:
                intent=new Intent(this,Home.class);
                startActivity(intent);
                finish();
                break;
            case R.id.final_bton_share:
                abreDialogCompartir();
                break;
            case R.id.final_bton_replay:
                 intent=new Intent(getApplicationContext(),Partida.class);
                intent.putExtra(Home.DIFICULTAD,dificultad);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void abreDialogCompartir(){
        Dialog dialogCompartir=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialogCompartir.setContentView(R.layout.dialog_compartir);
        dialogCompartir.setCanceledOnTouchOutside(false);
        dialogCompartir.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView facebook, twitter;

        facebook=dialogCompartir.findViewById(R.id.share_bton_facebook);
        twitter=dialogCompartir.findViewById(R.id.share_bton_twitter);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout layout=dialogCompartir.findViewById(R.id.layout_dialog_compartir);
        layout.startAnimation(animAparecer);

        dialogCompartir.show();


    }

    public void onBackPressed(){
        Intent intent=new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }


}
