package com.example.learncodetask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Splash_Activity extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;
	ImageView img;
	TextView txt1;
	TextView txt2;
	private static final int REQUEST_WRITE_PERMISSION = 786;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		Animation slide_up = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slideup);

		// Start animation

		ImageView imageView = (ImageView) findViewById(R.id.splash_img);

		imageView.setImageResource(R.drawable.logo);
		imageView.startAnimation(slide_up);
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt1.startAnimation(slide_up);
		txt2.startAnimation(slide_up);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity

				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);

			}
		}, SPLASH_TIME_OUT);
	}

}