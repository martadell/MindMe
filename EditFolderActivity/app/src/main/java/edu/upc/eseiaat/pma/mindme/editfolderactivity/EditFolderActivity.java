package edu.upc.eseiaat.pma.mindme.editfolderactivity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class EditFolderActivity extends AppCompatActivity {

    private ArrayList<Categories> llistacat = new ArrayList<Categories>();
    private CategoriesAdapter adapter;
    private String[] noms_cat;
    private int[] imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        noms_cat = getResources().getStringArray(R.array.llistacategories);
        imgs = new int[]{R.drawable.altres, R.drawable.animals, R.drawable.electronica,
        R.drawable.llibres, R.drawable.llocs, R.drawable.menjar, R.drawable.musica,
        R.drawable.regals, R.drawable.roba, R.drawable.transport};


        //ARREGLAR ORDRE SPINNER - ACTION BAR per posar la fletxa

        for (int i=0; i<noms_cat.length; i++){
            Drawable drawable = getResources().getDrawable(imgs[i], getTheme());
            llistacat.add(new Categories(noms_cat[i], drawable));
        }

        Collections.sort(llistacat, new Comparator<Categories>() {
            public int compare(Categories obj1, Categories obj2) {
                return obj1.getNom().compareTo(obj2.getNom());
            }
        });

        Spinner llistacategories = (Spinner) findViewById(R.id.llistacategories);

        adapter = new CategoriesAdapter(this, llistacat);

        llistacategories.setAdapter(adapter);

    }


}
