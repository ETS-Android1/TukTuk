package com.ndkapp.www.tuktuk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class account extends AppCompatActivity {

    TextView n, number, m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        String name = getIntent().getStringExtra("name");
        String num = getIntent().getStringExtra("acnumber");
        String mail = getIntent().getStringExtra("acmail");
        n = findViewById(R.id.acname);
        number = findViewById(R.id.mobile);
        m = findViewById(R.id.acmail);
        n.setText((CharSequence) name);
        number.setText((CharSequence) num);
        m.setText((CharSequence) mail);
    }
}
