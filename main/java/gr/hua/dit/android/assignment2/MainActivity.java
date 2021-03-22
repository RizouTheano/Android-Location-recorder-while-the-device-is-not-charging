package gr.hua.dit.android.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static DbHelper helper;
    BroadcastReceiver batteryBroadcastConnected;
    BroadcastReceiver batteryBroadcastDisconnected;
    private LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryBroadcastDisconnected = new BroadcastReceiver() {  // if phone is disconnected from charger
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, " Start getting locations... ", Toast.LENGTH_SHORT).show();
                find_location();

            }
        };
        IntentFilter filtercon = new IntentFilter();
        filtercon.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryBroadcastDisconnected, filtercon);

        batteryBroadcastConnected = new BroadcastReceiver() {  // if phone is connected to charger

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, " You connected your charger..App stop getting locations...", Toast.LENGTH_SHORT).show();
                if (locationListener != null) {
                    locationManager.removeUpdates(locationListener);
                } else {
                    Toast.makeText(context, " null", Toast.LENGTH_SHORT).show();

                }
            }
        };
        IntentFilter filterdis = new IntentFilter();
        filterdis.addAction(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(batteryBroadcastConnected, filterdis);


        helper = new DbHelper(MainActivity.this);  //db instance
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  // activate service for location
        locationListener = new LocationListener() { //provider for location , when the location has changed at least 10 meters
            @Override
            public void onLocationChanged(Location location) { //if ok, changes location
                long tms = System.currentTimeMillis();  // get timestamps
                String timestamp = String.valueOf(tms);
                String latitude = String.valueOf(location.getLatitude());  // get latitude
                String longitude = String.valueOf(location.getLongitude());  // get longitude
                saveLocationsToDb(timestamp, latitude, longitude);  // call method for variables to be saved into db

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
        };


    }


    @Override
    protected void onDestroy() {  // when activity destroy
        locationManager.removeUpdates(locationListener);  // stop getting locations
        super.onDestroy();
    }
    

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        find_location();
                    }
                }
            }
        }
    }

    protected void find_location() {  //this method finds the location

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //check if the suitable permissions is given
            // if permissions are not already given, ask user for them
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
            return;
        }
        // if user's location>10 or more than 5 minutes have passed get new location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }


    protected long saveLocationsToDb(String timestamp, String latitude, String longitude) { //method for writing into db
        ContactContract row = new ContactContract(timestamp, longitude, latitude); // instance of contactContract where the db fields are
        SQLiteDatabase database = helper.getWritableDatabase();  // db instance
        long insertId = helper.CreateEntry(row); //when an entry is creating, an insert id is returned
        Toast.makeText(MainActivity.this, "Location submitted!", Toast.LENGTH_SHORT).show();  //show message to user if the submission is successful
        return insertId;
    }


}