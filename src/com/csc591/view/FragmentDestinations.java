package com.csc591.view;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.csc591.DAL.Destination;
import com.csc591.DAL.DestinationDataSource;

public class FragmentDestinations extends Fragment {

	private List<Destination> destinations;
	private DestinationDataSource dataSource;
	
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
		
	}
	
	public void setUpInitialDatabase()
	{
		dataSource = new DestinationDataSource(this.getActivity());
		dataSource.open();
		
		//if(!dataSource.checkDataBase())
		{
			dataSource.CreateNewHARDCODEDDataBase();
		}
		
		destinations = dataSource.getAllDestinations();
	}
	
	private void setUpDestinationList()
	{
		DestinationListAdapter adapter = new DestinationListAdapter(this,
				R.layout.listviewitem_style, destinations);	
		
		// For custom list view and interactive output.
		ListView lv = (ListView)this.getView().findViewById(R.id.listViewDestinations);
		lv.setAdapter(adapter);
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
}
