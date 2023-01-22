package com.example.snapmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<UserImage> list;


    public MyAdapter(Context context, ArrayList<UserImage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        UserImage userImage = list.get(position);
        Picasso.get().load(userImage.getImageUrl()).into(holder.imageView);
        holder.latitude.setText(userImage.getLatitude());
        holder.longitude.setText(userImage.getLongitude());
        holder.timeOfUpload.setText(userImage.getTimeOfUpload());


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView latitude, longitude,timeOfUpload;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            latitude = itemView.findViewById(R.id.latitudeTextview);
            longitude = itemView.findViewById(R.id.longitudeTextview);
            timeOfUpload=itemView.findViewById(R.id.timeOfUpload);
            imageView=itemView.findViewById(R.id.uploadedImageView);
        }
    }



}