package com.example.learncodetask;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends Activity {

	private String TAG = MainActivity.class.getSimpleName();

	private ProgressDialog pDialog;
	private ListView lv;
	private static final int REQUEST_WRITE_PERMISSION = 786;
	ImageView ad;
	Animation animBlink;
	// URL to get contacts JSON
	private static String url = "https://learncodeonline.in/api/android/datastructure.json";

	public static ArrayList<HashMap<String, String>> questionslist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		requestPermission();
		animBlink = AnimationUtils.loadAnimation(this, R.anim.blink);
		questionslist = new ArrayList<HashMap<String, String>>();
		ad = (ImageView) findViewById(R.id.adView);
		lv = (ListView) findViewById(R.id.list);
		ad.startAnimation(animBlink);
		FloatingActionsMenu rightLabels = (FloatingActionsMenu) findViewById(R.id.right_labels);
		FloatingActionButton Share = new FloatingActionButton(this);
		Share.setTitle("Share");
		rightLabels.addButton(Share);
		Share.setIcon(R.drawable.share);
		Share.setSize(FloatingActionButton.SIZE_MINI);

		FloatingActionButton Contact = new FloatingActionButton(this);
		Contact.setTitle("Contact");
		Contact.setSize(FloatingActionButton.SIZE_MINI);
		rightLabels.addButton(Contact);
		rightLabels.removeButton(Contact);
		rightLabels.addButton(Contact);
		Contact.setIcon(R.drawable.contact);

		FloatingActionButton More = new FloatingActionButton(this);
		More.setTitle("More");
		More.setSize(FloatingActionButton.SIZE_MINI);
		rightLabels.addButton(More);
		rightLabels.removeButton(More);
		rightLabels.addButton(More);
		More.setIcon(R.drawable.more);
		Share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					Uri imageUri = null;
					try {
						imageUri = Uri.parse(MediaStore.Images.Media
								.insertImage(getContentResolver(),
										BitmapFactory.decodeResource(
												getResources(),
												R.drawable.shareimg), null,
										null));
					} catch (NullPointerException e) {
					}

					String text = "Download this App for top interview questions in Data Structures"
							+ "https://play.google.com/store/apps/details?id="
							+ getPackageName();
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.putExtra(Intent.EXTRA_TEXT, text);
					shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
					shareIntent.setType("image/*");
					shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					startActivity(Intent.createChooser(shareIntent,
							"share using...."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getApplicationContext(), "failed sharing",
							Toast.LENGTH_LONG).show();
				}
			}

		});
		Contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mailto = "mailto:hitesh@learncodeonline.in" + "?cc="
						+ "hitesh@learncodeonline.in" + "&subject="
						+ Uri.encode("Data Structures queires") + "&body="
						+ Uri.encode("Enter your message here........");

				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setData(Uri.parse(mailto));

				try {
					startActivity(emailIntent);
				} catch (ActivityNotFoundException e) {
					// TODO: Handle case where no email app is available
				}

			}
		});
		More.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://learncodeonline.in/"));
				startActivity(browserIntent);
			}
		});
		ad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://learncodeonline.in/"));
				startActivity(browserIntent);
			}
		});
		new GetContacts().execute();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(),
						DetailAnswerActivity.class);
				in.putExtra("position", position);
				startActivity(in);
			}
		});
	}

	/**
	 * Async task class to get json by making HTTP call
	 */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			HttpHandler sh = new HttpHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url);

			Log.e(TAG, "Response from url: " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					JSONArray questions = jsonObj.getJSONArray("questions");

					// looping through All Contacts
					for (int i = 0; i < questions.length(); i++) {
						JSONObject c = questions.getJSONObject(i);

						String id = c.getString("question");
						String name = c.getString("Answer");

						HashMap<String, String> question = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						question.put("question", id);
						question.put("Answer", name);

						// adding contact to contact list
						questionslist.add(question);
					}
				} catch (final JSONException e) {
					Log.e(TAG, "Json parsing error: " + e.getMessage());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Json parsing error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					});

				}
			} else {
				Log.e(TAG, "Couldn't get json from server.");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								getApplicationContext(),
								"Couldn't get json from server. Check LogCat for possible errors!",
								Toast.LENGTH_LONG).show();
					}
				});

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(MainActivity.this,
					questionslist, R.layout.list_item,
					new String[] { "question" }, new int[] { R.id.name });

			lv.setAdapter(adapter);
		}

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_WRITE_PERMISSION
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

		} else {
			Toast.makeText(getApplicationContext(), "permission denined", 5000)
					.show();
			finishAffinity();
		}
	}

	@TargetApi(23)
	private void requestPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(
					new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE },
					REQUEST_WRITE_PERMISSION);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finishAffinity();
	}
}