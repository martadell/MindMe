package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class FolderListActivity extends AppCompatActivity implements OnMapReadyCallback{

    private RecyclerView r_v;
    private FolderListActivityAdapter adapter;
    private ArrayList<Carpeta> llista_carpetes;
    private ArrayList<Picture> total_fotos;
    private SearchView searchView;
    private SearchView mapa;
    private GoogleMap mMap;
    private ViewSwitcher simpleViewSwitcher;
    MenuItem searchitem;
    MenuItem mapatotal;
    private String actLay = "galeria";
    private static final String FILENAME = "folder_list.txt";
    private static final int MAX_BYTES = 10000;
    private ImageButton btn_add;

    private void writeFolderList(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i<llista_carpetes.size(); i++){
                Carpeta it = llista_carpetes.get(i);
                String line = String.format("%s;%d;%s\n",
                        it.getNom_carpeta(),
                        it.getRuta_drawable(),
                        String.format("picture_list_%s_%d.txt", it.getNom_carpeta(), it.getRuta_drawable()));
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("berta", "writeItemList: FileNotFoundException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("berta", "writeItemList: IEOException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }
    }

    private void readFolderList(){
        llista_carpetes = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            if (nread>0) {
                String content = new String(buffer, 0, nread);
                String[] lines = content.split("\n");
                for (String line : lines) {
                    String[] parts = line.split(";");
                    llista_carpetes.add(new Carpeta(
                            parts[0],
                            ContextCompat.getDrawable(this, Integer.parseInt(parts[1])),
                            Integer.parseInt(parts[1])));
                }
            }
            fis.close();
        } catch (FileNotFoundException e) {
            Log.i("berta", "readItemList: FileNotFoundException");
        } catch (IOException e) {
            Log.i("berta", "readItemList: IOEException");
            Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeFolderList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        readFolderList();

        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btn_add =(ImageButton) findViewById(R.id.btn_afegircarpeta);

        inicialitzarRecyclerView();
    }

    private void inicialitzarRecyclerView() {
        //fem servir recyclerview en comptes de listveiew perquè permetrà fer el drag&drop
        //el problema és que no té onitemclicklistener i ho farem al adapter
        r_v = findViewById(R.id.list_folders);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        r_v.setLayoutManager(mLayoutManager);

        DynamicEventsHelper.DynamicEventsCallback callback = new DynamicEventsHelper.DynamicEventsCallback() {
            @Override
            public void onItemMove(int initialPosition, int finalPosition) {
                adapter.onItemMove(initialPosition, finalPosition);
            }
        };
        ItemTouchHelper androidItemTouchHelper = new ItemTouchHelper(new DynamicEventsHelper(callback));
        androidItemTouchHelper.attachToRecyclerView(r_v);

        adapter = new FolderListActivityAdapter(this, llista_carpetes, this, androidItemTouchHelper);
        r_v.setAdapter(adapter);

    }

    public void btn_afegir (View view){
        Intent intent = new Intent(this, EditFolderActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    String nom = intent.getStringExtra("nom carpeta");
                    int ruta_drawable = intent.getIntExtra("ruta drawable", 0);
                    int posicio_carpeta = intent.getIntExtra("posicio carpeta", -1);
                    String nom_ant = intent.getStringExtra("nom_antic");
                    int icona_antic = intent.getIntExtra("icona_antic", 0);

                    Drawable d = ContextCompat.getDrawable(this, ruta_drawable);

                    if (!nom.isEmpty()) {
                        if (posicio_carpeta != -1) {
                            llista_carpetes.get(posicio_carpeta).setIcona(d);
                            llista_carpetes.get(posicio_carpeta).setNom_carpeta(nom);
                            llista_carpetes.get(posicio_carpeta).setRuta_drawable(ruta_drawable);
                            String nom_fitxer_ant = String.format("picture_list_%s_%d.txt",
                                    nom_ant,
                                    icona_antic);
                            String nom_fitxer_nou = String.format("picture_list_%s_%d.txt",
                                    nom,
                                    ruta_drawable);
                            File oldfile = this.getFileStreamPath(nom_fitxer_ant);
                            File newfile = this.getFileStreamPath(nom_fitxer_nou);
                            oldfile.renameTo(newfile);
                            this.deleteFile(nom_fitxer_ant);
                        } else {
                            llista_carpetes.add(new Carpeta(nom, d, ruta_drawable));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
        }
    }

    @Override
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
        switch (item.getItemId()) {
            case R.id.menuSearch:

                btn_add.setVisibility(View.GONE);
                mapatotal.setVisible(false);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });
                searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewDetachedFromWindow(View arg0) {
                        // search was detached/closed
                        btn_add.setVisibility(View.VISIBLE);
                        mapatotal.setVisible(true);
                    }
                    @Override
                    public void onViewAttachedToWindow(View arg0) {
                        // search was opened
                    }
                });
                return true;
            case R.id.mapatotal:

                simpleViewSwitcher.showNext();
                if (actLay.equals("galeria")){
                    mapatotal.setIcon(R.drawable.galeria);
                    searchitem.setVisible(false);
                    actLay = "mapa";
                    showAllPic();
                }
                else if (actLay.equals("mapa")){
                    mapatotal.setIcon(R.drawable.mapa);
                    searchitem.setVisible(true);
                    actLay = "galeria";
                    mMap.clear();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

    private void showAllPic (){
        total_fotos = new ArrayList<Picture>();

        for (int i = 0; i<llista_carpetes.size(); i++){
            String nom = String.format("picture_list_%s_%d.txt",
                    llista_carpetes.get(i).getNom_carpeta(),
                    llista_carpetes.get(i).getRuta_drawable());
            try {
                FileInputStream fis = openFileInput(nom);
                byte[] buffer = new byte[MAX_BYTES];
                int nread = fis.read(buffer);
                if (nread>0) {
                    String content = new String(buffer, 0, nread);
                    String[] lines = content.split("\n");
                    for (String line : lines) {
                        String[] parts = line.split(";");
                        total_fotos.add(new Picture(
                                parts[0],
                                Double.parseDouble(parts[1].replace(',', '.')),
                                Double.parseDouble(parts[2].replace(',', '.'))));
                    }
                }
                fis.close();
            } catch (FileNotFoundException e) {
                Log.i("berta", "readItemList: FileNotFoundException");
            } catch (IOException e) {
                Log.i("berta", "readItemList: IOEException");
                Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();
            }
        }

        if (!total_fotos.isEmpty()) {
            for (int i = 0; i<total_fotos.size(); i++) {
                addMarker(total_fotos.get(i));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            total_fotos.get(total_fotos.size() - 1).getLat(),
                            total_fotos.get(total_fotos.size() - 1).getLng()),
                    8.0f));
        }
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
}