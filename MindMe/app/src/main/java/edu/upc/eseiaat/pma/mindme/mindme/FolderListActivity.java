package edu.upc.eseiaat.pma.mindme.mindme;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FolderListActivity extends AppCompatActivity {

    private ArrayList<Carpeta> llista_carpetes;
    private FolderListActivityAdapter adapter;
    private ListView l_c, searchfolders;

    //TODO versió 18 ordenar la llista amb un longclick (DragList)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);


        l_c = (ListView) findViewById(R.id.list_folders);
        searchfolders = (ListView) findViewById(R.id.search_folders);

        searchfolders.setVisibility(View.INVISIBLE);
        llista_carpetes = new ArrayList<Carpeta>();

        adapter = new FolderListActivityAdapter(this, R.layout.activity_folder_list, llista_carpetes);
        l_c.setAdapter(adapter);


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

                    Bundle bundle = afegir.getExtras();
                    byte[] b = bundle.getByteArray("icona");

                    Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                    Drawable d = new BitmapDrawable(getResources(), bmp);

                    if (!nom.isEmpty()) {
                        if (posicio_carpeta != -1) {
                            llista_carpetes.get(posicio_carpeta).setIcona(d);
                            llista_carpetes.get(posicio_carpeta).setNom_carpeta(nom);
                            llista_carpetes.get(posicio_carpeta).setRuta_drawable(ruta_drawable);
                        } else {
                            llista_carpetes.add(new Carpeta(nom, d, ruta_drawable, new ArrayList<Picture>()));
                        }
                    }
                    l_c.setVisibility(View.VISIBLE);
                    searchfolders.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchitem = menu.findItem(R.id.menuSearch);

        final SearchView searchView = (SearchView) searchitem.getActionView();

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

        return super.onCreateOptionsMenu(menu);
    }
}
