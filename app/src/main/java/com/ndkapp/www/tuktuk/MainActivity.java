package com.ndkapp.www.tuktuk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.util.Objects.requireNonNull;


public class MainActivity extends AppCompatActivity {
    TextView signup;
    public EditText umail,upsd;
    Button loginbtn;
    public static String mail,passwd;
    FirebaseAuth mAuth;
    ProgressBar pb2;
    String tnum2;
    String tname2;
    String tmail2;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup=(TextView)findViewById(R.id.signuptxt);
        loginbtn=(Button)findViewById(R.id.startbtn);
        umail=(EditText)findViewById(R.id.useremail);
        upsd=(EditText)findViewById(R.id.password);

        mAuth=FirebaseAuth.getInstance();

        dbh=new DatabaseHelper(this);
        final FirebaseUser user=mAuth.getCurrentUser();

        pb2=findViewById(R.id.pb1);

        //CHECKING THAT A USER ALREADY LOGGED IN OR NOT
        if (user != null) {
            finish();
            Intent i = new Intent(MainActivity.this, afterLoggedIn.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        signup.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeact();
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(isempty(umail))
                    {
                        umail.setError("Enter email address!!");
                        return;
                    }
                    if(isempty(upsd))
                    {
                        upsd.setError("Enter password!!");
                        return;
                    }

                    //CODE FOR LOGGING A USER IN USING EMAIL AND PASSWORD
                    mAuth.signInWithEmailAndPassword(umail.getText().toString(),upsd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pb2.setVisibility(View.VISIBLE);
                            if(task.isSuccessful())
                            {
                                finish();
                                mail=umail.getText().toString();
                                Log.d("mail","mail"+mail);
                                passwd=upsd.getText().toString();
                                savedata();
                                String userID=mAuth.getCurrentUser().getUid();
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                                myRef.keepSynced(true);
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {

                                            //STORING DATA IN LOCAL DATABASE
                                            tnum2 = dataSnapshot.child("mobilenumber").getValue(String.class);
                                            tname2= requireNonNull(dataSnapshot.child("name").getValue()).toString();
                                            tmail2= requireNonNull(dataSnapshot.child("email").getValue()).toString();
                                            Toast.makeText(MainActivity.this,tnum2,Toast.LENGTH_LONG).show();
                                            if(dbh.getNumber(tmail2).equals("-1")) {
                                                dbh.insertData(tname2, tnum2, tmail2);
                                                Log.d("after insert","data inserted"+dbh.getNumber(tmail2));
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"datasnapshot not found",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                pb2.setVisibility(View.GONE);
                                Intent i =new Intent(MainActivity.this,afterLoggedIn.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else
                            {
                                pb2.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"Incorrect password or email!!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }
        });
    }

    private void changeact()
    {
        Intent i = new Intent(this,signupactivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    //FUNCTION TO CHECK WHETHER A TEXTFIELD IS EMPTY OR NOT
    public static boolean isempty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            finish();
            Intent i = new Intent(MainActivity.this, afterLoggedIn.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {

        }
    }

    //FUNCTION TO SAVE DATA IN MOBILE
    public void savedata()
    {
        SharedPreferences sref=getSharedPreferences("shared",MODE_PRIVATE);
        SharedPreferences.Editor edit=sref.edit();
        edit.putString("email",mail);
        edit.apply();
        Log.d("in savedata","mail"+mail);
    }

    //FUNCTION TO LOAD DATA FROM MOBILE
    public String loaddata()
    {
        SharedPreferences sref=getSharedPreferences("shared",MODE_PRIVATE);
        mail=sref.getString("email", String.valueOf(MODE_PRIVATE));
        return mail;
    }
}
