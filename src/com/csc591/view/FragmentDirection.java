package com.csc591.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.csc591.DAL.Destination;
import com.csc591.utils.JSONParser;
import com.csc591.utils.PathHelper;
import com.csc591.view.MyLocationListener.onMyLocationChangeHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

// NOTE: IMP - Ideally this class should be a fragment instead of activity but 
// because of complexity in implementation of NestedFragment (not supported in 
// pervious version) using Activity here. Though no difference on GUI.
public class FragmentDirection extends Activity implements onMyLocationChangeHandler {

	// dummy LatLng constant - Unused
	static final LatLng NORTHHILL = new LatLng(53.558,9.927); 
	
	private GoogleMap map;
	private LatLng sourceLatLng;
	private LatLng destinationLatLng;
	private LocationManager myLocationManager;
	private MyLocationListener myLocationListener;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		// Removes title Bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_directions);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentDirect)).getMap();
		
		// Get received destination object
		Intent receivedIntent = getIntent();
		Destination receivedDestination = (Destination)receivedIntent.getSerializableExtra("DestinationObject");
		
		this.destinationLatLng = new LatLng(receivedDestination.getLatitude(), receivedDestination.getLongitude());
		
		this.sourceLatLng = new LatLng(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude());
		
		// Get current location either from GPS or Network Provider
		//this.addPlaceMarkerOnMap(sourceLatLng, null);
				
		// Destination place marker
		this.addPlaceMarkerOnMap(destinationLatLng, receivedDestination);

		// Following marker type should be used in case of custom marker
		//Marker kiel = map.addMarker(new MarkerOptions().position(destinationLatLng).title(receivedDestination.getName()).snippet(receivedDestination.getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.wyc_green)));

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15));
		// Provide default zoom level (i.e. how much map area to bring in focus)
		map.animateCamera(CameraUpdateFactory.zoomTo(17), 300, null);
		
		this.myLocationListener = new MyLocationListener();
		//drawWalkRoute(dummyLatLng, destinationLatLng);
	}
	
	public void onResume()
	{
		super.onResume();
		// start receiving last known location
		this.myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
	}
	
	public void onPause()
	{
		super.onPause();
		// Stop GPS tracking when application looses visibility
		myLocationManager.removeUpdates(myLocationListener);
	}
		
	/*
	 * Add placemarks on map. If destination is null then it is assumed that it is the current source location
	 */
	private void addPlaceMarkerOnMap(LatLng locationLatLng, Destination destination)
	{
		if(map== null)
		{
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentDirect)).getMap();
		}
		
		String destinationName;
		String destinationDescription;
		
		if(destination == null)
		{
			destinationName = "My Location";
			destinationDescription = "You are here!!";
		}
		else
		{
			destinationName = destination.getName();
			destinationDescription = destination.getDescription();
		}
		map.addMarker(new MarkerOptions().position(locationLatLng).title(destinationName).snippet(destinationDescription));
	}
	
	private Location getCurrentLocation()
	{
		this.myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		Location locationGPS = this.myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location locationNet = this.myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
	    long GPSLocationTime = 0;
	    if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

	    long NetLocationTime = 0;

	    if (null != locationNet) {
	        NetLocationTime = locationNet.getTime();
	    }

	    if ( 0 < GPSLocationTime - NetLocationTime ) {
	        return locationGPS;
	    }
	    return locationNet;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.csc591.view.MyLocationListener.onMyLocationChangeHandler#onMyLocationChanged(android.location.Location)
	 * On location change update the 
	 */
	@Override
	public void onMyLocationChanged(Location location) {
		this.addPlaceMarkerOnMap(new LatLng(location.getLatitude(), location.getLongitude()), null);
	}

	
	
}