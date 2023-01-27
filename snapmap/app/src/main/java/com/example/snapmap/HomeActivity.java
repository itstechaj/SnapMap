package com.example.snapmap;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView userName,emailAddress,joinDate;
    CardView openCamera,myPhotos,heatmap,logoutcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //link user details textviews variables with their views
        userName=findViewById(R.id.userName);
        emailAddress=findViewById(R.id.emailAddress);
        joinDate=findViewById(R.id.joinDate);
        //fetching user data and setting it
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and use the values to update the UI
                users user = dataSnapshot.getValue(users.class);

                String usernameText="Username: ";
                if(user!=null) usernameText+=user.getUsername();
                else usernameText+="NA";
                userName.setText(usernameText);

//                Toast.makeText(HomeActivity.this, "Username :"+usernameText, Toast.LENGTH_SHORT).show();

                String emailText="Email: ";
                if(user!=null) emailText+=user.getEmail();
                else emailText+="NA";
                emailAddress.setText(emailText);

                String joinDateText="Join Date: ";
                if(user!=null) joinDateText+=user.getJoinDate();
                else joinDateText+="NA";
                joinDate.setText(joinDateText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting User failed, log a message
                Toast.makeText(HomeActivity.this, "Can't Load user details!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        };
        databaseReference.addValueEventListener(userListener);
        // finding and making cardView clickable
        openCamera=findViewById(R.id.OpenCameraCard);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate user to MainActivity to click picture
                startActivity(new Intent(HomeActivity.this,MainActivity.class ));
            }
        });
        myPhotos=findViewById(R.id.myPhotosCard);
        myPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate user  to userActivity to show listview of uploaded image
                startActivity(new Intent(HomeActivity.this,userActivity.class ));
            }
        });
        heatmap=findViewById(R.id.heatMapCard);
        heatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate user  heatMapActivity to show listview of uploaded image
                startActivity(new Intent(HomeActivity.this,HeatmapActivity.class ));
            }
        });
        logoutcard=findViewById(R.id.logoutCard);
        // declaring shared preference instance
        final SharedPreferences sharedPreferences=getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        logoutcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write code to logout the user
                //logout logic start here
                //clear data in the shared preference
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                //logout logic ends here
                // Toasting logout successful
                Toast.makeText(HomeActivity.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
                //after logout navigate user to loginActivity
                startActivity(new Intent(HomeActivity.this,LoginActivity.class ));
            }
        });


    }
}