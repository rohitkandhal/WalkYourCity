package com.csc591.view;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener
{
	public interface ILocationChangeNotifier{
		public void onMyLocationChanged(Location location);		
	}
	
	public static ArrayList<ILocationChangeNotifier> listenerCollection;
	
    private static double latitude =0.0;
    private static double longitude = 0.0;
    private static double altitude = 0.0; 
    private static double speed = 0.0;
    private static MyLocationListener myLocationListener = new MyLocationListener();
    
    public static double getLat()
    {
        return latitude;
    }

    public static double getLon() 
    {
        return longitude;
    }

    public static double getAlt()
    {
        return altitude;
    }

    public static double getSpeed()
    {
        return speed;
    }
    
    public static MyLocationListener getInstance()
    {
    	if(myLocationListener == null)
    	{
    		myLocationListener = new MyLocationListener();
    	}
    	return myLocationListener;    	
    }
    
    public void addListener(ILocationChangeNotifier listener)
    {
    	listenerCollection = new ArrayList<MyLocationListener.ILocationChangeNotifier>();
    	listenerCollection.add(listener);    	
    }
    
    public void removeListener(ILocationChangeNotifier listener)
    {
    	if(listenerCollection.contains(listener))
    	{
    		listenerCollection.remove(listener);
    	}
    }
    
    @Override
    public void onLocationChanged(Location location) 
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        speed = location.getSpeed(); 
        
        //for(ILocationChangeNotifier listner: listenerCollection)
        //{
        	//listner.onMyLocationChanged(location);
        //}
    }

    @Override
    public void onProviderDisabled(String provider) {
    	//Toast.makeText(, "Device GPS is disabled",Toast.LENGTH_SHORT ).show();
    }
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}