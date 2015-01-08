package org.coursera.capstone.client.activities;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.coursera.capstone.client.GetAddressTask;
import org.coursera.capstone.client.R;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectLocationActivity extends FragmentActivity implements OnMapClickListener,OnClickListener {

	private GoogleMap mMap;
	private Location mCurrentLocation;
	private Location mLocation;
	
	public static String LATITUDE="latitude";
	public static String LONGITUDE="longitude";
	
	LinearLayout btnSave,btnCurrentLocation;
	TextView tvAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_location_layout);
		setTitle(getResources().getString(R.string.select_location));
		setUpMapIfNeeded();
		btnCurrentLocation=(LinearLayout) findViewById(R.id.layoutCurrentLocation);
		btnCurrentLocation.setOnClickListener(this);
		tvAddress=(TextView) findViewById(R.id.tvAddress);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpMapIfNeeded();
	}
	
	 private void setUpMapIfNeeded() {
	        if (mMap == null) {
	           SupportMapFragment fragm = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
	           if (fragm!=null)
	        	   mMap=fragm.getMap();
	          
	            if (mMap != null) {
	            	mMap.setOnMapClickListener(this);
	                setUpMap();
	            }
	        }
	    }
	 
	 private void setUpMap() {	        
	       
	        mCurrentLocation=getCurrentLocation();
	        if (mCurrentLocation!=null)
	        {
	        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 16));
	        mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))); 
	    new GetAddressTask(this,tvAddress).execute(mCurrentLocation);	      
			
	        }
	    }
	 
	 private Location getCurrentLocation()
	 {
		 LocationManager locManager =(LocationManager) this.getSystemService(LOCATION_SERVICE);
		 return locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		 
	 }
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	public void onMapClick(LatLng newLocation) {
		// TODO Auto-generated method stub
		mLocation=new Location(LocationManager.NETWORK_PROVIDER);
		mLocation.setLatitude(newLocation.latitude);
		mLocation.setLongitude(newLocation.longitude);
		clearMap();
		mMap.addMarker(new MarkerOptions().position(newLocation));
		new GetAddressTask(this,tvAddress).execute(mLocation);
		
		
	}
	private void clearMap()
	{
		mMap.clear();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.getMenuInflater().inflate(R.menu.select_loc_menu, menu);
		MenuItem item=menu.findItem(R.id.btnSave);
		View view =item.getActionView();
		btnSave=(LinearLayout) view.findViewById(R.id.btnSaveGift);
		btnSave.setOnClickListener(this);
		
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnSaveGift:
		{
			Intent intent=new Intent();
			Location location;
			if (mLocation!=null)
			{
				location=mLocation;
				
			}
			else
			{
				location=mCurrentLocation;
			}
			if (location!=null)
			{
				intent.putExtra(LATITUDE, location.getLatitude());
				intent.putExtra(LONGITUDE, location.getLongitude());
			}
			setResult(RESULT_OK,intent);
			finish();
			
			
		}
		break;
		case R.id.layoutCurrentLocation:
		{
			mLocation=null;
			clearMap();
			setUpMap();
		}
		break;
		}
		
	}
	
}
