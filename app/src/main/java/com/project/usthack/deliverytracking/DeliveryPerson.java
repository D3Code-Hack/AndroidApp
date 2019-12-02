package com.project.usthack.deliverytracking;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DeliveryPerson extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DirectionsResult results;
    String otp;

    final List<String> cities = new ArrayList<String>();
    int v=0;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    double v1,v2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_person);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button bt = findViewById(R.id.start);
        final EditText txtView = findViewById(R.id.otp);
        final ImageView bt1 = findViewById(R.id.verify);
        final Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webview = new WebView(getApplicationContext());
                webview.getSettings().setJavaScriptEnabled(true);
                setContentView(webview);
                webview.loadUrl("https://ust-hack.herokuapp.com");

            }
        });

        final Button emer = findViewById(R.id.emer);
        emer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("hi","error");
                Intent intent = new Intent(getApplicationContext(),emergency.class);
                startActivity(intent);
            }
        });
                        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strst1 = bt.getText().toString();
                if(strst1.equalsIgnoreCase("Start")){
                    startService(new Intent(getApplicationContext(), TrackerService.class));
                    bt.setBackgroundColor(Color.BLUE);
                    bt.setText("STOP");
                    txtView.setVisibility(View.VISIBLE);
                    bt1.setVisibility(View.VISIBLE);
                    otp = String.valueOf(new Random().nextInt(9999));
                    //Get number of the owner of the store to which he is assigned.
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("7250996657", null, "The OTP for verification is "+otp, null, null);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    String path = "OTP/"+uid;
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(path);
                    ref1.setValue(otp);
                }
                if(strst1.equalsIgnoreCase("Stop")){
                    stopService(new Intent(getApplicationContext(), TrackerService.class));
                    bt.setBackgroundColor(Color.RED);
                    bt.setText("START");
                    txtView.setVisibility(View.INVISIBLE);
                    bt1.setVisibility(View.INVISIBLE);

                }}});

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    String path = "OTP/"+uid;
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(path);
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String ch = dataSnapshot.getValue().toString();
                            if (txtView.getText().toString().equals(ch)) {
                                Toast.makeText(DeliveryPerson.this, "OTP Verified, Stop Tracking", Toast.LENGTH_SHORT).show();
                                SmsManager smsManager = SmsManager.getDefault();
                                //Number and store location and order id to be taken from database
                                smsManager.sendTextMessage("7250996657", null, "Package delivered at XYZ", null, null);
                                txtView.setVisibility(View.INVISIBLE);
                                bt1.setVisibility(View.INVISIBLE);
                                v=1;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                    if (v==1) {
                        ref1.removeValue();
                    }

                }});
    }



    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey("AIzaSyDJeSXFDR9XIzTaSD7pSKeBwHJfFgRPnj0")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("assignment");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    cities.add(postSnapshot.getValue().toString());
                    if(i==0){
                        v1 = Double.parseDouble(postSnapshot.getValue().toString());
                    }
                    else{
                        v2 = Double.parseDouble(postSnapshot.getValue().toString());
                    }
                    i++;

                    Log.i("raghav",postSnapshot.getValue().toString());
                }

                mMap.addMarker(new MarkerOptions().position(new LatLng(v1,v2))).setTitle("Destination for the driver");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear(); //clear old markers
        DateTime now = new DateTime();
        MarkStores();
        subscribeToUpdates();
    }

    private void MarkStores() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Store");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarkerBP(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarkerBP(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarkerBP(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {
            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
        } else {
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }

    private void subscribeToUpdates() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Current_location"+'/'+uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double lat = dataSnapshot.child("latitude").getValue(double.class);
                double lng = dataSnapshot.child("longitude").getValue(double.class);
                LatLng location = new LatLng(lat, lng);


                if (!mMarkers.containsKey(uid)) {
                    mMarkers.put(uid, mMap.addMarker(new MarkerOptions().title(uid).position(location)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                } else {
                    mMarkers.get(uid).setPosition(location);
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : mMarkers.values()) {
                    builder.include(marker.getPosition());
                }
//                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});
    }

}
