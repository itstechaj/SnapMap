package com.example.snapmap;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private final int CAMERA_REQ_CODE = 100;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseStorage storage;
    private StorageReference storagereference;
    public Uri imageUri;
    ImageView imgCamera,mapView;
    TextView latLongTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCamera = findViewById(R.id.imgCamera);
        Button btnCamera = findViewById(R.id.btnCamera);


        //ask for location permission

        askForLocationPermission();

        //getting mapview and texts

        mapView = findViewById(R.id.map_view);
        latLongTextView = findViewById(R.id.lat_long_text);

        btnCamera.setOnClickListener(view -> {
            Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(iCamera, CAMERA_REQ_CODE);
        });

        Button uploadButton = findViewById(R.id.btnUpload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the uploadImageToFirebase() method here
//                uploadImageToFirebase(img);
                Toast.makeText(MainActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                double latitude,longitude;
                assert data != null;
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
//                Uri imageUri = data.getData();
                Location location = getCurrentLocation();
                assert location != null;
                latitude=location.getLatitude();
                longitude=location.getLongitude();
//                addLocationToImage(img, latitude, longitude);


                String bingMapsApiKey = "AmqwMGmPtRT0bZK_I2ww-yIDuL2WYd3YsWhjbSxYMWMqq-IJtE0ifNWMvJPJPz2S"; // Replace with your own Bing Maps API key
                String center = latitude+","+longitude; // Coordinates for the center of the map view
                int zoom = 16; // Zoom level for the map view
                int width = 800; // Width of the map image in pixels
                int height = 560; // Height of the map image in pixels

// Construct the URL for the static map image
                String mapUrl = "https://dev.virtualearth.net/REST/v1/Imagery/Map/Road/" + center + "/" + zoom + "?mapSize=" + width + "," + height +"&pushpin="+center+"&key=" + bingMapsApiKey;

                // Set the latitude and longitude text
                latLongTextView.setText(String.format("Latitude: %s, Longitude: %s", latitude, longitude));

                // Use Picasso library to load the map image
                Picasso.get().load(mapUrl).into(mapView);

                imgCamera.setImageBitmap(img);
//                onCaptureImage(data);
            }
        }
    }


    private void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality from apps permission settings")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                openLocationSettings();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // Permission has already been granted
//            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied. This app will not work...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services.", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_SHORT).show();
            return null;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        String latstr=String.valueOf(location.getLatitude());
        String longstr=String.valueOf(location.getLongitude());
        String msg="Longitude="+longstr+" & Latitude="+latstr;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return location;
    }


    private void openLocationSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    //compress and upload image to firebase
    private void onCaptureImage(Intent data) {
        Bitmap img = (Bitmap) (data.getExtras().get("data"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bb = bytes.toByteArray();
//        imgCamera.setImageBitmap(img);

        uploadToFirebase(bb);
    }

    private void uploadToFirebase(byte[] bb) {
        String filename = "IMG";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD_HH:MM:SS", Locale.CANADA);
            Date now = new Date();
            filename = formatter.format(now);
        }

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading File...");
        dialog.show();


        StorageReference riversRef = storagereference.child("images/"+filename+".jpg");
        riversRef.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double percent = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :" + (int) percent + "%");
                    }
                })


                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Toast.makeText(MainActivity.this, "Failed To Upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }




}
