package edu.sjsu.android.googlemapsandsqlite ;

import android.content.ContentProvider ;
import android.content.ContentUris ;
import android.content.ContentValues ;
import android.content.UriMatcher ;
import android.database.Cursor ;
import android.database.SQLException ;
import android.net.Uri ;

public class LocationsContentProvider extends ContentProvider
{
    static final String PROVIDER_NAME = "edu.sjsu.android.googlemapsandsqlite.locations" ;
    static final String URL = "content://" + PROVIDER_NAME + "/locations" ;
    static final Uri CONTENT_URI = Uri.parse(URL) ;

    static final int LOCATIONS = 1 ;
    static final UriMatcher uriMatcher ;
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH) ;
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS) ;
    }

    private LocationsDB db ;

    @Override public boolean onCreate()
    {
        // Create a write able database which will trigger its creation if it doesn't already exist.
        db = new LocationsDB(getContext()) ;
        return (db == null)? false : true;
    }

    /**
     * A callback method that is invoked when insert operation is requested on this content provider.
     * @param uri initial Uri
     * @param values inserted row
     * @return result Uri
     */
    @Override public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insertLocation(values) ;    // Add a new location record

        if (rowID > 0)  // If record is added successfully
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID) ;
            getContext().getContentResolver().notifyChange(_uri, null) ;
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri) ;
    }

    /**
     * A callback method that is invoked when delete operation is requested on this content provider.
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows deleted.
     */
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {return db.deleteLocations();}

    /**
     * A callback method that is invoked by default content URI.
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return Cursor poiting to the result of the query, null otherwise
     */
    @Override public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        if (uriMatcher.match(uri) == LOCATIONS) return db.returnLocations();
        return null;
    }

    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {return 0;}
    @Override public String getType(Uri uri) {return null;}
}
