package com.project.usthack.deliverytracking;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

//        public static Address getLatLng(String location, Context mContext) {
//            Address address = null;
//            try {
//                Geocoder gc = new Geocoder(mContext);
//                List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects
//
//                for (Address a : addresses) {
//                    if (a.hasLatitude() && a.hasLongitude()) {
////                    Log.i(TAG, String.valueOf(location + "   " + a.getLatitude() + "
//// " + a.getLongitude()));
//                        address = a;
//                    } else {
//                        Log.d(TAG, " this location has no entry " + location);
//                    }
//                }
//            } catch (IOException e) {
//                // handle the exception
//            }
//
//            return address;
//        }
    }
}
