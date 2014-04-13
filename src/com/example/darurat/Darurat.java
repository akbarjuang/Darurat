package com.example.darurat;

import java.util.HashMap;
import java.util.Map;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


public class Darurat extends Activity {

	private ImageButton button1,button2,button3,button4;
	PolisiDatabase policedb;
	FireFighterDatabase pkdb;
	HospitalDatabase rsdb;
	SARDatabase sardb;
	
	//Analytic data
	Map<String, String> dimensions;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Parse.initialize(this, "e2S1uam9fRBp6EABlDz5VbBfddWR6J8HVd22IP4f", "2amlNKlVIBv8eDbh0uj5v1XCF2U1EvJpk4UUe59C");
        ParseAnalytics.trackAppOpened(getIntent());
        
        Log.d("Button", "udah lewat sini");
        setContentView(R.layout.activity_darurat);
        
        Log.d("Button", "udah lewat sini 1");
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        Log.d("Button", "udah lewat sini 2");
        
        button1 = (ImageButton) findViewById(R.id.polisi);
        button2 = (ImageButton) findViewById(R.id.rs);
        button3 = (ImageButton) findViewById(R.id.sar);
        button4 = (ImageButton) findViewById(R.id.pmk);
        
		policedb = new PolisiDatabase(this);
		policedb.loadContent();
		pkdb = new FireFighterDatabase(this);
		pkdb.loadContent();
		rsdb = new HospitalDatabase(this);
		rsdb.loadContent();
		sardb = new SARDatabase(this);
		sardb.loadContent();
		Log.d("Button", "udah lewat sini");
		
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
			.getSystemService(this.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		
		button1.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Hold", "udah lewat sini");
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				
				//Modif Vai
				
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "Polisi");
				ParseAnalytics.trackEvent("onLongClick", dimensions);
			    
				
				
				//Intent mapIntent = new Intent(getApplicationContext(),MapViewActivity.class);
				Intent mapIntent = new Intent(getApplicationContext(),TempActivity.class);
				
				//end of modif
				Bundle b = new Bundle();
				b.putDouble("Latitude", lat);
				b.putDouble("Longitude", lng);
				b.putString("Perusahaan", "Polisi");
				mapIntent.putExtras(b);
				startActivity(mapIntent);
				return true;
			}
		});
		
		button1.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "Polisi");
				ParseAnalytics.trackEvent("onClick", dimensions);
			    
				
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				String no_telp = policedb.getNoTelp(lat, lng);
				String nama_perusahaan = policedb.getNamaPerusahaan(lat, lng);
				final Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+no_telp));
					
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Darurat.this);
		 
					// set title
				alertDialogBuilder.setTitle("Are you sure to call "+nama_perusahaan+"?");
		 
					// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								startActivity(callIntent);
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
				alertDialog.show();
			}
				  
		});
		
		button2.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "RS");
				ParseAnalytics.trackEvent("onClick", dimensions);
			    
				
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				String no_telp = rsdb.getNoTelp(lat, lng);
				String nama_perusahaan = rsdb.getNamaPerusahaan(lat, lng);
				final Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+no_telp));
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Darurat.this);
		 
					// set title
				alertDialogBuilder.setTitle("Are you sure to call "+nama_perusahaan+"?");
		 
					// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								startActivity(callIntent);
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
				alertDialog.show();
 
			}
 
		});
		
		button2.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Hold", "udah lewat sini");
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				//Modif Vai
				
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "RS");
				ParseAnalytics.trackEvent("onLongClick", dimensions);
			    
				
				//Intent mapIntent = new Intent(getApplicationContext(),MapViewActivity.class);
				Intent mapIntent = new Intent(getApplicationContext(),TempActivity.class);
				
				//end of modif
				Bundle b = new Bundle();
				b.putDouble("Latitude", lat);
				b.putDouble("Longitude", lng);
				b.putString("Perusahaan", "RS");
				mapIntent.putExtras(b);
				startActivity(mapIntent);
				return true;
			}
		});
		
		button3.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "SAR");
				ParseAnalytics.trackEvent("onClick", dimensions);
			    
				
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				String no_telp = sardb.getNoTelp(lat, lng);
				String nama_perusahaan = sardb.getNamaPerusahaan(lat, lng);
				final Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+no_telp));
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Darurat.this);
		 
					// set title
				alertDialogBuilder.setTitle("Are you sure to call "+nama_perusahaan+"?");
		 
					// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								startActivity(callIntent);
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
				alertDialog.show();
 
			}
 
		});
		
		button3.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Hold", "udah lewat sini");
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				
				
				//Modif Vai
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "SAR");
				ParseAnalytics.trackEvent("onLongClick", dimensions);
			    
				
				//Intent mapIntent = new Intent(getApplicationContext(),MapViewActivity.class);
				Intent mapIntent = new Intent(getApplicationContext(),TempActivity.class);
				
				//end of modif
				
				Bundle b = new Bundle();
				b.putDouble("Latitude", lat);
				b.putDouble("Longitude", lng);
				b.putString("Perusahaan", "SAR");
				mapIntent.putExtras(b);
				startActivity(mapIntent);
				return true;
			}
		});
		
		button4.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "PK");
				ParseAnalytics.trackEvent("onClick", dimensions);
			    
				
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				String no_telp = pkdb.getNoTelp(lat, lng);
				String nama_perusahaan = pkdb.getNamaPerusahaan(lat, lng);
				final Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+no_telp));
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Darurat.this);
		 
					// set title
				alertDialogBuilder.setTitle("Are you sure to call "+nama_perusahaan+"?");
		 
					// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								startActivity(callIntent);
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
				alertDialog.show();
 
			}
 
		});
		
		button4.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Hold", "udah lewat sini");
				GPSTracker gps = new GPSTracker(getApplicationContext());
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				
				
				//Modif Vai
				
				//Send analytics
				dimensions = new HashMap<String, String>();
				dimensions.put("perusahaan", "PK");
				ParseAnalytics.trackEvent("onLongClick", dimensions);
			    
				
				//Intent mapIntent = new Intent(getApplicationContext(),MapViewActivity.class);
				Intent mapIntent = new Intent(getApplicationContext(),TempActivity.class);
				
				//end of modif
				
				Bundle b = new Bundle();
				b.putDouble("Latitude", lat);
				b.putDouble("Longitude", lng);
				b.putString("Perusahaan", "PK");
				mapIntent.putExtras(b);
				startActivity(mapIntent);
				return true;
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.darurat, menu);
        return true;
    }
    
    private class PhoneCallListener extends PhoneStateListener {

		String TAG = "LOGGING PHONE CALL";

		private boolean phoneCalling = false;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(TAG, "RINGING, number: " + incomingNumber);
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(TAG, "OFFHOOK");

				phoneCalling = true;
			}

			// When the call ends launch the main activity again
			if (TelephonyManager.CALL_STATE_IDLE == state) {

				Log.i(TAG, "IDLE");

				if (phoneCalling) {

					Log.i(TAG, "restart app");

					// restart app
					Intent i = getBaseContext().getPackageManager()
							.getLaunchIntentForPackage(
									getBaseContext().getPackageName());

					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);

					phoneCalling = false;
				}

			}
		}
	}    
}
