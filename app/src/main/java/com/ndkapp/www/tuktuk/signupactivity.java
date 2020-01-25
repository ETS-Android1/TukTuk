package com.ndkapp.www.tuktuk;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static com.ndkapp.www.tuktuk.MainActivity.isempty;

public class signupactivity extends AppCompatActivity {
    TextView login;
    public EditText name, password, cpassword, uemail;
    public static EditText number;
    Button sbtn;
    FirebaseAuth mAuth;
    ProgressBar pb;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        mAuth = FirebaseAuth.getInstance();
        login = (TextView) findViewById(R.id.logintxt);
        password = (EditText) findViewById(R.id.psd);
        name = (EditText) findViewById(R.id.uname);
        number = (EditText) findViewById(R.id.unum);
        cpassword = (EditText) findViewById(R.id.cpsd);
        sbtn = (Button) findViewById(R.id.signupbtn);
        uemail = (EditText) findViewById(R.id.umail);
        pb = findViewById(R.id.profressbar1);
        dbh = new DatabaseHelper(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeact();
            }
        });

        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                //VALIDATION CONDITIONS
                if (isempty(name)) {
                    pb.setVisibility(View.GONE);
                    name.setError("Enter name");
                    return;
                }
                if (isempty(number)) {
                    pb.setVisibility(View.GONE);
                    number.setError("Enter mobile number");
                    return;
                }
                if (isempty(password)) {
                    pb.setVisibility(View.GONE);
                    password.setError("Enter password");
                    return;
                }
                if (isempty(cpassword)) {
                    pb.setVisibility(View.GONE);
                    cpassword.setError("Confirm password");
                    return;
                }
                if (password.getText().toString().length() <= 8) {
                    pb.setVisibility(View.GONE);
                    password.setError("Password length must be greater than 8!!");
                    return;
                }
                if (isempty(uemail)) {
                    pb.setVisibility(View.GONE);
                    uemail.setError("Enter email address");
                    return;
                }
                if (number.getText().toString().length() != 13) {
                    pb.setVisibility(View.GONE);
                    number.setError("Please enter 10 digit number preceding with +91");
                    return;
                }
                if (!(password.getText().toString().equals(cpassword.getText().toString()))) {
                    pb.setVisibility(View.GONE);
                    cpassword.setError("Password must be same as above");
                    return;
                }
                //SIGNING UP USER WITH EMAIL AND PASSWORD
                mAuth.createUserWithEmailAndPassword(uemail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pb.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            dbh.insertData(name.getText().toString(), number.getText().toString(), uemail.getText().toString());
                            savedata();
                            finish();
                            registerData();
                            Intent in = new Intent(signupactivity.this, afterLoggedIn.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                        } else {
                            Toast.makeText(signupactivity.this, "Email already exist!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // FUNCTION TO REGISTER DATA IN FIREBASE DATABASE
    private void registerData() {
        final String n = name.getText().toString();
        final String mn = number.getText().toString();
        final String em = uemail.getText().toString();
        users u = new users(n, mn, em);
        FirebaseDatabase.getInstance().getReference("Users").child((FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(u);
        Toast.makeText(signupactivity.this, "Registered successfully!!", Toast.LENGTH_SHORT).show();
    }

    //FUNCTION TO SWITCH ACTIVITY
    private void changeact() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    // FUNCTION TO SAVE DATA IN MOBILE
    public void savedata() {
        SharedPreferences sref = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor edit = sref.edit();
        edit.putString("email", uemail.getText().toString());
        edit.apply();
    }
}
