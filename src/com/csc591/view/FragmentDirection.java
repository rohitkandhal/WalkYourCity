package com.csc591.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.csc591.DAL.Destination;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// NOTE: IMP - Ideally this class should be a fragment instead of activity but 
// because of complexity in implementation of NestedFragment (not supported in 
// pervious version) using Activity here. Though no difference on GUI.
public class FragmentDirection extends Activity {

	// dummy LatLng constant - Unused
	static final LatLng NORTHHILL = new LatLng(53.558,9.927); 
	
	private GoogleMap map;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		// Removes title Bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_directions);
		
		Intent receivedIntent = getIntent();
		Destination receivedDestination = (Destination)receivedIntent.getSerializableExtra("DestinationObject");
		
		LatLng destinationLatLng = new LatLng(receivedDestination.getLatitude(), receivedDestination.getLongitude());
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentDirect)).getMap();
		
		Marker destinationMarker = map.addMarker(new MarkerOptions().position(destinationLatLng).title(receivedDestination.getName()).snippet(receivedDestination.getDescription()));

		// Following marker type should be used in case of custom marker
		//Marker kiel = map.addMarker(new MarkerOptions().position(destinationLatLng).title(receivedDestination.getName()).snippet(receivedDestination.getDescription()).icon(BitmapDescriptorFactory.fromResource(R.drawable.wyc_green)));

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15));
		// Provide default zoom level (i.e. how much map area to bring in focus)
		map.animateCamera(CameraUpdateFactory.zoomTo(17), 700, null);
		
	}
}