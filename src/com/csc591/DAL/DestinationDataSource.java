
package com.csc591.DAL;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DestinationDataSource {
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = 
		{ MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_latitude,
			MySQLiteHelper.COLUMN_longitude,
			MySQLiteHelper.COLUMN_name,
			MySQLiteHelper.COLUMN_type,
			MySQLiteHelper.COLUMN_description,
			MySQLiteHelper.COLUMN_favorite
		};
	
	public DestinationDataSource(Context context)
	{
		dbHelper = new MySQLiteHelper(context);
	}
	
	public boolean checkDataBase()
	{
		SQLiteDatabase checkDB = null;
		
		try
		{
		 checkDB =	dbHelper.getWritableDatabase();
		}
		catch (Exception e) {
			// TODO: handle exception
			
		}
		
		
		if(checkDB != null)
		{
			checkDB.close();
			return true;
		}
		else 
			return false;
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public Destination createPlace(Destination dest)
	{
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.COLUMN_latitude, dest.getLatitude() );
		values.put(MySQLiteHelper.COLUMN_longitude,dest.getLongitude());
		values.put(MySQLiteHelper.COLUMN_name, dest.getName() );
		values.put(MySQLiteHelper.COLUMN_type, dest.getType() );
		values.put(MySQLiteHelper.COLUMN_description, dest.getDescription() );
		values.put(MySQLiteHelper.COLUMN_favorite,  dest.getFavorite());
		
		long insertId = database.insert(MySQLiteHelper.TABLE_DESTINATIONS, 
				null, values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DESTINATIONS,
				allColumns, 
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Destination newComment = cursorToDestination(cursor);
		cursor.close();
		return newComment;
	}
	
	public void CreateNewHARDCODEDDataBase()
	{
		Destination dest = new Destination(1, 35.83631354286433, -78.64284843206406, "Target (Underground)", 0, "Dummy", 0);
		
		this.createPlace(dest);
		
		Destination dest1 = new Destination(	2, 35.837289663981274, -78.6402440071106, "CapTrust Tower", 1, "Dummy", 0);
		
		this.createPlace(dest1);
		
		Destination dest2 = new Destination(3, 35.83806375411341, -78.64184260368347, "North Hills Central", 2, "Dummy", 0);
		
		this.createPlace(dest2);
		
		Destination dest3 = new Destination(4, 35.83748101280661, -78.64376306533813, "JCPenney", 3, "Dummy", 0 );
		
		this.createPlace(dest3);
	}
	
	private Destination cursorToDestination(Cursor cursor)
	{
		Destination dest = new Destination();
		dest.setId(cursor.getLong(0));
		dest.setLatitude(cursor.getInt(1));
		dest.setLongitude(cursor.getInt(2));
		dest.setName(cursor.getString(3));
		dest.setType(cursor.getInt(4));
		dest.setDescription(cursor.getString(5));
		dest.setFavorite(cursor.getInt(6));
		return dest;
	}
	
	public List<Destination> getAllDestinations()
	{
		List<Destination> destinations = new ArrayList<Destination>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DESTINATIONS, allColumns, null, null, null, null, null);
	
		cursor.moveToFirst();
		// Check end of cursor
		while (!cursor.isAfterLast())
		{
			Destination comment = cursorToDestination(cursor);
			destinations.add(comment);
			cursor.moveToNext();
		}
		cursor.close();
		return destinations;
	}
}
