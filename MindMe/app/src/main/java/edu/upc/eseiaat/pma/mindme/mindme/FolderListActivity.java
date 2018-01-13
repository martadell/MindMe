package edu.upc.eseiaat.pma.mindme.mindme;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.List;

public class FolderListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<Carpeta> llista_carpetes;
    ArrayList<Picture> total_fotos;
    private FolderListActivityAdapter adapter;
    private ListView l_c, searchfolders;
    private SearchView searchView;
    private SearchView mapa;
    private GoogleMap mMap;
    private ViewSwitcher simpleViewSwitcher;
    MenuItem searchitem;
    MenuItem mapatotal;
    private String actLay = "galeria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        l_c = (ListView) findViewById(R.id.list_folders);
        searchfolders = (ListView) findViewById(R.id.search_folders);

        searchfolders.setVisibility(View.INVISIBLE);
        llista_carpetes = new ArrayList<Carpeta>();

        adapter = new FolderListActivityAdapter(this, R.layout.activity_folder_list, llista_carpetes);
        l_c.setAdapter(adapter);

        l_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                 Intent accedir_carpeta = new Intent(FolderListActivity.this, FolderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("llista fotos", llista_carpetes.get(pos).getLlista_fotos());
                accedir_carpeta.putExtras(bundle);
                accedir_carpeta.putExtra("nom carpeta", llista_carpetes.get(pos).getNom_carpeta());
                accedir_carpeta.putExtra("ruta drawable", llista_carpetes.get(pos).getRuta_drawable());
                startActivityForResult(accedir_carpeta, 1);
               // Toast.makeText(FolderListActivity.this, " " + "Click carpeta", Toast.LENGTH_SHORT).show();
            }
        });

        searchfolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
              Intent accedir_carpeta = new Intent(FolderListActivity.this, FolderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("llista fotos", llista_carpetes.get(pos).getLlista_fotos());
                accedir_carpeta.putExtras(bundle);
                accedir_carpeta.putExtra("nom carpeta", llista_carpetes.get(pos).getNom_carpeta());
                accedir_carpeta.putExtra("ruta drawable", llista_carpetes.get(pos).getRuta_drawable());
                startActivityForResult(accedir_carpeta, 1);
                //Toast.makeText(FolderListActivity.this, " " + "Click carpeta search", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void btn_afegir (View view){
        Intent intent = new Intent(this, EditFolderActivity.class);
        startActivityForResult(intent ,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent afegir) {
        switch (requestCode) {
            case 0:
                if (resultCode == AppCompatActivity.RESULT_OK) {

                    String nom = afegir.getStringExtra("nom carpeta");
                    int ruta_drawable = afegir.getIntExtra("ruta drawable", 0);
                    int posicio_carpeta = afegir.getIntExtra("posicio carpeta", -1);

                    Drawable d = ContextCompat.getDrawable(this, ruta_drawable);

                    if (!nom.isEmpty()) {
                        if (posicio_carpeta != -1) {
                            llista_carpetes.get(posicio_carpeta).setIcona(d);
                            llista_carpetes.get(posicio_carpeta).setNom_carpeta(nom);
                            llista_carpetes.get(posicio_carpeta).setRuta_drawable(ruta_drawable);
                        } else {
                            llista_carpetes.add(new Carpeta(nom, d, ruta_drawable, new ArrayList<Picture>()));
                        }
                    }
                    searchfolders.setVisibility(View.INVISIBLE);
                    l_c.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
        }
    }

  //  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        searchitem = menu.findItem(R.id.menuSearch);
        mapatotal = menu.findItem(R.id.mapatotal);

        searchView = (SearchView) searchitem.getActionView();
        mapa = (SearchView) mapatotal.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menuSearch:

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        l_c.setVisibility(View.INVISIBLE);
                        searchfolders.setVisibility(View.VISIBLE);

                        ArrayList<Carpeta> filtered = new ArrayList<Carpeta>();

                        FolderListActivityAdapter searchadapter = new FolderListActivityAdapter(getApplicationContext(), R.layout.activity_folder_list, filtered);
                        searchfolders.setAdapter(searchadapter);

                        for (Carpeta c: llista_carpetes) {
                            if (c.getNom_carpeta().contains(newText)) {
                                filtered.add(c);
                            }
                        }
                        return true;
                    }
                });

                return true;
            case R.id.mapatotal:

                simpleViewSwitcher.showNext();
                if (actLay.equals("galeria")){
                    mapatotal.setIcon(R.drawable.galeria);
                    actLay = "mapa";
                }
                else if (actLay.equals("mapa")){
                    mapatotal.setIcon(R.drawable.mapa);
                    actLay = "galeria";
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        total_fotos = new ArrayList<Picture>();

        for (Carpeta c: llista_carpetes){
            total_fotos.addAll(c.getLlista_fotos());
        }

        if (!total_fotos.isEmpty()) {
            for (int i = 0; i < total_fotos.size(); i++) {
                addMarker(total_fotos.get(i));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            total_fotos.get(total_fotos.size() - 1).getLat(),
                            total_fotos.get(total_fotos.size() - 1).getLng()),
                    5.0f));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i<total_fotos.size(); i++){
                    if (String.valueOf(marker.getPosition().latitude).equals(String.valueOf(total_fotos.get(i).getLat()))
                            && String.valueOf(marker.getPosition().longitude).equals(String.valueOf(total_fotos.get(i).getLng()))){
                        callPopUp(total_fotos.get(i).getFoto());
                    }
                }
                return true;
            }
        });
    }

    private void addMarker(Picture pic){
        LatLng cordinates = new LatLng(pic.getLat(),pic.getLng());
        BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.drawable.dot);

        mMap.addMarker(new MarkerOptions()
                .position(cordinates)
                .icon(dot)
        );
    }

    private void callPopUp (String foto){
        Intent bigImage = new Intent(FolderListActivity.this, PopUpActivity.class);
        bigImage.putExtra("picture", foto);
        startActivity(bigImage);
    }

    //TODO: - Fer que el boto afegir carpeta no es mogui quan passes a mode cerca
    //TODO: - Guardar tot en llistes
    //TODO: - 3 puntets
    //TODO: - DragList V18
    //TODO: - Personalitzar actionbar
    //TODO: - Posar-ho tot per a que es vegi maco al mobil

}