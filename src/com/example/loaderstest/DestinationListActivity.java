package com.example.loaderstest;

import java.util.List;

import model.Destination;
import DAL.DestinationsDataSource;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class DestinationListActivity extends Activity {

	private DestinationsDataSource dataSource;
	
	private ListView listView1;
	
	private List<Destination> destCollection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_list);
		
		this.setUpDestinationList();
	}
	
	private void setUpDestinationList()
	{
		dataSource = new DestinationsDataSource(this);
		dataSource.open();
		
		//if(!dataSource.checkDataBase())
		{
		dataSource.CreateNewHARDCODEDDataBase();
		}
		
		destCollection = dataSource.getAllDestinations();
		
		DestinationListAdapter adapter = new DestinationListAdapter(this,
				R.layout.listviewitem_style, destCollection);
		
		listView1 = (ListView)findViewById(R.id.listView1);
		
		View header = (View)getLayoutInflater().inflate(R.layout.listview_header, null);
		//listView1.addHeaderView(header);
		listView1.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custom_list, menu);
		return true;
	}
	
	
	
	protected void onResume()
	{
		dataSource.open();
		super.onResume();
	}
	
	protected void onPause()
	{
		dataSource.close();
		super.onPause();
	}


}
