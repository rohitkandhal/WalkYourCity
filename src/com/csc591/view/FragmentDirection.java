package com.csc591.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

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

	static final LatLng HAMBURG = new LatLng(53.558,9.927); 
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	
	private GoogleMap map;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		// Removes title Bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_directions);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentDirect)).getMap();
		
		Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG).title("Title"));
		
		Marker kiel = map.addMarker(new MarkerOptions().position(KIEL).title("Kiel").snippet("This is cool").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
		
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 700, null);
		
	}
	
	
	/*
		public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_directions,container,false);
		
	}
	*/	
}