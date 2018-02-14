package com.example.vamshi.guglemapsampl;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;

import java.security.Permission;
import java.security.Permissions;

/**
 * Created by vamshi on 2/9/18.
 */

public class MapActivity extends AppCompatActivity {


    private Boolean mLocPermGranted = false;
    private GoogleMap map;
    private FusedLocationProviderClient locationProviderClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_map);

        getPermission();

    }


    private void getDeviceLocation() {

        Log.i("Device Location", "Getting the device current location");


        locationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        if (mLocPermGranted) {
            try {
                com.google.android.gms.tasks.Task location = locationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        if (task.isSuccessful()) {
                            Log.i("myLocation", "onComplete:Found Location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);
                        } else {
                            Toast.makeText(MapActivity.this, "current location is null", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            } catch (SecurityException e) {
                Log.e("Exception", "getDeviceLocation:Security Exception" + e.getMessage());
            }

        }

    }

    private void moveCamera(LatLng latLng,float zoom){
        Log.i("moveCamera","moveCamera: moving the camera to : lat: " + latLng.latitude + " lng: "+ latLng.longitude);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

    }


    private void initMap() {
        SupportMapFragment smp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        smp.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(MapActivity.this, "map is ready", Toast.LENGTH_SHORT).show();
                map = googleMap;

                if(mLocPermGranted){
                    getDeviceLocation();
                    if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                       return;
                    }

                    map.setMyLocationEnabled(true);

                    map.getUiSettings().setMyLocationButtonEnabled(false);

                }


            }
        });


    }


    private void getPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocPermGranted = true;
                initMap();


        } else {

            ActivityCompat.requestPermissions(this, permissions, 1234);


        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1234:


                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mLocPermGranted = false;
                        return;
                    }
                }
                mLocPermGranted = true;
                // initialize the map
                initMap();
        }


    }
}
