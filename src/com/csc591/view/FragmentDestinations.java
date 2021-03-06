package com.csc591.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.csc591.DAL.Destination;
import com.csc591.DAL.DestinationDataSource;
import com.csc591.utils.DurationComparator;
import com.csc591.utils.GoogleDistanceMatrixReader;
import com.csc591.view.Home.OnFooterCategorySelectionChanged;
import com.csc591.view.MyLocationListener.ILocationChangeNotifier;
import com.google.android.gms.maps.model.LatLng;

public class FragmentDestinations extends Fragment implements OnFooterCategorySelectionChanged, ILocationChangeNotifier{

	private ArrayList<Destination> allDestinations;
	private ArrayList<Destination> selectedDestinations;
	DestinationListAdapter destinationListAdapter;
	
	private DestinationDataSource dataSource;
	private LatLng currentLatLng;
	
	public interface FragmentDestinationInterface{
		public void onListItemClickHandler(Destination destination);
	}
	
	public FragmentDestinationInterface homeActivityInterface;
	public ProgressDialog progress;
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//Sets the splash screen in motion ...waits till the point where the database in completely loaded into the memory
		
		progress = ProgressDialog.show(this.getActivity(),"Loading...","Loading application View, please wait...", false, false);
		//progress = new ProgressDialog(this.getActivity(), ProgressDialog.STYLE_SPINNER);
		//progress.setMessage("Loading...");
		progress.show();
		View view = inflater.inflate(R.layout.fragment_dest_list,container,false);
		this.setUpInitialDatabase();

		return view;
	}
	

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		this.setUpDestinationList();
	}
	
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		if(activity instanceof FragmentDestinationInterface)
		{
			homeActivityInterface = (FragmentDestinationInterface)activity;
		}
	}
	
	public void onDetach()
	{
		super.onDetach();
		homeActivityInterface = null;
	}
	
	public void onResume()
	{	 	
		dataSource.open();	 	
		super.onResume();	 	
		this.addLocationListener();	 	
		this.startLocationUpdates();	 	
	}	 	

	public void onPause()	 	
	{	 	
		dataSource.close();	 	
		super.onPause();	 	
		this.removeLocationListner();	 	
		this.stopLocationUpdates();	 	
	}
	
	public void onListViewItemClick(int position)
	{
		// Bug - selected destination list have the currently displayed list item not all destination
		homeActivityInterface.onListItemClickHandler(selectedDestinations.get(position));
	}
	
	public void onListViewItemHandle(int position)
	{
		/** Getting the fragment transaction object, which can be used to add, remove or replace a fragment */
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		
		/** Getting the existing detailed fragment object, if it already exists. The fragment object is retrieved by its tag name*Frag*
		* Getting the orientation ( Landscape or Portrait ) of the screen */
		int orientation = getResources().getConfiguration().orientation;
		
		/** Landscape Mode */
		if(orientation == Configuration.ORIENTATION_PORTRAIT ){
			/** Getting the fragment manager for fragment related operations */
			FragmentManager fragmentManager = getFragmentManager();
			Fragment prevFrag = fragmentManager.findFragmentByTag("com.csc591.view.FragmentDestinations");
			
			// Remove the existing detailed fragment object if it exists */
			if(prevFrag!=null)
				fragmentTransaction.remove(prevFrag);
		
			// Instantiating the fragment FragmnetDirection */
			FragmentDirection fragment = new FragmentDirection();
		
			// Creating a bundle object to pass the data(the clicked item's position) from the activity to the fragment */
			Bundle b = new Bundle();
		
			// Setting the data to the bundle object */
			b.putInt("position", position);
		
			// Setting the bundle object to the fragment */
			//fragment.setArguments(b);
		
			// Adding the fragment to the fragment transaction */
			//	fragmentTransaction.add(R.id.fragmentDirection, fragment,"in.wptrafficanalyzer.country.details");

			// Adding this transaction to backstack */
			fragmentTransaction.addToBackStack(null);
		
			// Making this transaction in effect */
			fragmentTransaction.commit();
		        
		}
	}
	
	public void setUpInitialDatabase()
	{
		dataSource = new DestinationDataSource(this.getActivity());
		dataSource.open();
				
		//dataSource.CreateNewHARDCODEDDataBase();
		
		// This call is for retrieving data from the server dynamically.
		new RetrieveData(getActivity().getApplicationContext()).execute();
		
		this.allDestinations = new ArrayList<Destination>();
		this.selectedDestinations = new ArrayList<Destination>();
		
		this.allDestinations = dataSource.getAllDestinations();

		for(Destination des: this.allDestinations)
		{
			this.selectedDestinations.add(des);
		}
		
		new GetDistanceMatrixTask().execute(this.allDestinations);
	}
	
	private void setUpDestinationList()
	{
		 this.destinationListAdapter = new DestinationListAdapter(this,
				R.layout.listviewitem_style, selectedDestinations);	
		
		// For custom list view and interactive output.
		ListView lv = (ListView)this.getView().findViewById(R.id.listViewDestinations);
		lv.setAdapter(this.destinationListAdapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		    	 onListViewItemClick(position);
		       }
		   });
	}
	
	@Override
	public void onCategoryChangeHandler(ArrayList<Integer> newFlags) {

		this.selectedDestinations.clear();
		
		for(Destination des: allDestinations)
		{
			if(newFlags.contains(des.getType()))
			{
				this.selectedDestinations.add(des);
			}
		}
		Collections.sort(selectedDestinations, new DurationComparator());
		this.destinationListAdapter.notifyDataSetChanged();
	}
	
	public void addLocationListener() 
	{		 	
		MyLocationListener.getInstance().addListener(this);	 	
	}	 	
			 	
	public void removeLocationListner()	 	
	{	 	
		MyLocationListener.getInstance().removeListener(this);	 	
	}	 	

	public void startLocationUpdates()
	{
		LocationManager locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE );  
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000000, 20, MyLocationListener.getInstance());
	}
			
	public void stopLocationUpdates() 
	{
		LocationManager locationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE );
		locationManager.removeUpdates(MyLocationListener.getInstance());
	}
	
	@Override
	public void onMyLocationChanged(Location location) {
		// TODO calculate difference between the two latlng and if difference is greater than 20 meters then 
		// call Distance Matrix api otherwise not.
		LatLng newLatLng = new LatLng(MyLocationListener.getLat(), MyLocationListener.getLon());
		this.currentLatLng = newLatLng;
		
		synchronized (this) {
			//new GetDistanceMatrixTask().execute(this.allDestinations);
		}
	}
	
	public LatLng getCurrentLatLng() {
		
		if(this.currentLatLng == null)
		{
			Location lastKnown = this.getLastBestKnownDeviceLocation();
			this.currentLatLng = new LatLng(lastKnown.getLatitude(), lastKnown.getLongitude());
			
		}
		return currentLatLng;
	}

	public void setCurrentLatLng(LatLng currentLatLng) {
		this.currentLatLng = currentLatLng;
	}
	
	public Location getLastBestKnownDeviceLocation()
	{
		Location locationGPS = ((LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location locationNet = ((LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
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
	 * This method gets the result from the google Distance Matrix api call and have
	 * distance and walking time of all destinations present in our project.
	 * Update the local copy of destination collection's walking time object with the time received
	 * and show on UI.
	 */
	private void onBackgroundTaskDataObtained(ArrayList<Integer> walkingTimes)
	{
		Destination temp;
		
		// copy Walking times to destination objects
		for(int index = 0; index < walkingTimes.size(); index++)
		{
			temp = this.allDestinations.get(index); 
			int uiListIndex = this.selectedDestinations.indexOf(temp);
			temp.setWalkingTime(walkingTimes.get(index));
			if(uiListIndex >= 0)
			{
				this.selectedDestinations.get(uiListIndex).setWalkingTime(walkingTimes.get(index));
			}
		}		
		Collections.sort(selectedDestinations, new DurationComparator());
		Collections.sort(allDestinations, new DurationComparator());
		this.destinationListAdapter.notifyDataSetChanged();		
	}
	
	// **************************************************************************************
				// 	 GOOGLE DISTANCE MATRIX api 
				// for calculating distance and walking time between two points
	// **************************************************************************************
	
	public class GetDistanceMatrixTask extends AsyncTask<List<Destination>, Void, Document> {
		
		private Exception exception;

		// Please note walking directions are hard coded, in case you need 
		// driving direction (in future) then change mode to walking
		protected Document doInBackground(List<Destination>... allLocations) {
			
			String destinationURL = "";
			List<Destination> reqdLocations = allLocations[0];
			
			for(int index = 0; index < reqdLocations.size(); index ++)
			{
				destinationURL += reqdLocations.get(index).getLatitude() +"," + reqdLocations.get(index).getLongitude();
				if(index+1 != reqdLocations.size())
				{
					try {
						destinationURL+=  URLEncoder.encode("|", "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// IMP to use URLEncoder here
				// See more details at http://goo.gl/f6GFq
			}
			
			// Textile location: 35.770409,-78.678747
			String url = "http://maps.googleapis.com/maps/api/distancematrix/xml?"
					+ "origins=" + getCurrentLatLng().latitude + "," + getCurrentLatLng().longitude
	        		+ "&destinations=" + destinationURL
	        		+ "&sensor=false&mode=walking";
			
	        try {
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpContext localContext = new BasicHttpContext();
	            HttpPost httpPost = new HttpPost(url);
	            HttpResponse response = httpClient.execute(httpPost, localContext);
	            InputStream in = response.getEntity().getContent();
	            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            Document doc = builder.parse(in);
	            return doc;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return null;
			
		}
		
		protected void onPostExecute(Document doc) {
	        // Parse the result obtained from api call 
			parseGoogleDistanceMatrix(doc);
			progress.dismiss();
	    }
		
		/*
		 * Parse the google XML result and get the required data.
		 * Also pass the required data to Fragment Direction (using onBackground TaskData Obtained method).
		 * Then that method will draw route on map
		 */
		public void parseGoogleDistanceMatrix(Document doc)
		{
			GoogleDistanceMatrixReader gDistMatrix = new GoogleDistanceMatrixReader();
			ArrayList<Integer> duration = gDistMatrix.getDurationValue(doc);

			FragmentDestinations.this.onBackgroundTaskDataObtained(duration);
		}
	}
	
	
	// ***************************************************************************************
				// Getting list dynamically from server
	// ***************************************************************************************
	

	public class RetrieveData extends AsyncTask {

		Context context;
	    ProgressDialog progressDialog;
	    //ConfigurationContainer configuration = ConfigurationContainer.getInstance();
	    
	    public RetrieveData(Context context) {
	        //this.context = context;
	        //waitSpinner = new ProgressDialog(this.context);
	    }
	    
	    protected Object doInBackground(Object... args) {


	    /*	try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
	    	
			String mysqlIP = "152.46.18.252";
			ArrayList<String> data = new ArrayList<String>();
			String result = "";
			//the year data to send
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("",""));
			 
			InputStream is = null;
			try{
			        HttpClient httpclient = new DefaultHttpClient();
			        HttpPost httppost = new HttpPost("http://"+mysqlIP+"/getDestinationsList.php");
			       // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        HttpResponse response = httpclient.execute(httppost);
			        HttpEntity entity = response.getEntity();
			        is = entity.getContent();
			}catch(Exception e){
			        Log.e("log_tag", "Error in http connection "+e.toString());
			}
			//convert response to string
			try{
			        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			        StringBuilder sb = new StringBuilder();
			        String line = null;
			        while ((line = reader.readLine()) != null) {
			                sb.append(line + "\n");
			        }
			        is.close();
			 
			        result=sb.toString();
			}catch(Exception e){
			        Log.e("log_tag", "Error converting result "+e.toString());
			}
			 
			//parse json data
			try{
					dataSource.open();
					dataSource.clearDB();
					
			        JSONArray jArray = new JSONArray(result);
			        for(int i=0;i<jArray.length();i++){
			                JSONObject json_data = jArray.getJSONObject(i);
			        		
			        		//if(!dataSource.checkDataBase())
			                Destination dest = new Destination(json_data.getInt("id"), json_data.getDouble("lats"), json_data.getDouble("longs"), json_data.getString("name"), json_data.getInt("type"), json_data.getString("description"), json_data.getInt("favourite"));
			        		
			        		dataSource.createPlace(dest);
			                
			        }
			}catch(JSONException e){
			        Log.e("log_tag", "Error parsing data "+e.toString());
			}

			
			return null;
		}
	    
	    
	    protected void onProgressUpdate(Object... values) {
	        super.onProgressUpdate(values);
	    }
	    
		protected void onPostExecute(Object result) {
	      
			super.onPostExecute(result);
	    }		
	}	
}
