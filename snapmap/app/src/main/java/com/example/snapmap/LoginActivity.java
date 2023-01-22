package com.example.snapmap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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
        Button btn_sign = findViewById(R.id.signupButton);
        mAuth=FirebaseAuth.getInstance();
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
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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