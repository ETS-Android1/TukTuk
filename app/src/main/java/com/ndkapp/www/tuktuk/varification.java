package com.ndkapp.www.tuktuk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class varification extends AppCompatActivity {
    Button vbtn;
    String codeSent;
    FirebaseAuth Auth;
    EditText codevarification;
    ProgressBar pb2;
    public String tnum,tnum2,message,driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varification);
        vbtn =(Button)findViewById(R.id.varifybtn);
        Auth=FirebaseAuth.getInstance();
        codevarification=(EditText)findViewById(R.id.otp);
        pb2=findViewById(R.id.progressbar2);
        Log.d("before num","num getting");

        final String num=getIntent().getStringExtra("mobile");
        final String num2=getIntent().getStringExtra("mobile2");
        final String msg=getIntent().getStringExtra("finalmsg");
        final String dname=getIntent().getStringExtra("dname");

        Toast.makeText(varification.this,num,Toast.LENGTH_LONG).show();
        driver=dname;
        message=msg;
        tnum2=num2;
        tnum=num;

        sendVerificationCode(num);

        vbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                verifyCode(codevarification.getText().toString());
                getPermission();
            }
        });
    }

    //FUNCTION TO VERIFY THE OTP
    public void verifyCode(String codeentered)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,codeentered );
        signInWithPhoneAuthCredential(credential);
    }

    //FUNCTION TO SEND OTP TO USER FROM FIREBASE
    private void sendVerificationCode(String number)
    {
        Log.d("after num","num sent for varification");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    //CODE FOR ACTIONS AFTER OTP VERIFICATION
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            Log.d("in callbacks","getting code");
            if(code!=null)
            {
                Log.d("in callbacks","varifying code");
                verifyCode(code);
                getPermission();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d("in callbacks","sent code");
            codeSent=s;
        }
    };

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pb2.setVisibility(View.VISIBLE);
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pb2.setVisibility(View.GONE);
                            Intent i =new Intent(varification.this,ridebooked.class);
                            i.putExtra("drivernum",tnum2);
                            i.putExtra("dname",driver);
                            i.putExtra("rideinfo",message);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Invalid Code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //CODE TO GET PERMISSION FOR TEXT MESSAGING
    public void getPermission()
    {
        int percheck= ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if(percheck== PackageManager.PERMISSION_GRANTED)
        {
            sendMessage();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }
    }
    public void sendMessage()
    {
       //String message="test message arrived";
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(tnum2,null,message,null,null);
        sms.sendTextMessage(tnum,null,message,null,null);
        Toast.makeText(varification.this,"message sent",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case 0:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    sendMessage();
                }
                else
                {
                    Toast.makeText(this,"Dont have permission",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i =new Intent(varification.this,afterLoggedIn.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
