package com.example.vamshi.guglemapsampl;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    /*you need to set up your project with the Google Play services SDK,
     which is available from the Google maven repository.
     >>>> add dependency in the project
      maven{
           url "https://maven.google.com"
        }
    >>>>>>add permissions
       <uses-permission android:name="android.permission.INTERNET"/>
       <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
       <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
       <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    >>>>>>add meta data
      for google play services and  api key
        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="AIzaSyABAGpukfmzt-v5Yy7NBr1URnX4KvQzI38"/>

        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />

        */

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (isServicesOK()) {
            Button btn = findViewById(R.id.button1);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(i);


                }
            });

        }


    }


    // to check if all the service setup is gud
    public boolean isServicesOK() {
        Log.d("ServicesOK", "checking google services version ");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            // to check everything is fine and user can make map requests

            Log.d("ServicesOK", "Google Play services is Working ");


            return true;


        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d("ServicesOK", "an error occured and we can fix it ");


            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();

        } else {
            Toast.makeText(this, "we can't make map requests ", Toast.LENGTH_SHORT).show();

        }

        return false;
    }





}