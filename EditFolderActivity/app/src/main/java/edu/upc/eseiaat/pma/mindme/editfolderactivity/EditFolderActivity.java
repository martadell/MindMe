package edu.upc.eseiaat.pma.mindme.editfolderactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class EditFolderActivity extends AppCompatActivity {

    private ArrayList<Categories> llistacat = new ArrayList<Categories>();
    private CategoriesAdapter adapter;
    private String[] noms_cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        noms_cat = getResources().getStringArray(R.array.llistacategories);

       //ListView llistacategories = (ListView) findViewById(R.id.list);
        llistacat.add(new Categories(noms_cat[0], getResources().getDrawable(R.drawable.altres)));
        llistacat.add(new Categories(noms_cat[1], getResources().getDrawable(R.drawable.animals)));
        llistacat.add(new Categories(noms_cat[2], getResources().getDrawable(R.drawable.electronica)));
        llistacat.add(new Categories(noms_cat[3], getResources().getDrawable(R.drawable.llibres)));
        llistacat.add(new Categories(noms_cat[4], getResources().getDrawable(R.drawable.llocs)));
        llistacat.add(new Categories(noms_cat[5], getResources().getDrawable(R.drawable.menjar)));
        llistacat.add(new Categories(noms_cat[6], getResources().getDrawable(R.drawable.musica)));
        llistacat.add(new Categories(noms_cat[7], getResources().getDrawable(R.drawable.regals)));
        llistacat.add(new Categories(noms_cat[8], getResources().getDrawable(R.drawable.roba)));
        llistacat.add(new Categories(noms_cat[9], getResources().getDrawable(R.drawable.transport)));

        Spinner llistacategories = (Spinner) findViewById(R.id.llistacategories);

        adapter = new CategoriesAdapter(this, llistacat);

        llistacategories.setAdapter(adapter);

       /* llistacat.add(new Categories("Altres", getResources().getDrawable(R.drawable.altres)));
        Spinner llistacategories = (Spinner) findViewById(R.id.llistacategories);
        llistacategories.setAdapter(new CategoriesAdapter(this,android.R.layout.simple_spinner_item, llistacat));
        //llistacategories.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories));
        */
    }


}
