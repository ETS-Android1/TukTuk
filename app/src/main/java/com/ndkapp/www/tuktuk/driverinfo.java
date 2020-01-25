package com.ndkapp.www.tuktuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class driverinfo extends AppCompatActivity {
    ImageButton D1, D2;
    TextView dname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverinfo);
        dname = findViewById(R.id.d1);
        final String num = getIntent().getStringExtra("mobile");
        final String msg = getIntent().getStringExtra("finalmsg");
        D1 = (ImageButton) findViewById(R.id.book1);
        D2 = (ImageButton) findViewById(R.id.kbtn);
        D1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(driverinfo.this, varification.class);
                in.putExtra("mobile", num);
                in.putExtra("finalmsg", msg);
                in.putExtra("dname", "DRIVER 1");
                in.putExtra("mobile2", "+911111111111");
                Log.d("dnum", "+918758990480");
                startActivity(in);
            }
        });
        D2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(driverinfo.this, varification.class);
                in.putExtra("mobile", num);
                in.putExtra("finalmsg", msg);
                in.putExtra("dname", "DRIVER 2");
                in.putExtra("mobile2", "+911111111111");
                startActivity(in);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(driverinfo.this, afterLoggedIn.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
