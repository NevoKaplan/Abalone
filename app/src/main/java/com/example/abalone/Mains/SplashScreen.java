package com.example.abalone.Mains;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abalone.R;

public class SplashScreen extends AppCompatActivity {

    View first, second;
    ImageView circle;
    TextView logo;
    Animation topAnimation, bottomAnimation, zoomAnimation;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, ChooseLayout.class);
                startActivity(intent);
                finish();
            }

        }, 3000);
    }

    private void init() {
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom);

        circle = findViewById(R.id.circle);
        circle.setAnimation(zoomAnimation);
    }
}