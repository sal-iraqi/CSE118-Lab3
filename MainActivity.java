package com.example.lab3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.lab3.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private TextView mLatitudeView;
    private TextView mLongtitudeView;
    private TextView mAltitudeView;
    private TextView mWifiStrengthView;
    private android.location.LocationManager mGPSLocationManager;
    private android.location.LocationListener mGPSLocationListener;
    private android.net.wifi.WifiManager mWifiManager;
    private ActivityMainBinding binding;
    private long _minTime = 10;
    private float _minDistance = 0.1f;

    private static final int FINE_PERMISSION_CODE = 100;
    private static final int COARSE_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        mTextView = binding.text;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("In onResume");
        mLatitudeView = (TextView) findViewById(R.id.latitude);
        mLongtitudeView = (TextView) findViewById(R.id.longtitude);
        mAltitudeView = (TextView) findViewById(R.id.altitude);
        mWifiStrengthView = (TextView) findViewById(R.id.wifi_strength);
        mGPSLocationManager = (android.location.LocationManager) this.getSystemService(android.content.Context.LOCATION_SERVICE);
        Context context = getApplicationContext();
        mWifiManager = (android.net.wifi.WifiManager) context.getSystemService(android.content.Context.WIFI_SERVICE);

        mGPSLocationListener = new android.location.LocationListener() {
            public void onLocationChanged(android.location.Location location) {
                System.out.println("Location Changed");
                int rssi = mWifiManager.getConnectionInfo().getRssi();
                int level = mWifiManager.calculateSignalLevel(rssi);
                mLatitudeView.setText(String.valueOf((int) location.getLatitude()));
                mLongtitudeView.setText(String.valueOf((int) location.getLongitude()));
                mAltitudeView.setText(String.valueOf((int) location.getAltitude()));
                mWifiStrengthView.setText(String.valueOf(level));

                System.out.println("Latitude: " + mLatitudeView.getText());
                System.out.println("Longtitude: " + mLongtitudeView.getText());
                System.out.println("Altitude: " + mAltitudeView.getText());
                System.out.println("WiFi Strength: " + mWifiStrengthView.getText());
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, FINE_PERMISSION_CODE);
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, COARSE_PERMISSION_CODE);
            return;
        }
        mGPSLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, _minTime, _minDistance, mGPSLocationListener);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Access Fine Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Access Fine Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        else if (requestCode == COARSE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Access Coarse Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Access Coarse Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}