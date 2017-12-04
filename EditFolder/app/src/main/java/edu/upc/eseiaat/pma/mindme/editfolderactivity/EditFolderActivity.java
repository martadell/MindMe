package edu.upc.eseiaat.pma.mindme.editfolderactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditFolderActivity extends AppCompatActivity {

    //private ArrayList<> llistacat = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        Spinner llistacategories = (Spinner) findViewById(R.id.llistacategories);
        /*if (llistacat.isEmpty()){
            llistacat.add(new Categories("altres", getResources().getDrawable(R.drawable.altres)));
        }*/

       String[] llistacat = getResources().getStringArray(R.array.llistacategories);
        llistacategories.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistacat));
    }
}
