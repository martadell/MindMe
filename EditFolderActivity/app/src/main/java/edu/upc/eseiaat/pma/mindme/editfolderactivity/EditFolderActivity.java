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


        for (int i=0; i<noms_cat.length; i++){
            Drawable drawable = getResources().getDrawable(imgs[i], getTheme());
            llistacat.add(new Categories(noms_cat[i], drawable));
        }
        /*
        llistacat.add(new Categories(noms_cat[0], ));
        llistacat.add(new Categories(noms_cat[1], getResources().getDrawable(R.drawable.animals)));
        llistacat.add(new Categories(noms_cat[2], getResources().getDrawable(R.drawable.electronica)));
        llistacat.add(new Categories(noms_cat[3], getResources().getDrawable(R.drawable.llibres)));
        llistacat.add(new Categories(noms_cat[4], getResources().getDrawable(R.drawable.llocs)));
        llistacat.add(new Categories(noms_cat[5], getResources().getDrawable(R.drawable.menjar)));
        llistacat.add(new Categories(noms_cat[6], getResources().getDrawable(R.drawable.musica)));
        llistacat.add(new Categories(noms_cat[7], getResources().getDrawable(R.drawable.regals)));
        llistacat.add(new Categories(noms_cat[8], getResources().getDrawable(R.drawable.roba)));
        llistacat.add(new Categories(noms_cat[9], getResources().getDrawable(R.drawable.transport)));*/

        Spinner llistacategories = (Spinner) findViewById(R.id.llistacategories);

        adapter = new CategoriesAdapter(this, llistacat);

        llistacategories.setAdapter(adapter);

    }


}
