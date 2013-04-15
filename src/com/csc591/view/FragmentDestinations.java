package com.csc591.view;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.csc591.DAL.Destination;
import com.csc591.DAL.DestinationDataSource;
import com.csc591.utils.GoogleDistanceMatrixReader;
import com.csc591.view.Home.OnFooterCategorySelectionChanged;

public class FragmentDestinations extends Fragment implements OnFooterCategorySelectionChanged{

	private List<Destination> allDestinations;
	private List<Destination> selectedDestinations;
	DestinationListAdapter destinationListAdapter;
	
	private DestinationDataSource dataSource;
	
	public interface FragmentDestinationInterface{
		public void onListItemClickHandler(Destination destination);
	}
	
	public FragmentDestinationInterface homeActivityInterface;
	
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_dest_list,container,false);
		this.setUpInitialDatabase();
		return view;
	}
	

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
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
	
	public void onListViewItemClick(int position)
	{
		homeActivityInterface.onListItemClickHandler(allDestinations.get(position));
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
		
		//if(!dataSource.checkDataBase())
		{
			dataSource.CreateNewHARDCODEDDataBase();
		}
		
		allDestinations = dataSource.getAllDestinations();
		this.selectedDestinations = dataSource.getAllDestinations();
		
		ArrayList<Destination> forGoogleApi = new ArrayList<Destination>();
		//Destination dest8 = new Destination(0,,"Bowling",0,"some description",0);
		
		Destination dummySourceDest = new Destination();
		//forGoogleApi.add()
		new GetDistanceMatrixTask().execute(this.allDestinations);
		//new GetDistanceMatrixTask().execute(new LatLng(35.777418,-78.677666), new LatLng(35.78036,-78.67816), new LatLng(35.787515,-78.670456));
	}
	
	private void setUpDestinationList()
	{
		 this.destinationListAdapter = new DestinationListAdapter(this,
				R.layout.listviewitem_style, selectedDestinations);	
		
		// For custom list view and interactive output.
		ListView lv = (ListView)this.getView().findViewById(R.id.listViewDestinations);
		lv.setAdapter(this.destinationListAdapter);
	}
	
	public void onResume()
	{
		dataSource.open();
		super.onResume();
	}

	public void onPause()
	{
		dataSource.close();
		super.onPause();
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
		
		this.destinationListAdapter.notifyDataSetChanged();
	}
	
	/*
	 * This method gets the result from the google Distance Matrix api call and have
	 * distance and walking time of all destinations present in our project.
	 * Update the local copy of destination collection's walking time object with the time received
	 * and show on UI.
	 */
	private void onBackgroundTaskDataObtained(ArrayList<Integer> walkingTimes)
	{
		this.setUpDestinationList();
		
		// copy Walking times to destination objects
		for(int index = 0; index < walkingTimes.size(); index++)
		{
			this.selectedDestinations.get(index).setWalkingTime(walkingTimes.get(index));
			this.allDestinations.get(index).setWalkingTime(walkingTimes.get(index));
		}		
		
		ListView lv = (ListView)this.getView().findViewById(R.id.listViewDestinations);

		lv.setOnItemClickListener(new OnItemClickListener() {

	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	    	 onListViewItemClick(position);
	       }
	   });
		
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
			
			// IMPORTANT NOTE: ASSUMING THAT FIRST LATLNG PASSED IS ALWAYS A SOURCE LOCATION
			for(int index = 1; index < reqdLocations.size(); index ++)
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
			
			String url = "http://maps.googleapis.com/maps/api/distancematrix/xml?" 
	        		+ "origins=" + reqdLocations.get(0).getLatitude()+ "," + reqdLocations.get(0).getLongitude()  
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
}
