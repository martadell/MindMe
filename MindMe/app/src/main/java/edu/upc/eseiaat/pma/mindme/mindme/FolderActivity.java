package edu.upc.eseiaat.pma.mindme.mindme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ViewSwitcher simpleViewSwitcher;
    private BitmapDescriptor dot;
    private ArrayList<Picture> llista_fotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);

        llista_fotos = new ArrayList<>();
        llista_fotos.add(new Picture(getResources(), R.drawable.ic_action_name, 0.0, 0.0));

        PicturesAdapter adapter = new PicturesAdapter(this, R.layout.activity_folder, llista_fotos);

        GridView gridview = (GridView) findViewById(R.id.pic_grid);
        gridview.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(FolderActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.btn_switch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_switch:
                simpleViewSwitcher.showNext();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(FolderActivity.this, marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void addMarker (View v){

        Double latitude = -0.25;
        Double longitude = 10.3;

        LatLng cordinates = new LatLng(Double.parseDouble(latitude.toString()),
                Double.parseDouble(longitude.toString()));
        dot = BitmapDescriptorFactory.fromResource(R.drawable.dot);

        mMap.addMarker(new MarkerOptions()
                .position(cordinates)
                .icon(dot)
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cordinates));
    }
}
