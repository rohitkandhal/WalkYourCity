
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
		
		String q = "DELETE FROM destinations ";
		database.execSQL(q);
		
		Destination dest = new Destination(1, 35.76652,-78.697278, "Food Lion", 3, "Dummy", 0);
		
		this.createPlace(dest);
		
		Destination dest1 = new Destination(2, 35.788507,-78.665457, "D.H. Hill", 1, "Dummy", 0);
		
		this.createPlace(dest1);
		
		Destination dest2 = new Destination(3, 35.83806375411341, -78.64184260368347, "North Hills Central", 2, "Dummy", 0);
		
		this.createPlace(dest2);
		
		Destination dest3 = new Destination(4, 35.83748101280661, -78.64376306533813, "JCPenney", 3, "Dummy", 0 );
		
		this.createPlace(dest3);
		
		Destination dest4 = new Destination(5,35.83760713,-78.64222348,"REI",0,"Dummy",0);
		
		this.createPlace(dest4);
		
		Destination dest5 = new Destination(6,	35.83992502,-78.64247024,"6Forks Bus Stop",1,"some description",0);
		
		this.createPlace(dest5);
		
		Destination dest6 = new Destination(7,	35.83736794,-78.64178896,"Central Bus Stop",3,"some description",0);
		
		this.createPlace(dest6);
		
		Destination dest7 = new Destination(8,	35.83728966,-78.64024401,"CapTrust Tower",2,"some description",0);
		
		this.createPlace(dest7);
	
		Destination dest8 = new Destination(9,	35.83757016,-78.63985777,"Bowling",0,"some description",0);
		
		this.createPlace(dest8);
		
		Destination dest9 = new Destination(10,35.83688305,-78.64260972,"North Hills Commons",3,"some description",0);
		
		this.createPlace(dest6);
		
		
	
	}
	
	private Destination cursorToDestination(Cursor cursor)
	{
		Destination dest = new Destination();
		dest.setId(cursor.getLong(0));
		// BUG FIX: In SQLite everything is stored as text however you need to take extra care
		// while fetching data from the database. Although while storing the Latitude, Longitude 
		// data type is INTEGER (see MySQLiteHelper) but actually Lat, Lng are double values. 
		// while fetching data, use proper data type.
		// Credits: http://stackoverflow.com/questions/3083796/storing-double-values-in-sqlite-how-to-ensure-precision?answertab=votes#tab-top
		dest.setLatitude(cursor.getDouble(1));
		dest.setLongitude(cursor.getDouble(2));
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
