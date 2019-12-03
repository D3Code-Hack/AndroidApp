package com.project.usthack.deliverytracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Admin extends FragmentActivity implements OnMapReadyCallback {
    String s;
    private Button b;
    private Button nxt;
    private GoogleMap mMap;
    private HashMap<String, Marker> mMarkers = new HashMap<>();

    private EditText name1;
    private EditText contact1;
    private EditText latitude1;
    private EditText place1;
    private EditText longitude1;
double lat1,log1;
    private String name;
    private String contact;
    private String latitude;
    private String longitude;
    private String place;

    String path="";
    DatabaseReference ref;
    private Button submit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_admin);


        latitude1 = (EditText) findViewById(R.id.latitude);
        longitude1 = (EditText) findViewById(R.id.longitude);
        name1 = (EditText) findViewById(R.id.oname);
        contact1 = (EditText) findViewById(R.id.ocontact);
        place1 = (EditText) findViewById(R.id.place);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        b= findViewById(R.id.add);
        nxt= findViewById(R.id.next);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView t = findViewById(R.id.place);
//                s = t.getText().toString();
//                Log.d("STRING", s);
//                getLatLng(s,getApplicationContext());
//            }
//        });

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TrackActivity.class);
                startActivity(myIntent);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                place =place1.getText().toString();

                path = "Enterprise/"+uid+"/Store/"+place+"/Name";
                ref= FirebaseDatabase.getInstance().getReference(path);
                name =name1.getText().toString();
                ref.setValue(name);

                path = "Enterprise/"+uid+"/Store/"+place+"/Contact";
                ref= FirebaseDatabase.getInstance().getReference(path);
                contact =contact1.getText().toString();
                ref.setValue(contact);

                path = "Enterprise/"+uid+"/Store/"+place+"/Latitude";
                ref= FirebaseDatabase.getInstance().getReference(path);
                //latitude =latitude1.getText().toString();
                ref.setValue("12.8");

                path = "Enterprise/"+uid+"/Store/"+place+"/Longitude";
                ref= FirebaseDatabase.getInstance().getReference(path);
                //longitude =longitude1.getText().toString();
                ref.setValue("18.1");

                Intent intent = new Intent(getApplicationContext(),TrackActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static Address getLatLng(String location, Context mContext) {
        Address address = null;
        try {
            Geocoder gc = new Geocoder(mContext);
            List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects

            for (Address a : addresses) {
                if (a.hasLatitude() && a.hasLongitude()) {
                  Log.i("location", String.valueOf(location + "   " + a.getLatitude() + " " + a.getLongitude()));
                    address = a;
                } else {
                    Log.d("ERROR", " this location has no entry " + location);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {


        DatabaseReference db =FirebaseDatabase.getInstance().getReference().child("Enterprise");
        Query query = db.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    lat1 =  Double.parseDouble(child.child("Latitude").getValue().toString());
                    log1 =  Double.parseDouble(child.child("Longitude").getValue().toString());
                }
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Log.d("msg",""+lat1+"hi");
                Log.d("msg",""+lat1);
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(lat1,log1))
                        .zoom(1)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat1,log1))).setTitle("Enterprise");
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        MarkStores();
    }

    private void MarkStores() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Enterprise/"+uid+"/Store");
        ref1.addChildEventListener(new ChildEventListener() {
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
        try{
            String key = dataSnapshot.getKey();
            HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
            double lat = Double.parseDouble(value.get("Latitude").toString());
            double lng = Double.parseDouble(value.get("Longitude").toString());
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
        catch(Exception e){

        }


    }
}
