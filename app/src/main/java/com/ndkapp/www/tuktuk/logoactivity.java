package com.ndkapp.www.tuktuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class logoactivity extends AppCompatActivity {
    Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logoactivity);

        //CODE FOR SPLASH SCREEN
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i1 = new Intent(logoactivity.this, MainActivity.class);
                startActivity(i1);
                finish();
            }
        }, 2000);
    }
}
