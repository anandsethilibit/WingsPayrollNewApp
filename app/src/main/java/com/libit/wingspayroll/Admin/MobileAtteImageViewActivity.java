package com.libit.wingspayroll.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.libit.wingspayroll.R;

public class MobileAtteImageViewActivity extends AppCompatActivity {
    android.widget.ImageView ImageView;
    String image;
    String ImageRegid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_atte_image_view);

        ImageView = findViewById(R.id.ImageView);
        Intent intent = getIntent();
        image = intent.getStringExtra("Image");
        ImageRegid = intent.getStringExtra("ImageRegid");


        Glide.with(getApplicationContext())
                .load(image)
                .placeholder(R.drawable.demoimage)
                .error(R.drawable.demoimage)
                .fitCenter()
                .into(ImageView);

    }
}