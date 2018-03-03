package com.example.aayush.bloodbank;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.io.IOException;
import java.security.Permission;
import java.security.Permissions;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 101;
    private GoogleMap mMap;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_REQUEST_CODE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        address = (String) bundle.getString("value");
        Log.e("Adress", address);
        Log.e("OnCreate", "OK");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings ui;
        LatLng sydney = new LatLng(-34, 151);
        Geocoder geocoder = new Geocoder(this);
        List<Address> mListAdress = null;
        Address adres = null;
        LatLng loc;
        double lo, la;

        try {
            mListAdress = geocoder.getFromLocationName(address, 5);
            Log.e("List Address", "OK fetched");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mListAdress.isEmpty()) {
            Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
            String now = address.replaceAll(",", "+");
            Intent geoIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + now)); // Prepare intent
            startActivity(geoIntent); // Initiate lookup
            Log.e("Not fouind", "Not Found");
            onBackPressed();
        } else {
            adres = mListAdress.get(0);
            Log.e("Address", "OK");

            if (adres == null) {
                Toast.makeText(this, "finissh", Toast.LENGTH_SHORT).show();
                return;
            }

            la = adres.getLatitude();
            lo = adres.getLongitude();

            loc = new LatLng(la, lo);
            Log.e("LATLAN", "OK");
            ui = mMap.getUiSettings();
            ui.isZoomControlsEnabled();
            ui.isZoomGesturesEnabled();
            mMap.addMarker(new MarkerOptions().position(loc).title(address));

            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    loc, 15);
            mMap.animateCamera(location);
        }
    }

    protected void requestPermission(String permissionType, int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this,
                permissionType);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permissionType}, requestCode
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
