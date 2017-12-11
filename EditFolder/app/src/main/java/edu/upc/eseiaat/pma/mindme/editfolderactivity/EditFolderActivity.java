package edu.upc.eseiaat.pma.mindme.editfolderactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditFolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        Spinner list = (Spinner) findViewById(R.id.list);
        String[] listcat = {"A", "B", "C"};
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listcat));
    }
}
