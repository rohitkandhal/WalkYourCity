
package com.csc591.view;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csc591.DAL.Destination;
import com.csc591.utils.GMapDirectionHelper;

public class DestinationListAdapter extends ArrayAdapter<Destination> {
	
	public class PlaceData
	{
		ImageView imgIcon;
		TextView txtName;
		TextView txtTime;
	}
	
	Context context;
	FragmentDestinations hdl;
	int layoutResourceId;
	List<Destination> data = null;
	
	public DestinationListAdapter(FragmentDestinations hdl, int layoutResourceId, List<Destination> data)
	{
		super(hdl.getActivity(), layoutResourceId, data);
		this.hdl = hdl;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		
	}
	
	@SuppressLint("DefaultLocale")
	public View getView(int postion, View convertView, ViewGroup parent)
	{
		View listItem = convertView;
		PlaceData placeData = null;
		
		if(listItem == null)
		{
			LayoutInflater inflater = this.hdl.getActivity().getLayoutInflater();
			listItem = inflater.inflate(layoutResourceId, parent, false);
			
			placeData = new PlaceData();
			placeData.imgIcon = (ImageView)listItem.findViewById(R.id.imgIcon);
			
			placeData.txtName = (TextView)listItem.findViewById(R.id.txtTitle);
			
			placeData.txtTime = (TextView)listItem.findViewById(R.id.txtTime);
			listItem.setTag(placeData);
		}
		else
		{
			placeData = (PlaceData)listItem.getTag();
		}
		
		Destination destination = data.get(postion);
		placeData.txtName.setText(destination.getName().toUpperCase());
		placeData.imgIcon.setImageResource(getIcon(destination.getType()));
		placeData.txtTime.setText(getTextForTime(destination));	// Google api returns time in seconds
		listItem.setBackgroundColor(getColorCode(destination.getType()));
		return listItem;
	}
	
	public String getTextForTime(Destination destination)
	{
		// If time is less than a minutes
		if(destination.getWalkingTime()/60 == 0)
		{
			return "You are here!!";
		}
		return "in " + destination.getWalkingTime()/60 +" minutes";
	}
	
	public int getIcon(int type)
	{
		switch(type)
		{
		case 0:
			
			return R.drawable.list_orange;
			
		case 1:
			//return R.drawable.sky;
			return R.drawable.list_sky;
			
		case 2:
			return R.drawable.list_blue;
			//return R.drawable.orange;
			
		case 3:
			return R.drawable.list_green;
			//return R.drawable.green;
			
			default:
				return R.drawable.list_green;
		}	
	}
	
	public int getColorCode(int color)
	{
		switch(color)
		{
		case 0:
			return this.hdl.getResources().getColor(R.color.orange);
			
		case 1:
			return this.hdl.getResources().getColor(R.color.sky);

		case 2:
			return this.hdl.getResources().getColor(R.color.blue);
			
		case 3:
			return this.hdl.getResources().getColor(R.color.green);
			
			default:
				return this.hdl.getResources().getColor(R.color.green);
		}
	}
}
