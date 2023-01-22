package com.example.snapmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    //button for showing uploaded image
    Button showUploadedImgBtn;

    private final int CAMERA_REQ_CODE = 100;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private StorageReference storagereference;
    DatabaseReference databaseReference;
//    DatabaseReference databaseReference;

    ImageView imgCamera,mapView;
    TextView latLongTextView;
    Intent imgdata;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCamera = findViewById(R.id.imgCamera);
        Button btnCamera = findViewById(R.id.btnCamera);

        showUploadedImgBtn=findViewById(R.id.showUploadedImages);

        storagereference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

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
                onCaptureImage(imgdata);
            }
        });

        //Showing uploaded image when clicked on showUploadImgBtn
        showUploadedImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate user to corresponding user activity
                startActivity(new Intent(MainActivity.this,userActivity.class ));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                assert data != null : "The image data is NULL";

                Bitmap img = (Bitmap) (data.getExtras().get("data"));

                Location location = getCurrentLocation();
                assert location != null : "Location is NULL";
                latitude = location.getLatitude();
                longitude = location.getLongitude();


                String bingMapsApiKey = "AmqwMGmPtRT0bZK_I2ww-yIDuL2WYd3YsWhjbSxYMWMqq-IJtE0ifNWMvJPJPz2S"; // Replace with your own Bing Maps API key
                String center = latitude + "," + longitude; // Coordinates for the center of the map view
                int zoom = 16; // Zoom level for the map view
                int width = 800; // Width of the map image in pixels
                int height = 560; // Height of the map image in pixels

                // Construct the URL for the static map image
                String mapUrl = "https://dev.virtualearth.net/REST/v1/Imagery/Map/Road/" + center + "/" + zoom + "?mapSize=" + width + "," + height + "&pushpin=" + center + "&key=" + bingMapsApiKey;

                //calling getLocationName() to get the name of location
                String locationName = getlocationName(latitude, longitude);

                // Set the latitude and longitude text
                latLongTextView.setText(String.format("Location: %s\nLatitude: %s, Longitude: %s", locationName, latitude, longitude));

                // Use Picasso library to load the map image
                Picasso.get().load(mapUrl).into(mapView);

                imgCamera.setImageBitmap(img);
                imgdata = data;

                //if data enabled then upload otherwise save offline
                if (!isDataEnabled()) {
                    //Ask to access storage permission
                    requestStoragePermission();
                    // Store the image locally
                    saveImageOffline(img);
//                    Toast.makeText(this, "Saving offline...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Asking for Location permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, REQUEST_STORAGE_PERMISSION);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        }
    }

    //asking the user for location permission
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

    //getting location from android LocationManager class
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

        uploadToFirebase(bb);
    }



//    private void uploadToFirebase(byte[] bb,String locStr)
//    {
//        String filename = "IMG";
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//            filename = dateFormat.format(new Date());
//
//        }
//
//        ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setTitle("Uploading File...");
//        dialog.show();
//
//        StorageReference riversRef = storagereference.child("images/"+filename+".jpg");
//        StorageReference locRef = storagereference.child("Location_Info/"+filename+".txt");
//
//        byte[] textBytes = locStr.getBytes();
//
//        locRef.putBytes(textBytes);
//
//        riversRef.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        dialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
//                    }
//                })
//
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        double percent = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                        dialog.setMessage("Uploaded :" + (int) percent + "%");
//                    }
//                })
//
//
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Upload failed
//                        Toast.makeText(MainActivity.this, "Failed To Upload", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    //function for reverseGeocoding using geocoordinates.
    private String getlocationName(double latitude,double longitude)
    {
        String cityName = null;
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    if(address.getLocality() != null){
                        cityName = address.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }
    //Function to check weather internet is on or off
    private boolean isDataEnabled() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    //this method will save the image (if internet is off) in folder snapmap/img/ with name as Image_<systemtimeinmillisec>.jpg
    private void saveImageOffline(Bitmap bitmap){

        OutputStream fos;

        try{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

                ContentResolver resolver = getContentResolver();
                ContentValues contentValues =  new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + System.currentTimeMillis() + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "snapmap/img/");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception e){

            Toast.makeText(this, "Image not saved \n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }



    private void uploadToFirebase(byte[] bb)
    {
        String filename = "IMG";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            filename = dateFormat.format(new Date());

        }

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading File...");
        dialog.show();

        StorageReference riversRef = storagereference.child("images/"+filename+".jpg");

        String finalFilename = filename;
        riversRef.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        //new code starts here
                        String imageUrl;
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                Uri downloadUrl = uri;
                                String imageUrl = uri.toString();
                                //Do what you want with the url
                                // generating upload info and setting it starts here
                                //generating current time
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd/MM/yy  HH:mm:ss",Locale.US);
                                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                                String timeOfUpload = dateFormat.format(new Date());
                                uploadInfo imageUploadInfo = new uploadInfo(latitude, longitude,timeOfUpload, imageUrl);
//                                getting the user id of current login user
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference.child(userId).child("Uploaded Images").child(finalFilename).setValue(imageUploadInfo);
                                // generating upload info and setting it ends here
                            }
                            //new code ends here

                        });
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


    public static class uploadInfo {

        public double latitude;
        public double longitude;
        public String timeOfUpload;
        public String imageURL;
        public uploadInfo(){}

        public uploadInfo(double latitude,double longitude,String timeOfUpload, String url) {
            this.timeOfUpload = timeOfUpload;
            this.imageURL = url;
            this.latitude=latitude;
            this.longitude=longitude;
        }

//        public String getUserId() {
//            return userId;
//        }
        public double getLatitude() {
            return latitude;
        }
        public double getLongitude() {
            return longitude;
        }
        public String getImageURL() {
            return imageURL;
        }
        public String getTimeOfUpload() {
            return timeOfUpload;
        }
    }



}

