package edu.sjsu.android.googlemapsandsqlite ;

import androidx.fragment.app.FragmentActivity ;
import androidx.loader.app.LoaderManager ;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor ;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle ;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory ;
import com.google.android.gms.maps.GoogleMap ;
import com.google.android.gms.maps.OnMapReadyCallback ;
import com.google.android.gms.maps.SupportMapFragment ;
import com.google.android.gms.maps.model.LatLng ;
import com.google.android.gms.maps.model.MarkerOptions ;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>
{
    private GoogleMap map ;
    private final LatLng LOCATION_UNI = new LatLng(37.335371, -121.881050) ;
    private final LatLng LOCATION_CS = new LatLng(37.333714, -121.881860) ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap ;
        LoaderManager.getInstance(this).initLoader(0, null, this) ; // invoke LoaderCallbacks to retrieve and draw already saved locations in map

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override public void onMapClick(LatLng point)
            {
                map.addMarker(new MarkerOptions().position(point).title("Find Me Here!")) ; // add a maker to the map
                LocationInsertTask insertTask = new LocationInsertTask() ;   // creating an instance of LocationInsertTask

                // storing the latitude, longitude and zoom level to SQList database
                ContentValues values = new ContentValues() ;
                values.put(LocationsDB.LATITUDE, point.latitude) ;
                values.put(LocationsDB.LONGITUDE, point.longitude) ;
                values.put(LocationsDB.ZOOM_LVL, map.getCameraPosition().zoom) ;
                insertTask.execute(values) ;

                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show() ; // display "Maker is added to the Map" message
            }
        }) ;

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override public void onMapLongClick(LatLng point)
            {
                map.clear() ;   // removing all markers from the Google Map
                LocationDeleteTask deleteTask = new LocationDeleteTask() ;  // creating an instance of LocationDeleteTask
                deleteTask.execute() ;  // deleting all the rows from SQLite database table
                Toast.makeText(getBaseContext(), "All markers are removed", Toast.LENGTH_LONG).show() ; // display “ALL makers are removed" message
            }
        }) ;
    }

    public void onClickCS(View v)
    {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN) ;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18) ;
        map.animateCamera(update) ;
    }

    public void onClickUni(View v)
    {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL) ;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNI, 14) ;
        map.animateCamera(update) ;
    }

    public void onClickCity(View v)
    {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE) ;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNI, 10) ;
        map.animateCamera(update) ;
    }

    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>
    {
        @Override protected Void doInBackground(ContentValues... contentValues)
        {
            // setting up values to insert the clicked location into SQLite database
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]) ;
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>
    {
        @Override protected Void doInBackground(Void... params)
        {
            // deleting all the location stored in SQLite database
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null) ;
            return null;
        }
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
    {
        Loader<Cursor> c = null ;
        Uri uri = LocationsContentProvider.CONTENT_URI ;    // Uri to the content provider LocationsContentProvider
        // Fetches all the rows from locations table
        c = new CursorLoader(this, uri, null, null, null, null) ;
        return c;
    }

    @SuppressLint("Range")
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1)
    {
        int locationCount = 0 ;
        double lat = 0 ;
        double lng = 0 ;
        float zoom = 0 ;

        // Number of locations available in the SQLite database table
        if (arg1 != null)
        {
            locationCount = arg1.getCount() ;
            arg1.moveToFirst() ;    // move the current record pointer to the first row of the table
        }
        else {locationCount = 0 ;}

        for (int i = 0; i < locationCount; i++)
        {
            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.LATITUDE)) ;   // get the latitude
            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.LONGITUDE)) ;  // get the longitude
            zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.ZOOM_LVL)) ;   // get the zoom level

            LatLng location = new LatLng(lat, lng) ;    // creating an instance of LatLng to plot the location in Google Maps
            map.addMarker(new MarkerOptions().position(location).title("Marker added!")) ;  // drawing the marker in the Google Maps
            arg1.moveToNext() ; // traverse the pointer to the next row
        }

        if (locationCount > 0)
        {
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng))) ;   // moving CameraPosition to last clicked position
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom)) ;   // setting the zoom level in the map on last position is clicked
        }
    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {}
}
