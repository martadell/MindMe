package edu.upc.eseiaat.pma.mindme.provamap;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnSwitch = (Button) findViewById(R.id.btn_switch);
        btn_map = (Button) findViewById(R.id.btn_map);
        btn_pic = (Button) findViewById(R.id.btn_pic);
        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);

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

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "Botó map funciona", Toast.LENGTH_SHORT).show();
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

        // Add a marker in ESEIAAT, Terrassa, and move the camera.
        final LatLng eseiaat = new LatLng(41.562863, 2.023187);
        mMap.addMarker(new MarkerOptions()
                .position(eseiaat)
                //.title("Marker in ESEIAAT") //decidir si volem text o no
                //.snippet("This is Univerity") //decidir si volem text o no
                //TODO: canviar la icona a un punt
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eseiaat));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapActivity.this, eseiaat.toString(), Toast.LENGTH_SHORT).show();
                // si true: no mostra el text del marcador, si false: mostra el text del marcador
                return true;
            }
        });
    }
}
