package com.example.learncodetask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailAnswerActivity extends Activity {

	TextView question_txt;
	TextView Answer_txt;
	ArrayList<HashMap<String, String>> questionlist;
	Intent mIntent;
	int position;
	ImageView ad;

	Animation animBlink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.answer_detail_activity);
		mIntent = getIntent();
		position = mIntent.getIntExtra("position", 0);
		ad = (ImageView) findViewById(R.id.adView);
		question_txt = (TextView) findViewById(R.id.question_txt);
		animBlink = AnimationUtils.loadAnimation(this, R.anim.blink);

		// set animation listener

		Answer_txt = (TextView) findViewById(R.id.Answer_txt);
		questionlist = MainActivity.questionslist;

		question_txt.setText(questionlist.get(position).get("question"));

		Answer_txt.setText(questionlist.get(position).get("Answer"));
		ad.startAnimation(animBlink);
		ad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://learncodeonline.in/"));
				startActivity(browserIntent);
			}
		});
	}
}
