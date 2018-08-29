package com.worldskills.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

/////////////// Se buscan los componentes en xml para animar, se instancia la animación y se ordena que deje la figura en la posición final
        ImageView imageView= findViewById(R.id.image);
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.alpha);
        imageView.setAnimation(animation);

        animation.setFillAfter(true);
///////////////// Se crea un hilo para que termine el tiempo e inicie la partida, se envia un booleano para home y saber asi que es primera vez
        //// y asi digitalizar los nombres
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(Splash.this, Home.class);
                intent.putExtra("ingreso", true);
                startActivity(intent);
            }
        }, 3000);


    }
}
