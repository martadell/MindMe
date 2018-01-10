package edu.upc.eseiaat.pma.mindme.mindme;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class FolderListActivity extends AppCompatActivity {

    private ArrayList<Carpeta> llista_carpetes;
    private FolderListActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        /*Intent intent1 = getIntent();
        int position = intent1.getIntExtra("posicio", 0);
        llista_carpetes.remove(position);*/

        ListView l_c = (ListView) findViewById(R.id.list_folders);

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

                    if (posicio_carpeta != -1){
                        llista_carpetes.get(posicio_carpeta).setIcona(d);
                        llista_carpetes.get(posicio_carpeta).setNom_carpeta(nom);
                        llista_carpetes.get(posicio_carpeta).setRuta_drawable(ruta_drawable);
                    }
                    else {
                        llista_carpetes.add(new Carpeta(nom, d, ruta_drawable, new ArrayList<Picture>()));
                    }
                    adapter.notifyDataSetChanged();
                }
        }
    }

    //TODO acitonbar buscar
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }*/

}
