package com.sp.mm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Splashscreen extends Activity {

    private static boolean splashLoaded = false;
    private ImageView splashicon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (!splashLoaded) {
            setContentView(R.layout.splashscreen);
            splashicon = findViewById(R.id.splashimg);
            splashicon.setImageResource(R.drawable.splashimg);
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(Splashscreen.this, Registration.class));
                    finish();
                }
            }, secondsDelayed * 1000);

            splashLoaded = true;
        }
        else {
            Intent goToMainActivity = new Intent(Splashscreen.this, Registration.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}
