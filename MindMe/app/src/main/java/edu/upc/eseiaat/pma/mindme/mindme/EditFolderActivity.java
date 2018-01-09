package edu.upc.eseiaat.pma.mindme.mindme;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.support.v7.appcompat.R.styleable.MenuItem;

public class EditFolderActivity extends AppCompatActivity {

    //TODO s'haurà de fer algun tipus de distinció depenent de si vens des del botó afegir o d'editar (editar que et mostri ja les dades i modificar-les si alguna canvia, afegir buit)
    private ArrayList<Categories> llistacat = new ArrayList<Categories>();
    private CategoriesAdapter adapter;
    private String[] noms_cat;
    private int[] imgs;
    private Menu menu;
    private int p;
    private EditText nom_carpeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        Intent intent = getIntent();

        ImageButton btn_ok = (ImageButton) findViewById(R.id.btn_ok);

        //OMPLIM LA LLISTA

        noms_cat = getResources().getStringArray(R.array.llistacategories);
        imgs = new int[]{R.drawable.altres, R.drawable.animals, R.drawable.electronica,
                R.drawable.llibres, R.drawable.llocs, R.drawable.menjar, R.drawable.musica,
                R.drawable.regals, R.drawable.roba, R.drawable.transport};


        for (int i = 0; i < noms_cat.length; i++) {
            Drawable drawable = getResources().getDrawable(imgs[i], getTheme());
            llistacat.add(new Categories(noms_cat[i], drawable));
        }

        Collections.sort(llistacat, new Comparator<Categories>() {
            public int compare(Categories obj1, Categories obj2) {
                return obj1.getNom().compareTo(obj2.getNom());
            }
        });

        //CREEM L'ADAPTADOR

        Spinner llistacategories = (Spinner) findViewById(R.id.llistacategories);

        adapter = new CategoriesAdapter(this, llistacat);

        llistacategories.setAdapter(adapter);

        nom_carpeta = (EditText) findViewById(R.id.edit_nom);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        llistacategories.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        p = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ok (View view){

        String nc = nom_carpeta.getText().toString();
        Intent afegeix = new Intent();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), imgs[p]); //AQUI TIENE QUE IR UN INT POSICION DEL DRAWABLE
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        afegeix.putExtra("nom carpeta", nc);
        afegeix.putExtra("icona", byteArray);
        setResult(RESULT_OK, afegeix);
        finish();
    }

}