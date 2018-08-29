package com.worldskills.myapplication.Redes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class RedObject {

    Context context;
// SE NECESITA ENVIAR UN CONTEXTO PARA QUE LAS SDKS INICIEN, PARA ELLO SE CREO UN CONSTRUCTOR Y SE USO LAS POO.
    public RedObject(Context context) {
        this.context = context;
    }


    // METODO PUBLICO QUE DEVOLVERA LA ACCIÓN DE COMPARTIR EN FACEBOOK
    public void shareFacebook (int puntaje){
        FacebookSdk.sdkInitialize(context);

        ShareDialog dialog = new ShareDialog((Activity) context);

        if(dialog.canShow(ShareLinkContent.class)){
            ShareLinkContent link = new ShareLinkContent.Builder()
                    .setQuote("¡He jugado WSEMPAREJA APP, mi puntaje ha sido "+puntaje+ " Descargala y acepta este reto para tu mente")
                    .setContentUrl(Uri.parse("https://developers.facebook.com/")).build();

            dialog.show(link);
        }
    }
    // METODO PUBLICO QUE DEVOLVERA UN INTENT DE COMPARTIR EN TWITTER. OBSERVACION: SE ESPERO HASTA EL FINAL DE LA PRUEBA PARA LA APROBACIÓN DE TWITTER
    // DE LA CUENTA Y SIN EMABRGO NUNCA LLEGO, POR ELLO SE REALIZO CON UN INTENT. SE ADJUNTA SS DE LA PANTALLA DE TWITTER.
    public Intent shareTwitter (int puntaje){
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.twitter.android");
        intent.putExtra(intent.EXTRA_TEXT, "¡He jugado WSEMPAREJA APP, mi puntaje ha sido "+puntaje+ " Descargala ahora y acepta este reto para tu mente");

        return intent;
    }
}
