package com.ndkapp.www.tuktuk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class aboutus extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    static int ct = 0;
    ImageButton infobtn;
    ImageButton mail, ybtn, kbtn, locbtn;
    TextView infotext, taptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        infobtn = findViewById(R.id.info);
        infotext = findViewById(R.id.infotxt);
        taptext = findViewById(R.id.tap);
        mail = findViewById(R.id.contactmail);
        ybtn = findViewById(R.id.yash);
        locbtn = findViewById(R.id.location);
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String information = "If you want to recommend us any new feature or you are facing any issue or want to give information about any driver who is not in " +
                        "the list then you can contact us by clicking the EMAIL button ";
                if (ct == 0) {
                    infotext.setText(information);
                    taptext.setText(" ");
                    ct += 1;
                } else {
                    infotext.setText(" ");
                    taptext.setText("Tap here");
                    ct -= 1;
                }
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                //REDIRECTING TO GMAIL
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:" + "yashsp1158@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT, "Query or Feedback");
                try {
                    startActivity(Intent.createChooser(i, "Choose an Email client :"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(aboutus.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ybtn.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("101010101");
            }
        });

        locbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_VIEW);
                //REDIRECTING TO GOOGLE MAPS
                in.setData(Uri.parse("https://www.google.com/maps/place/Royal+Care+Boys+Hostel/@22.597658,72.8216473,17z/data=!3m1!4b1!4m5!3m4!1s0x395e50c30bd44029:0xa7ccb7b99f760e6e!8m2!3d22.597658!4d72.823836"));
                startActivity(in);
            }
        });
    }

    private void makePhoneCall(String num) {
        if (ContextCompat.checkSelfPermission(aboutus.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(aboutus.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + num;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(aboutus.this, afterLoggedIn.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
