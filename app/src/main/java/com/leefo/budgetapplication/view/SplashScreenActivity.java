package com.leefo.budgetapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.leefo.budgetapplication.R;

public class SplashScreenActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim,rightAnim, leftAnim;
    ImageView letterL,letterO,letterE1,letterE2,letterF;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        playStartUpSound();
        loadAnimation();

        getItemsID();

        setAnimation();

        setATimeSplashScreen();

    }

    private void setAnimation() {
//        tools.setAnimation(leftAnim);
//        safe.setAnimation(rightAnim);
        letterL.setAnimation(rightAnim);
        letterE1.setAnimation(bottomAnim);
        letterE2.setAnimation(bottomAnim);
        letterF.setAnimation(topAnim);
        letterO.setAnimation(leftAnim);
    }

    private void getItemsID() {
        letterL = findViewById(R.id.letterL);
        letterO = findViewById(R.id.letterO);
        letterE1 = findViewById(R.id.letterE1);
        letterE2 = findViewById(R.id.letterE2);
        letterF = findViewById(R.id.letterF);
//        safe = findViewById(R.id.safe);
//        tools = findViewById(R.id.tools);
//        tools = findViewById(R.id.tools);
    }

    private void loadAnimation() {
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);

    }

    private void playStartUpSound() {
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.startup_sound);
        }

        mediaPlayer.start();
    }

    private void setATimeSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this,MainActivity.class);

                startActivity(i);


                finish();

            }
        }, 5000);
    }


}