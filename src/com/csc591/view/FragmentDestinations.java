package com.csc591.view;

import java.io.InputStream;
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.csc591.DAL.Destination;
import com.csc591.DAL.DestinationDataSource;
import com.csc591.utils.GMapDirectionHelper;
import com.csc591.view.Home.OnFooterCategorySelectionChanged;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

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
		return view;
	}
	

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		this.setUpInitialDatabase();
		
		this.setUpDestinationList();
		ListView lv = (ListView)this.getView().findViewById(R.id.listViewDestinations);
		//lv.setClickable(true);

		lv.setOnItemClickListener(new OnItemClickListener() {

	       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	    	 onListViewItemClick(position);
	       }
	   });
		
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
		this.selectedDestinations = dataSource.getAllDestinations();;
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
}
