package edu.upc.eseiaat.pma.mindme.provamap;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ViewSwitcher simpleViewSwitcher;
    Button btnSwitch;
    Button btn_map;
    Button btn_pic;
    EditText latitude;
    EditText longitude;
    BitmapDescriptor dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnSwitch = (Button) findViewById(R.id.btn_switch);
        btn_map = (Button) findViewById(R.id.btn_map);
        btn_pic = (Button) findViewById(R.id.btn_pic);
        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleViewSwitcher.showNext();
                String act_layout = btnSwitch.getText().toString();
                if (act_layout.equals("TO MAP")){
                    btnSwitch.setText("TO PIC");
                }
                else {
                    btnSwitch.setText("TO MAP");
                }
            }
        });

        btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "Botó pic funciona", Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapActivity.this, marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void addMarker (View v){

        LatLng cordinates = new LatLng(Float.parseFloat(latitude.getText().toString()),
                Float.parseFloat(longitude.getText().toString()));
        dot = BitmapDescriptorFactory.fromResource(R.drawable.dot);

        mMap.addMarker(new MarkerOptions()
                .position(cordinates)
                .icon(dot)
                );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cordinates));
    }
}
