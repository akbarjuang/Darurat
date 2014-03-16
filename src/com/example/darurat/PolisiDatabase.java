package com.example.darurat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import com.google.android.gms.maps.model.LatLng;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.method.ArrowKeyMovementMethod;
import android.util.FloatMath;
import android.util.Log;

public class PolisiDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "PoliceDB";

	// Monster table name
	private static final String TABLE = "tables";

	// Monster Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_JENIS = "jenis";
	private static final String KEY_NAMA = "nama";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_NO_TELP = "no_telp";
	
	public PolisiDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_JENIS + " TEXT,"
				+ KEY_NAMA + " TEXT,"  + KEY_LATITUDE + " DOUBLE,"+ KEY_LONGITUDE + " DOUBLE," + KEY_NO_TELP + " TEXT " +");";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);

		// Create tables again
		onCreate(db);
	}
	
	public void loadContent()
	{
		onUpgrade(this.getReadableDatabase(), DATABASE_VERSION, DATABASE_VERSION);
		//id mulai dari 0
		//contoh : addData(0,Polisi,-6.8989898,182.0840840,085723657);
		addData(0, "Polisi", "Polsek Coblong", -6.887668, 107.62356,  "0222502532");
		addData(1, "Polisi", "Polsekta Cidadap", -6.847449, 107.599255, "0222013521");
		addData(2, "Polisi", "Polresta Bandung Tengah", -6.915265, 107.631114, "0227271115");
		addData(3, "SAR", "Kantor SAR Bandung", -6.966349, 107.824372, "0227780437");
		addData(4, "SAR", "Kantor SAR Jakarta", -6.12641,106.654227,"02155051111");
		addData(5, "RS", "RSUP Dr. Hasan Sadikin",  -6.896785,107.597999 ,"0222034953");
		addData(6, "RS", "RS Santo Borromeus", -6.894637,107.613624,"0222552080");
		addData(7, "PK", "Dinas Kebakaran Kota Bandung",-6.915907, 107.634507,"0227207113");
		addData(8, "PK", "UPTD PK Soreang",-7.026198,107.524936 ,"0225891113");
	}
	
	void addData(int id, String jenis, String nama, double latitude, double longitude, String no_telp ) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, id); 
		values.put(KEY_JENIS, jenis);
		values.put(KEY_NAMA, nama);
		values.put(KEY_LATITUDE, latitude);
		values.put(KEY_LONGITUDE, longitude); 
		values.put(KEY_NO_TELP, no_telp);  

		
		// Inserting Row
		db.insert(TABLE, null, values);
		db.close(); // Closing database connection
	}
	
	String getNoTelp(double curlat, double curlong) 
	{
		Log.d("get nomor telp", "belom lewat sini");
		SQLiteDatabase db = this.getReadableDatabase();
		String no_telp = "085725706477"; //default
		
		int count = getDataCount();
		double minJarak = 10000.00;
		Log.d("get nomor telp", "belom lewat sini");
		for(int i = 0;i < count; i++){
			Log.d("get nomor telp", "masih proses");
			Cursor cursor = db.query(TABLE, new String[] { KEY_ID,
					KEY_JENIS, KEY_NAMA, KEY_LATITUDE, KEY_LONGITUDE, KEY_NO_TELP }, KEY_ID + "=?",
					new String[] { String.valueOf(i) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			Log.d("nomor telp", cursor.getString(1));
			if (cursor.getString(1).equals("Polisi")){
				double lat = Double.parseDouble(cursor.getString(3));
				double lon = Double.parseDouble(cursor.getString(4));
				Log.d("Koordinat",lat+" "+lon+" "+curlat+" "+curlong);
				double jarak = distFrom(lat, lon, curlat, curlong);
				Log.d("jarak", "jarak = "+jarak);
				if (jarak < minJarak){
					Log.d("yg disimpen nomor telp", cursor.getString(5));
					no_telp = cursor.getString(5);
					minJarak = jarak;
				}
			}
		}
		// return contact
		Log.d("get nomor telp", "udah lewat sini");
		return no_telp;
	}
	
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (dist * meterConversion);
	    }
	
	public int getDataCount() {
		String countQuery = "SELECT  count(*) FROM " + TABLE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();

		// return count
		return cursor.getInt(0);
	}
	
	ArrayList<LatLng> getCoordinate() 
	{
		Log.d("get perusahaan", "belom lewat sini");
		ArrayList<LatLng> listCoordinate = new ArrayList<LatLng>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		int count = getDataCount();
		for(int i = 0;i < count; i++){
			Log.d("get perusahaan", "masih proses");
			Cursor cursor = db.query(TABLE, new String[] { KEY_ID,
					KEY_JENIS, KEY_NAMA, KEY_LATITUDE, KEY_LONGITUDE, KEY_NO_TELP }, KEY_ID + "=?",
					new String[] { String.valueOf(i) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			if (cursor.getString(1).equals("Polisi")){
				double lat = Double.parseDouble(cursor.getString(3));
				double lon = Double.parseDouble(cursor.getString(4));			
				listCoordinate.add(new LatLng(lat, lon));
				Log.d("get perusahaan", lat+" "+lon);
			}
		}
		// return contact
		Log.d("get perusahaan", "selesai");
		return listCoordinate;
	}

	public String getNamaPerusahaan(double curlat, double curlong) {
		Log.d("get nama perusahaan", "belom lewat sini");
		SQLiteDatabase db = this.getReadableDatabase();
		String nama_perusahaan = "Alamat Kantor"; //default
		
		int count = getDataCount();
		double minJarak = 10000.00;
		Log.d("get nama perusahaan", "belom lewat sini");
		for(int i = 0;i < count; i++){
			Log.d("get nama perusahaan", "masih proses");
			Cursor cursor = db.query(TABLE, new String[] { KEY_ID,
					KEY_JENIS, KEY_NAMA, KEY_LATITUDE, KEY_LONGITUDE, KEY_NO_TELP }, KEY_ID + "=?",
					new String[] { String.valueOf(i) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			Log.d("nomor telp", cursor.getString(1));
			if (cursor.getString(1).equals("Polisi")){
				double lat = Double.parseDouble(cursor.getString(3));
				double lon = Double.parseDouble(cursor.getString(4));
				Log.d("Koordinat",lat+" "+lon+" "+curlat+" "+curlong);
				double jarak = distFrom(lat, lon, curlat, curlong);
				Log.d("jarak", "jarak = "+jarak);
				if (jarak < minJarak){
					Log.d("yg disimpen nomor telp", cursor.getString(2));
					nama_perusahaan = cursor.getString(2);
					minJarak = jarak;
				}
			}
		}
		// return contact
		Log.d("get nama perusahaan", "udah lewat sini");
		return nama_perusahaan;
	}
	
	
}
