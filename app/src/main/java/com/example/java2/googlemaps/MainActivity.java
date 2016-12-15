package com.example.java2.googlemaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final float MIN_DISTANCE_METERS = 1;
    private static final long MIN_TIME_UPDATE = 1;
    private final String TAG="tralala";
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean net = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        checkBestProvider();
        Location lastKnownLocation=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Log.v(TAG," "+lastKnownLocation+ "last known loc");
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_METERS, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("locationCHanged", location.toString());
        getAdress(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("onStatus", provider+"  "+status+"    ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("onProviderEnabl", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("onProviderDisabl", provider);
    }

    private void getAdress(double latitude,double longitude){
        Geocoder geocoder =new Geocoder(getApplicationContext(), Locale.getDefault());
        try{
            List<Address> listAdresses=geocoder.getFromLocation(latitude,longitude,1);
            if(null!=listAdresses&&listAdresses.size()>0){
                String location=listAdresses.get(0).getAddressLine(0);
                String countryCode=listAdresses.get(0).getCountryCode();
                String countryName=listAdresses.get(0).getCountryName();
                Log.v(TAG,"country code: "+countryCode+" country name: "+countryName+" location: "+location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void checkBestProvider(){

        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String locationProvider=lm.getBestProvider(criteria,true);
        Log.v(TAG," loc provider: "+ locationProvider);
    }
}
