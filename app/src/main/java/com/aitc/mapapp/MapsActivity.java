package com.aitc.mapapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    String ip;
    String countryCode;
    Double latitude;
    Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        ip = intent.getExtras().getString("ip");
        countryCode = intent.getExtras().getString("country_code");
        longitude = intent.getExtras().getDouble("longitude");
        latitude = intent.getExtras().getDouble("latitude");

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(ip + " : " + countryCode));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));

//        // Move the camera instantly to Sydney with a zoom of 15.
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 10));
//
//        // Zoom in, animating the camera.
//        mMap.animateCamera(CameraUpdateFactory.zoomIn());
//
//        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 4000, null);
//
//        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(latitude, longitude))      // Sets the center of the map to Mountain View
//                .zoom(15)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();                   // Creates a CameraPosition from the builder
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
