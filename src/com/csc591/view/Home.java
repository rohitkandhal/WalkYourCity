package com.csc591.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
		
		// Uses below to Remove notification bar but not required in our application. Helpful in games
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getCurrentDeviceLocation();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);

		//ViewServer.get(this).addWindow(this);
	}
	
	private LocationManager myLocationManager;
	private MyLocationListener myLocationListener;
	
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
	
	//public void onListItemClickHandler_OLD_NOT_WORKING(String text) {
		
		/** Getting the fragment transaction object, which can be used to add, remove or replace a fragment */
		//FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		
		/** Getting the existing detailed fragment object, if it already exists. The fragment object is retrieved by its tag name*Frag*
		* Getting the orientation ( Landscape or Portrait ) of the screen */
		//int orientation = getResources().getConfiguration().orientation;
		
		/** Landscape Mode */
		//if(orientation == Configuration.ORIENTATION_PORTRAIT ){
			/** Getting the fragment manager for fragment related operations */
			//FragmentManager fragmentManager = getFragmentManager();
			//Fragment prevFrag = fragmentManager.findFragmentByTag("com.csc591.view.FragmentDestinations");
			
			// Remove the existing detailed fragment object if it exists */
		//	if(prevFrag!=null)
		//		fragmentTransaction.remove(prevFrag);
		
			// Instantiating the fragment FragmnetDirection */
			//FragmentDirection fragment = new FragmentDirection();
		
			// Creating a bundle object to pass the data(the clicked item's position) from the activity to the fragment */
			//Bundle b = new Bundle();
		
			// Setting the data to the bundle object */
			//b.putInt("position", position);
		
			// Setting the bundle object to the fragment */
			//fragment.setArguments(b);
		
			// Adding the fragment to the fragment transaction */
			//fragmentTransaction.add(R.id.fragmentDirection, fragment);

		//	fragmentTransaction.replace(R.id.fragmentDestinations,fragment);
			//getFragmentManager().beginTransaction().add(R.id.fragmentDirection, fragment).commit();
			// Adding this transaction to backstack */
		//	fragmentTransaction.addToBackStack(null);
		
			// Making this transaction in effect */
			//fragmentTransaction.commit();
		        
	//	}
		
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
