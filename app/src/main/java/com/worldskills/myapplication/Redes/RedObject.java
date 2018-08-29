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

    public RedObject(Context context) {
        this.context = context;
    }

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

    public Intent shareTwitter (int puntaje){
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.twitter.android");
        intent.putExtra(intent.EXTRA_TEXT, "¡He jugado WSEMPAREJA APP, mi puntaje ha sido "+puntaje+ " Descargala ahora y acepta este reto para tu mente");

        return intent;
    }
}
