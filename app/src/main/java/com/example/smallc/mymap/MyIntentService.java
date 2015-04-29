package com.example.smallc.mymap;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by small.c on 2015/4/29.
 */
public class MyIntentService extends IntentService {
    private LocationManager locationManager;
    private String provider;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No provider", Toast.LENGTH_SHORT).show();
            return;
        }


        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {    // 显示当前设备的位置信息
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }
    };

    private void showLocation(Location location) {
//        String currentLocation = "latitude is " + location.getLatitude() + "\n"
//                + "longitude is " + location.getLongitude();
        String  latitude =location.getLatitude()+"";
        String  longitude =location.getLongitude()+"";
        Intent intent1=new Intent();
        intent1.putExtra("LOCATION_LATITUDE",latitude);
        intent1.putExtra("LOCATION_LONGITUDE",longitude);
        intent1.setAction("com.example.broadcasttest.MY_BROADCAST");
        sendBroadcast(intent1);

    }

    public void onDestroy() {
        super.onDestroy();
    }

}
