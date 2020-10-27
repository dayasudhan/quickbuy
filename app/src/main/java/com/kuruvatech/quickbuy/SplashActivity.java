package com.kuruvatech.quickbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.splunk.mint.Mint;

public class SplashActivity extends Activity implements Animation.AnimationListener {


    ImageView splashImage,splashOneLogo;


    // Animation
    Animation animBike,animLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, "49d903c2");
        setContentView(R.layout.activity_zoom_in);

       // imgPoster = (ImageView) findViewById(R.id.imgLogo);
        splashImage = (ImageView) findViewById(R.id.food_deliver_bike);
        splashOneLogo=(ImageView) findViewById(R.id.splashlogo);

        // load the animation
        animBike = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move);
        animLogo= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        // set animation listener
        animBike.setAnimationListener(this);
        animLogo.setAnimationListener(this);
        // button click event

        Thread timer= new Thread(){
            public void run(){
                try{
                    splashOneLogo.startAnimation(animLogo);
                    splashImage.startAnimation(animBike);
                    sleep(3000);

                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    Intent i= new Intent(SplashActivity.this,MainActivity.class);

                    startActivity(i);
                    finish();
                }

            }


        };
        timer.start();

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for zoom in animation
        if (animation == animBike) {

        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {

        // TODO Auto-generated method stub

    }
}