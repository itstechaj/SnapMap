package com.example.snapmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.emailEditText);
        password=findViewById(R.id.passwordEditText);
        Button btn_login = findViewById(R.id.loginButton);
        //register
        TextView btn_sign = findViewById(R.id.signupButton);
        mAuth=FirebaseAuth.getInstance();
        //shared preference declaration
        final SharedPreferences sharedPreferences=getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        // checking if shared preference already have data then directly login
        final String previousUserMail=sharedPreferences.getString("userEmail","");
        if(!previousUserMail.isEmpty())
        {
            //user is already login so directly redirect to home activity
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        btn_login.setOnClickListener(v -> {
            String emailtxt= email.getText().toString().trim();
            String passwordtxt=password.getText().toString().trim();
            if(emailtxt.isEmpty())
            {
                email.setError("Email is empty");
                email.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches())
            {
                email.setError("Enter the valid email");
                email.requestFocus();
                return;
            }
            if(passwordtxt.isEmpty())
            {
                password.setError("Password is empty");
                password.requestFocus();
                return;
            }
            if(password.length()<6)
            {
                password.setError("Length of password is more than 6");
                password.requestFocus();
                return;
            }
            mAuth.signInWithEmailAndPassword(emailtxt,passwordtxt).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    //first save the login data into shared preference for future login
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("userEmail",emailtxt);
                    editor.putString("password",passwordtxt);
                    editor.apply();
                    //showing toast of successful login
                    Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show();
                    //if login successful then redirect to homeActivity
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,
                            "Please Check Your login Credentials",
                            Toast.LENGTH_SHORT).show();
                }

            });
        });
        btn_sign.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,RegistrationActivity.class )));
    }

}