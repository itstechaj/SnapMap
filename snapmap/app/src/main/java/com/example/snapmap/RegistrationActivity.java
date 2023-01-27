package com.example.snapmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RegistrationActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    Button signupBtn;
    TextView loginBtn;
    EditText username,email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //shared preference declaration
        final SharedPreferences sharedPreferences=getSharedPreferences("LoginData", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_registration);
        username=findViewById(R.id.usernameEditText);
        email=findViewById(R.id.emailEditText);
        password=findViewById(R.id.passwordEditText);
        signupBtn=findViewById(R.id.registerButton);
        loginBtn=findViewById(R.id.loginButton);
        mAuth=FirebaseAuth.getInstance();
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernametxt=username.getText().toString().trim();
                String emailtxt = email.getText().toString().trim();
                String passwordtxt= password.getText().toString().trim();
                if(usernametxt.isEmpty())
                {
                    email.setError("Username is empty");
                    email.requestFocus();
                    return;
                }
                if(emailtxt.isEmpty())
                {
                    email.setError("Email is empty");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches())
                {
                    email.setError("Enter the valid email address");
                    email.requestFocus();
                    return;
                }
                if(passwordtxt.isEmpty())
                {
                    password.setError("Enter the password");
                    password.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    password.setError("Length of the password should must be >= 6");
                    password.requestFocus();
                    return;
                }
//                mAuth.createUserWithEmailAndPassword(emailtxt,passwordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful())
//                        {
//                            Toast.makeText(RegistrationActivity.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(RegistrationActivity.this,userActivity.class ));
//                        }
//                        else
//                        {
//                            Toast.makeText(RegistrationActivity.this,"Already Registered",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                mAuth.createUserWithEmailAndPassword(emailtxt, passwordtxt)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(RegistrationActivity.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                                    String uid=task.getResult().getUser().getUid();
//                                    List<String> imageUrls=new ArrayList<>();
                                    //getting current date in form of string
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy  HH:mm:ss",Locale.US);
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                                    String joinDate = dateFormat.format(new Date());

                                    users user=new users(uid,usernametxt,emailtxt,passwordtxt,joinDate);
                                    firebaseDatabase.getReference().child("users").child(uid).setValue(user);
                                    //now we also have to save the login user data into shared preference
                                    //first save the login data into shared preference for future login
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString("userEmail",emailtxt);
                                    editor.putString("password",passwordtxt);
                                    editor.apply();
                                    //now registration successful and also saved in shared preference .now redirect to homeActivity
                                    startActivity(new Intent(RegistrationActivity.this,HomeActivity.class ));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegistrationActivity.this,"Already Registered!! Please Login",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class ));
            }
        });
    }


}