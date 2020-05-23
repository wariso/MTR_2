package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class GPSTracker implements LocationListener {
    Context context;

    public GPSTracker(){
        context = App.getcontext();

    }

    public Location getLocation(){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permiso no otorgado", Toast.LENGTH_SHORT).show();
            return null;

        }

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
            Toast.makeText(context, "GPS esta habilitado", Toast.LENGTH_LONG).show();
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            String res = "";
            if (l!=null){
                res = "" + l.getLatitude() + " " + l.getLongitude();
            }else{
                res = "N/A";
            }

            Toast.makeText(context, "coordenadas " + res, Toast.LENGTH_LONG).show();
            return l;

        }else{
            Toast.makeText(context, "Habilite GPS", Toast.LENGTH_LONG).show();
        }

        return null;

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
