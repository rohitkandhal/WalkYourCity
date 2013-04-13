
package com.csc591.view;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.CalendarContract.Colors;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csc591.DAL.Destination;

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
		placeData.txtTime.setText("in XXX minutes");
		listItem.setBackgroundColor(getColorCode(destination.getType()));
		return listItem;
	}
	
	/* Old Method for activity
	
	public View getView(int postion, View convertView, ViewGroup parent)
	{
		View listItem = convertView;
		PlaceData placeData = null;
		
		if(listItem == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			listItem = inflater.inflate(layoutResourceId, parent, false);
			
			placeData = new PlaceData();
			placeData.imgIcon = (ImageView)listItem.findViewById(R.id.imgIcon);
			
			placeData.txtName = (TextView)listItem.findViewById(R.id.txtTitle);
			
			listItem.setTag(placeData);
		}
		else
		{
			placeData = (PlaceData)listItem.getTag();
		}
		
		Destination destination = data[postion];
		placeData.txtName.setText(destination.getName());
		placeData.imgIcon.setImageResource(getIcon(destination.getType()));
		
		return listItem;
	}
	*/
	
	public int getIcon(int type)
	{
		switch(type)
		{
		case 0:
			//return R.drawable.blue;
			return R.drawable.list_blue;
			
		case 1:
			//return R.drawable.sky;
			return R.drawable.list_sky;
			
		case 2:
			return R.drawable.list_orange;
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
			return this.hdl.getResources().getColor(R.color.blue);
			
		case 1:
			return this.hdl.getResources().getColor(R.color.sky);

		case 2:
			return this.hdl.getResources().getColor(R.color.orange);
			
		case 3:
			return this.hdl.getResources().getColor(R.color.green);
			
			default:
				return this.hdl.getResources().getColor(R.color.green);
		}
	}
}
