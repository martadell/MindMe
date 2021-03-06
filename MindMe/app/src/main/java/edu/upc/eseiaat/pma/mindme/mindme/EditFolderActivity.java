package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.MenuItem;
import java.util.ArrayList;

public class EditFolderActivity extends AppCompatActivity {

    private ArrayList<Categories> llistacat = new ArrayList<Categories>();
    private CategoriesAdapter adapter;
    private String[] noms_cat;
    private int[] imgs;
    private int p;
    private EditText nom_carpeta;
    private int posicio_carpeta;
    private int ic_antic;
    private String nom_antic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        //AFEGIR FLETXA ENRERE ACTIONBAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //RECONEIXER ELEMENTS PANTALLA
        nom_carpeta = (EditText) findViewById(R.id.edit_nom);

        //REBRE INTENT
        Intent intent = getIntent();

        String nom = intent.getStringExtra("nom carpeta");
        int codi_ruta = intent.getIntExtra("id drawable", 0);
        posicio_carpeta = intent.getIntExtra("posicio carpeta", -1);
        nom_antic = nom;
        ic_antic = codi_ruta;

        //OMPLIM LA LLISTA DE CATEGORIES

        noms_cat = getResources().getStringArray(R.array.llistacategories);
        imgs = new int[]{R.drawable.altres, R.drawable.animals, R.drawable.electronica,
                R.drawable.llibres, R.drawable.llocs, R.drawable.menjar, R.drawable.musica,
                R.drawable.regals, R.drawable.roba, R.drawable.transport};


        for (int i = 0; i < noms_cat.length; i++) {
            Drawable drawable = ContextCompat.getDrawable(this, imgs[i]);
            llistacat.add(new Categories(noms_cat[i], drawable));
        }

        //CREEM L'ADAPTADOR
        Spinner llistacategories = (Spinner) findViewById(R.id.llistacategories);

        adapter = new CategoriesAdapter(this, llistacat);

        llistacategories.setAdapter(adapter);

        llistacategories.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                           android.view.View v, int position, long id) {
                                p = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        //APLIQUEM INTENT I AGAFEM INFORMACIÓ
        nom_carpeta.setText(nom);

         for (int i=0; i<llistacat.size(); i++){
             if (imgs[i] == codi_ruta){
                 llistacategories.setSelection(i);
             }
         }

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

        afegeix.putExtra("nom carpeta", nc);
        afegeix.putExtra("posicio carpeta", posicio_carpeta);
        afegeix.putExtra("ruta drawable", imgs[p]);
        afegeix.putExtra("nom_antic", nom_antic);
        afegeix.putExtra("icona_antic", ic_antic);
        setResult(RESULT_OK, afegeix);
        finish();
    }

    public void cancel (View view){
        finish();
    }

}