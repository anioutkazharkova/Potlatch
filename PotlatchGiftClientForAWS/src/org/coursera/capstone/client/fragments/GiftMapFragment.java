package org.coursera.capstone.client.fragments;

import org.coursera.capstone.client.GetAddressTask;
import org.coursera.capstone.client.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.Fragment;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GiftMapFragment extends SupportMapFragment  {

	private GoogleMap mMap;
	private Location mCurrentLocation;
	private double lat=1000;
	private double lng=1000;
	
	 public GiftMapFragment() {
		// TODO Auto-generated constructor stub
	}
 public GiftMapFragment(double lat,double lng)
 {
	 this.lat=lat;
	 this.lng=lng;
 }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.map_fragment_layout,null);
		setUpMapIfNeeded();
		return view;
	}
	
	private void setUpMapIfNeeded() {
        if (mMap == null) {
           SupportMapFragment fragm = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
           if (fragm!=null)
        	   mMap=fragm.getMap();
          
            if (mMap != null) {
            	
                setUpMap();
            }
        }
    }
	@Override
	public void onResume() {
		// TODO Auto-generated method stub		
		super.onResume();
		setUpMapIfNeeded();
	}
	private void setUpMap() {	        
	       
		if (lat!=1000&&lng!=1000)
		{
        mCurrentLocation=new Location(LocationManager.NETWORK_PROVIDER);
        mCurrentLocation.setLatitude(lat);
        mCurrentLocation.setLongitude(lng);
		}
        if (mCurrentLocation!=null)
        {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 16));
        mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))); 
   // new GetAddressTask(this,tvAddress).execute(mCurrentLocation);	      
		
        }
	}
}
