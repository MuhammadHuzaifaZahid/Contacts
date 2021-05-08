package com.example.Contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class Contact_Details extends AppCompatActivity implements OnMapReadyCallback {
    private MapboxMap mapboxMap;
    private MapView mapView;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    CarmenFeature selectedCarmenFeature;
    ContactModel ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1Ijoic21kLXByb2plY3RzIiwiYSI6ImNrMTF1cnJzMDBrbzYzY3AybDJ2cDJhMXcifQ.QX4XGvQOqNRk9uogsCuneg");
        setContentView(R.layout.activity_contact_details);


        DatabaseHandler db = new DatabaseHandler(this);
        ct = db.getContact(getIntent().getExtras().getInt("Id"));
        TextView Contact_Name = findViewById(R.id._Name);
        Contact_Name.setText(ct.getName());
        TextView Contact_No = findViewById(R.id._Phone_No);
        Contact_No.setText(ct.getPhone_No());
        ImageView Contact_Photo = findViewById(R.id.Contact_Photo);
        if(ct.getImage()!=null) { Contact_Photo.setImageURI(Uri.parse(ct.getImage())); }
        if(ct.getAddress()!=null){selectedCarmenFeature = CarmenFeature.fromJson(ct.getAddress());}

        mapView = findViewById(R.id._Address_Location);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this) ;

    }

    private void Show_Location() {

        if (mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                if (source != null) {
                    source.setGeoJson(FeatureCollection.fromFeatures(
                            new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                }

                // Move map camera to the selected location
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                        ((Point) selectedCarmenFeature.geometry()).longitude()))
                                .zoom(14)
                                .build()), 4000);

            }
        }

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Add the symbol layer icon to map for future use
                style.addImage(symbolIconId, BitmapFactory.decodeResource(
                        Contact_Details.this.getResources(), R.drawable.map_default_map_marker));
                // Create an empty GeoJSON source using the empty feature collection
                style.addSource(new GeoJsonSource(geojsonSourceLayerId));
                // Set up a new symbol layer for displaying the searched location's feature coordinates
                style.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                        iconImage(symbolIconId),
                        iconOffset(new Float[] {0f, -8f})
                ));

                if(ct.getAddress()!=null){Show_Location();}
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        finish();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void Directions(View view) {
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try { gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER); } catch(Exception ex) {}

        try { network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER); } catch(Exception ex) {}

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else if(selectedCarmenFeature!=null && gps_enabled && network_enabled) {

            Intent intent = new Intent(this, Main2Activity.class);

            intent.putExtra("Json", selectedCarmenFeature.toJson());

            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Turn on your location.",Toast.LENGTH_SHORT).show();
        }
    }

    public void Show_All(View view) {

        ImageView Edit = findViewById(R.id.edit);
        ImageView Delete = findViewById(R.id.delete);
        if(Edit.getVisibility()==View.INVISIBLE)
        {
            Edit.setVisibility(View.VISIBLE);
            Delete.setVisibility(View.VISIBLE);
        }
        else if(Edit.getVisibility()==View.VISIBLE)
        {
            Edit.setVisibility(View.INVISIBLE);
            Delete.setVisibility(View.INVISIBLE);
        }
    }

    public void Edit_Contact(View view) {
        Intent intent2 = new Intent(this,Edit_Contact.class);
        intent2.putExtra("Id",getIntent().getExtras().getInt("Id"));
        startActivity(intent2);
    }

    public void Delete_Contact(View view) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteContact(ct);
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }
}
