package com.example.darurat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class TempActivity extends Activity {

	private ImageButton button1;
	PolisiDatabase policedb;
	
	Toast clickToast;
	Toast longClickToast;
	
	private enum TUTORIAL_STATE{PREP, AFTER_CLICK, DONE}
	
	TUTORIAL_STATE state = TUTORIAL_STATE.PREP;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.d("Button", "udah lewat sini");
	        setContentView(R.layout.activity_darurat);
	        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
	        
	        clickToast = Toast.makeText(this, "Click to make a call", Toast.LENGTH_LONG);
	        longClickToast = Toast.makeText(this, "Long Click to View Map", Toast.LENGTH_LONG);
	        showClickToast();
	        
	        
	        button1 = (ImageButton) findViewById(R.id.polisi);
	        
	        policedb = new PolisiDatabase(this);
			policedb.loadContent();
			
	        
	        button1.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					
					if (state == TUTORIAL_STATE.AFTER_CLICK){
						
						state = TUTORIAL_STATE.DONE;
						
						Toast.makeText(TempActivity.this, "Good. You've done the tutorial.", Toast.LENGTH_LONG).show();
						
						
						// TODO Auto-generated method stub
						Log.d("Hold", "udah lewat sini");
						GPSTracker gps = new GPSTracker(getApplicationContext());
						double lat = gps.getLatitude();
						double lng = gps.getLongitude();
						
						//Modif Vai
						//Intent mapIntent = new Intent(getApplicationContext(),MapViewActivity.class);
						Intent mapIntent = new Intent(getApplicationContext(),Darurat.class);
						
						//end of modif
						Bundle b = new Bundle();
						b.putDouble("Latitude", lat);
						b.putDouble("Longitude", lng);
						b.putString("Perusahaan", "Polisi");
						mapIntent.putExtras(b);
						startActivity(mapIntent);
					}
					
					return true;
				}
			});
	        
	        button1.setOnClickListener(new OnClickListener() { 
				@Override
				public void onClick(View arg0) {
					
					if (state == TUTORIAL_STATE.PREP){
						state = TUTORIAL_STATE.AFTER_CLICK;
						
						Toast.makeText(TempActivity.this, "Good. Let's just cancel for now. Choose anything.", Toast.LENGTH_LONG).show();
						
						GPSTracker gps = new GPSTracker(getApplicationContext());
						double lat = gps.getLatitude();
						double lng = gps.getLongitude();
						String no_telp = policedb.getNoTelp(lat, lng);
						String nama_perusahaan = policedb.getNamaPerusahaan(lat, lng);
						final Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:"+no_telp));
							
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								TempActivity.this);
				 
							// set title
						alertDialogBuilder.setTitle("Are you sure to call "+nama_perusahaan+"?");
				 
							// set dialog message
						alertDialogBuilder
								.setCancelable(false)
								.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
										//startActivity(callIntent);
										showLongClickToast();
									}
								  })
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
										showLongClickToast();
									}
								});
				 
								// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
						alertDialog.show();
						
						
					}
					
				}
					  
			});
	      
	 }
	 
	 private void showClickToast(){
		 clickToast.show();
	 }
	 
	 private void showLongClickToast(){
		 longClickToast.show();
	 }
	 
}
