package com.example.loaderstest;

import java.util.List;

import model.Destination;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DestinationListAdapter extends ArrayAdapter<Destination> {
	
	public class PlaceData
	{
		ImageView imgIcon;
		TextView txtName;
	}
	
	Context context;
	int layoutResourceId;
	List<Destination> data = null;
	
	public DestinationListAdapter(Context context, int layoutResourceId, List<Destination> data)
	{
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data  	= data;
	}
	
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
		
		Destination destination = data.get(postion);
		placeData.txtName.setText(destination.getName());
		placeData.imgIcon.setImageResource(getIcon(destination.getType()));
		
		return listItem;
	}
	
	public int getIcon(int type)
	{
		switch(type)
		{
		case 0:
			return R.drawable.blue;
			
		case 1:
			return R.drawable.sky;
			
		case 2:
			return R.drawable.orange;
			
		case 3:
			return R.drawable.green;
			
			default:
				return R.drawable.blue;
		}
		
	}

}
