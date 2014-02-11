package com.csc591.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;

import com.csc591.DAL.Destination;
import com.csc591.view.FragmentDestinations.FragmentDestinationInterface;
import com.csc591.view.FragmentFooter.FragmentFooterInterface;


public class Home extends Activity implements FragmentDestinationInterface, FragmentFooterInterface{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Removes title Bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.getCurrentDeviceLocation();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
	}
	
	private LocationManager myLocationManager;
	private MyLocationListener myLocationListener;
	
}
	
	public void getCurrentDeviceLocation()
	{
		try{
			this.myLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
			this.myLocationListener = new MyLocationListener();
			this.myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		}
		catch (Exception e) {
			// TODO: handle GPS unavailable or Wifi unavailable exception
		}
	}
	
	public void onDestroy() {
        super.onDestroy();
        //ViewServer.get(this).removeWindow(this);
        if(this.myLocationListener != null && this.myLocationManager != null)
    		myLocationManager.removeUpdates(myLocationListener);
   }

    public void onResume() {
        super.onResume();
        //ViewServer.get(this).setFocusedWindow(this);
        if(this.myLocationListener != null && this.myLocationManager != null)
        	this.myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
   }
    
    public void onPause()
    {
    	super.onPause();
    	if(this.myLocationListener != null && this.myLocationManager != null)
    		myLocationManager.removeUpdates(myLocationListener);
    }

    
	@Override
	public void onListItemClickHandler(Destination destination)
	{
		Intent myIntent = new Intent(Home.this, FragmentDirection.class);
		myIntent.putExtra("DestinationObject", destination); //Optional parameters
		Home.this.startActivity(myIntent);
	}
		
	public interface OnFooterCategorySelectionChanged{
		public void onCategoryChangeHandler(ArrayList<Integer> newFlags);
	}
	
	public void onCategoryButtonClickHandler(ArrayList<Integer> newFlags)
	{
		Fragment destinationFragment = (Fragment)getFragmentManager().findFragmentById(R.id.fragmentDestinations);
		
		if(destinationFragment instanceof OnFooterCategorySelectionChanged)
		{
			((OnFooterCategorySelectionChanged) destinationFragment).onCategoryChangeHandler(newFlags);
		}
	}

}
