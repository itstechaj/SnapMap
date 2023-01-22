package com.example.snapmap;

public class UserImage {

    String imageURL,timeOfUpload;
    double latitude, longitude;

    private  UserImage(){}

    public UserImage(String imageUrl,String timeOfUpload, double latitude, double longitude) {
        this.timeOfUpload=timeOfUpload;
        this.imageURL = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public String getTimeOfUpload() {
        return timeOfUpload;
    }


    public void setImageUrl(String imageUrl) {
        imageUrl = imageUrl;
    }

    public String getLatitude() {
        return String.valueOf(latitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return String.valueOf(longitude);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}