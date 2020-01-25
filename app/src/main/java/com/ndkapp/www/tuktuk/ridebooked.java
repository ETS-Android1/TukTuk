package com.ndkapp.www.tuktuk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ridebooked extends AppCompatActivity {
TextView number,name,info;
String tcallnum;
ImageView callicon;
String infomsg;
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ridebooked);
        name=findViewById(R.id.drivername);
        info=findViewById(R.id.rideinfo);
        final String callnum=getIntent().getStringExtra("drivernum");
        final String dname=getIntent().getStringExtra("dname");
        infomsg=getIntent().getStringExtra("rideinfo");
        info.setText(infomsg);
        name.setText(dname);
        tcallnum=callnum;
        number=findViewById(R.id.callnumber);
        number.setText(callnum);
        callicon=findViewById(R.id.callimage);
        callicon.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(callnum);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i =new Intent(ridebooked.this,afterLoggedIn.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    //CODE TO MAKE PHONE CALL
    private void makePhoneCall(String num)
    {
            if(ContextCompat.checkSelfPermission(ridebooked.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ridebooked.this, new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL );
            }
            else
            {
                String dial = "tel:" +num;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
    }
    //FOR REQUEST PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                makePhoneCall(tcallnum);
            }
            else
            {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
