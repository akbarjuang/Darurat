package com.example.darurat;

import java.util.ArrayList;

import com.example.darurat.R.layout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.maps.MapActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MapViewActivity extends FragmentActivity {

	private GoogleMap googleMap;
	PolisiDatabase polisidb = new PolisiDatabase(this);
	ArrayList<Marker> listMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);

		listMarker = new ArrayList<Marker>();
		
		Bundle bundel = getIntent().getExtras();
		double latitude = bundel.getDouble("Latitude");
		double longitude = bundel.getDouble("Longitude");
		String perusahaan = bundel.getString("Perusahaan");
		LatLng CurrentLocation = new LatLng(latitude, longitude);
		polisidb.loadContent();
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = mapFragment.getMap();
		googleMap.setMyLocationEnabled(false);
		
		if (googleMap!=null){
		      Marker current = googleMap.addMarker(new MarkerOptions().position(CurrentLocation));
		      
		      if (perusahaan.equals("Polisi")){
		    	  ArrayList<LatLng> listPolisi = polisidb.getCoordinate();
		    	  for(int i=0;i<listPolisi.size();i++){
		    		  Marker polisi = googleMap.addMarker(new MarkerOptions().position(listPolisi.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		    		  listMarker.add(polisi);
		    	  }
		      }
		}
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentLocation, 15));

		// Zoom in, animating the camera.
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_view, menu);
		return true;
	}
	
}
