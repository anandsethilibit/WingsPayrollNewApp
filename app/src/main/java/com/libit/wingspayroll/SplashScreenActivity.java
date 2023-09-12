package com.libit.wingspayroll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.libit.wingspayroll.Network.StaticDataHelper;


public class SplashScreenActivity extends AppCompatActivity {
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo=(ImageView) findViewById(R.id.logo);
        startAnimations();
        ShowSplashScreen();
    }


    private void startAnimations() {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        logo.startAnimation(animation);
    }


    private void ShowSplashScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (StaticDataHelper.getBooleanFromPreferences(SplashScreenActivity.this, "islogin")) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                }else if(StaticDataHelper.getBooleanFromPreferences(SplashScreenActivity.this, "isAdminlogin")){
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, Splash2Activity.class);
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                }
            }
        }, 3000);
    }
}
