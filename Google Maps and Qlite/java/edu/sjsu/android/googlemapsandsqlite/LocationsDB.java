package edu.sjsu.android.googlemapsandsqlite ;

import android.content.ContentValues ;
import android.content.Context ;
import android.database.Cursor ;
import android.database.sqlite.SQLiteDatabase ;
import android.database.sqlite.SQLiteOpenHelper ;

public class LocationsDB extends SQLiteOpenHelper
{
    // DB info and schema
    static final String DATABASE_NAME = "Location" ;
    static final String TABLE_NAME = "locations" ;
    static final int DATABASE_VERSION = 1 ;
    static final String _ID = "_id";
    static final String LATITUDE = "latitude" ;
    static final String LONGITUDE = "longitude" ;
    static final String ZOOM_LVL = "zoom" ;

    static final String CREATE_DB_TABLE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " latitude DOUBLE NOT NULL, " + " longitude DOUBLE NOT NULL, " + " zoom INTEGER NOT NULL);" ;
    private SQLiteDatabase db ;

    public LocationsDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION) ;
        this.db = getWritableDatabase() ;
    }

    @Override public void onCreate(SQLiteDatabase db) {db.execSQL(CREATE_DB_TABLE) ;}

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME) ;
        onCreate(db) ;
    }

    /**
     * Inserts a new location to the table.
     * @param values the row needed to be inserted.
     * @return the ID (primary key) of the inserted row.
     */
    public long insertLocation(ContentValues values)
    {
        long ID = db.insert(TABLE_NAME, "", values) ;
        return ID;
    }

    /**
     * Deletes all locations from the table.
     * @return number of rows deleted.
     */
    public int deleteLocations()
    {
        int count = db.delete(TABLE_NAME, null, null) ;
        return count;
    }

    /**
     * Returns all the locations from the table.
     * @return a Cursor pointing to the result of the query.
     */
    public Cursor returnLocations()
    {
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null) ;
        return c;
    }
}
