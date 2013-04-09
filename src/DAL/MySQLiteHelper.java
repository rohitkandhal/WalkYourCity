package DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_DESTINATIONS = "destinations";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_latitude = "latitude";
	public static final String COLUMN_longitude = "longitude";
	public static final String COLUMN_name = "name";
	public static final String COLUMN_type = "type";
	public static final String COLUMN_description = "description";
	public static final String COLUMN_favorite = "favorite";
	
	private static final String DATABASE_NAME = "WalkYourCity.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_DESTINATIONS + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_latitude + " integer not null, " 
			+ COLUMN_longitude + " integer not null, "
			+ COLUMN_name + " text not null, " 
			+ COLUMN_type + " integer, "
			+ COLUMN_description + " text, "
			+ COLUMN_favorite + " integer not null);";
	
	public MySQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME,null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(MySQLiteHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + 
				" to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESTINATIONS);
		onCreate(db);
	}
}
