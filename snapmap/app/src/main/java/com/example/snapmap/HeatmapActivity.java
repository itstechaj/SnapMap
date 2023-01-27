package com.example.snapmap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HeatmapActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<Coordinate> coordinates=new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
        ImageView mapImageView = findViewById(R.id.heatmap_image_view);

        //setting database reference
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("Uploaded Images");
//        coordinates = getIntent().getParcelableArrayListExtra("coordinates");
        //fetching data code starts here
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserImage user = userSnapshot.getValue(UserImage.class);
                    Double latitude = Double.valueOf(user.getLatitude());
                    Double longitude = Double.valueOf(user.getLongitude());
                    Coordinate mycord=new Coordinate(latitude, longitude);
//                    System.out.println("Latitude: " + mycord.getLatitude() + " , Longitude: " + mycord.getLongitude());
                    coordinates.add(mycord);
                }
                //get center of coordinates
                Coordinate centerCoordinate=getCenter();
                String centerLat= String.valueOf(centerCoordinate.getLatitude());
                String centerLong= String.valueOf(centerCoordinate.getLongitude());
                //building url and showing image code starts here
                String str=centerLat+"%2C"+centerLong+"/16";
                //actual url -> https://dev.virtualearth.net/REST/V1/Imagery/Map/AerialWithLabels/?mapSize=300%2C600&format=png&dcl=1&pushpin=42.6564%2C-73.7638%3B1&pushpin=30.6564%2C-80.7638%3B1&pushpin=50.6564%2C-73.7638%3B1&key=AmqwMGmPtRT0bZK_I2ww-yIDuL2WYd3YsWhjbSxYMWMqq-IJtE0ifNWMvJPJPz2S
                StringBuilder urlBuilder = new StringBuilder("https://dev.virtualearth.net/REST/V1/Imagery/Map/AerialWithLabelsOnDemand/"+str+"?mapSize=300%2C600&format=png&dcl=1&");

                for (int i = 0; i < coordinates.size(); i++) {
                    Coordinate coordinate = coordinates.get(i);
                    urlBuilder.append("pushpin=").append(coordinate.getLatitude()).append("%2C").append(coordinate.getLongitude()).append("%3B56%3B&");

                }
                urlBuilder.append("key=AmqwMGmPtRT0bZK_I2ww-yIDuL2WYd3YsWhjbSxYMWMqq-IJtE0ifNWMvJPJPz2S");
                String heatMapUrl = urlBuilder.toString();
                System.out.println(heatMapUrl);
                Picasso.get().load(heatMapUrl).into(mapImageView);
                //building url and showing image code starts here
                //getting height and width of mapImageView
                int width = mapImageView.getWidth();
                int height = mapImageView.getHeight();
                System.out.println("height = " + height+" ,Width = "+width);
                //getting height and width of mapImageView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        //fetching data code ends here

    }
    public Coordinate getCenter() {
        double totalX = 0;
        double totalY = 0;
        for (Coordinate coordinate : coordinates) {
            totalX += coordinate.getLatitude();
            totalY += coordinate.getLongitude();
        }
        double centerX = totalX / coordinates.size();
        double centerY = totalY / coordinates.size();
        return new Coordinate(centerX, centerY);
    }
}


//    public void getCoordinates() {
//
//}
