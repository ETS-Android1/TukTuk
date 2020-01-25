package com.ndkapp.www.tuktuk;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

//FIREBASE PACKAGES
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.ndkapp.www.tuktuk.MainActivity.isempty;
import static com.ndkapp.www.tuktuk.MainActivity.mail;

public class afterLoggedIn extends AppCompatActivity {
    FirebaseAuth mAuth;
    Toolbar t;
    EditText pdate, ptime,noofpassenger;
    DrawerLayout drawer;
    TextView head;
    Spinner p;
    Spinner dr;

    private DatabaseReference myRef;

    private String userID;
    final Calendar c = Calendar.getInstance();
    Button book;
    DatabaseHelper dbh;
    String strnum,strname;
    String pickdate,picktime,pickloc,droploc,passenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_logged_in);
        loaddata();
        dbh=new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        pdate = (EditText) findViewById(R.id.date);
        ptime = (EditText) findViewById(R.id.time);
        noofpassenger=findViewById(R.id.noofpass);
        book = (Button) findViewById(R.id.bookbtn);
        drawer = findViewById(R.id.draw_activity);
        strname=dbh.getName(mail);
        head=findViewById(R.id.headertext);
        head.setText(strname);
        p=findViewById(R.id.pickup);
        dr=findViewById(R.id.drop);
        t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = findViewById(R.id.nav_view);

        //code to set format of date
        final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        //code to select date from calender
        pdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(afterLoggedIn.this, d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                pickdate="Pickup Date : "+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR)+" ";
            }
        });

        //code to select time from clock
        ptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                TimePickerDialog tp = new TimePickerDialog(afterLoggedIn.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ptime.setText(hourOfDay + ":" + minute);
                        picktime="Pickup Time: "+hourOfDay + ":" + minute+" ";
                    }
                }, hour, min, true);
                tp.setTitle("Selected time");
                tp.show();
            }
        });

        //code to set navigation view in activity and add clicklisteners to the menu items
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.account:
                        strnum=dbh.getNumber(mail);
                        Intent ain=new Intent(afterLoggedIn.this,account.class);
                        ain.putExtra("name",strname);
                        ain.putExtra("acnumber",strnum);
                        ain.putExtra("acmail",mail);
                        startActivity(ain);
                       break;
                    case R.id.logout:
                        mAuth.signOut();
                        //finish();
                        Intent in = new Intent(afterLoggedIn.this, MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        break;
                    case R.id.share:
                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        startActivity(Intent.createChooser(myIntent,"SHARE USING"));
                        break;
                    case R.id.contact:
                        Intent i=new Intent(afterLoggedIn.this,aboutus.class);
                        startActivity(i);
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //code to book a ride by clicking th button
        book.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (isempty(pdate)) {
                    pdate.setError("Select date!!");
                    return;
                }
                if (isempty(ptime)) {
                    ptime.setError("Select time!!");
                    return;
                }
                passenger=noofpassenger.getText().toString();
                pickloc = p.getSelectedItem().toString();
                droploc = dr.getSelectedItem().toString();
                if(isempty(noofpassenger))
                {
                     noofpassenger.setError("Select number of passengers!!");
                     return;
                }
                int ps=Integer.parseInt(passenger);
                if(ps>5) {
                    noofpassenger.setTextColor(R.color.colorAccent);
                    noofpassenger.setError("Not more than 5!!");
                    return;
                }
                if(pickloc.equals(null)||pickloc.equals("---Select Location---"))
                {
                    TextView errorText = (TextView)p.getSelectedView();
                    errorText.setError("Select location");
                    errorText.setTextColor(R.color.colorAccent);
                    return;
                }
                if(droploc.equals(null)||droploc.equals("---Select Location---"))
                {
                    TextView errorText = (TextView)p.getSelectedView();
                    errorText.setError("Select location");
                    errorText.setTextColor(R.color.colorAccent);
                    return;
                }
                if(pickloc.equals(droploc))
                {
                    TextView errorText = (TextView)p.getSelectedView();
                    errorText.setError("Both places can't be same");
                    errorText.setTextColor(R.color.colorAccent);
                    return;
                }
                strnum=dbh.getNumber(mail);
                Log.d("in afterloggedin","number"+mail);
                String message= "Booked by : "+strname+"\n"+"Pickup Location :"+pickloc+"\n"+"Drop Location : "+droploc+"\n"+pickdate+"\n"+picktime+"\nNo.of passenger:"+passenger;
                Intent in = new Intent(afterLoggedIn.this, driverinfo.class);
                in.putExtra("mobile", strnum);
                in.putExtra("finalmsg",message);
                startActivity(in);
            }
        });
    }

    //function to set date format
    private void updateLabel() {
        String format = "dd/MM/yy";
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
        pdate.setText(df.format(c.getTime()));
    }

    //function to set close navigation bar on pressing back button
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            Intent i = new Intent(afterLoggedIn.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public void loaddata()
    {
        SharedPreferences sref=getSharedPreferences("shared",MODE_PRIVATE);
        mail=sref.getString("email", String.valueOf(MODE_PRIVATE));
        Log.d("in loaddata","mail"+mail);
    }
}
