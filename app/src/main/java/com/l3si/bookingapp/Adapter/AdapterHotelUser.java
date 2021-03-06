package com.l3si.bookingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.l3si.bookingapp.Filter.FilterHotelUser;
import com.l3si.bookingapp.Model.ModelHotel;
import com.l3si.bookingapp.MyApplication;
import com.l3si.bookingapp.activity.HotelDetailActivity;
import com.l3si.bookingapp.databinding.RowHotelUserBinding;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHotelUser extends RecyclerView.Adapter<AdapterHotelUser.HolderHotelUser> implements Filterable {
    private Context context;
    public ArrayList<ModelHotel> hotelArrayList,filterList,hotelrecomendedArrayList;
    private FilterHotelUser filter;
    //Animation for image buttons
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private ArrayList prodArrayList= null;
    private RowHotelUserBinding binding;
    private static final String TAG = "ADAPTER_HOTEL_USER_TAG";
    public AdapterHotelUser(Context context, ArrayList<ModelHotel> hotelArrayList ) {
        this.context = context;
        this.hotelArrayList = hotelArrayList;
        this.filterList = hotelArrayList;
    }

    @NonNull
    @Override
    public HolderHotelUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // bind the view
        binding = RowHotelUserBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderHotelUser(binding.getRoot());
    }

    @RequiresApi(api = 31)
    @Override
    public void onBindViewHolder(@NonNull HolderHotelUser holder, int position) {
        // get data , set data , handle click ext
        // get Data
        ModelHotel model = hotelArrayList.get(position);
        String hotelId =model.getId();
       // Log.e("hotelId", " hotelId " +hotelId);
        String etSource = model.getLocation();
        double lat1 = model.getLattitude();
        double long1 = model.getLongitude();
        double distance = model.getDistance();
       // String rating = model.getRating();
        String categoryId =model.getCategoryId();
        String hotelView = model.getUrl();
        String title = model.getTitle();
        String description = model.getDescription();
        String price = model.getPrice();
        String avgrating = model.getReview();
        long timestamp = model.getTimetamp();
        // convert time
        //set data
     //   loadReviews(model,holder);
        holder.titleTv.setText(title);
       // holder.ratingBar.setText(rating);
        holder.ratingBar.setText(avgrating);
        holder.descriptionTv.setText(description);
        holder.priceTv.setText(price+" DA");
        //holder.distance.setText(distance+" km du centre ville");
        if(hotelArrayList.get(position).getUrl() != null){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                }
            },1000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(context).load(hotelArrayList.get(position).getUrl()).into(holder.hotelView);
                }
            },1500);

        }

        MyApplication.laodCategory(""+categoryId,holder.categoryTv);
        MyApplication.getRating(""+hotelId,binding.ratingBar,binding.ratingTil);
        MyApplication.laodlocation(""+hotelId,holder.location);

        Log.e("ratingBar", " ratingBar " +avgrating);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // MyApplication.addLocation(context,"","");
                Intent intent = new Intent(context, HotelDetailActivity.class);
                intent.putExtra("etSource",etSource);
                intent.putExtra("lat1",lat1);
                intent.putExtra("long1",long1);
                intent.putExtra("hotelId",hotelId);
                intent.putExtra("distance",distance);
              //  intents.putExtra("distance",distance);
                intent.putExtra("image",hotelArrayList.get(position).getUrl());
                context.startActivity(intent);
                //Animation on click
                v.startAnimation(buttonClick);


            }
        });
    }


    @Override
    public int getItemCount() {
        return hotelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null ){
            filter = new FilterHotelUser(filterList , this);
        }
        return filter;
    }

    class HolderHotelUser extends RecyclerView.ViewHolder{
        ImageView hotelView;
        ProgressBar progressBar;
        RatingBar ratingTil;

        TextView titleTv,descriptionTv,priceTv,categoryTv,rateTv,location,ratingBar;
        public HolderHotelUser(@NonNull View itemView) {
            super(itemView);
            hotelView = binding.hotelView;
            progressBar = binding.progressBar;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            priceTv = binding.priceTv;
            categoryTv = binding.categoryTv;
            rateTv = binding.rateTv;
            ratingBar = binding.ratingBar;
            location = binding.location;
            ratingTil = binding.ratingTil;
        }

    }
}
