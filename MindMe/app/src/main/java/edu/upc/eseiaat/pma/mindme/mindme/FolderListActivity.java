package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FolderListActivity extends AppCompatActivity {

    private ArrayList<Carpeta> llista_carpetes;
    private FolderListActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);

        ListView l_c = (ListView) findViewById(R.id.list_folders);

        llista_carpetes = new ArrayList<Carpeta>();

        //TODO fer icona afegir
        //TODO afegir l'intent quan es clica el botó afegir carpeta
        //AIXÒ PASSA QUAN ES FA CLICK AL BOtÓ D'AFEGIR!!!! --> ARREGLAR NÚRIA
        /*Intent intent = new Intent (this, EditFolderActivity.class);
        startActivityForResult(intent ,0);*/

        adapter = new FolderListActivityAdapter(this, R.layout.activity_folder_list, llista_carpetes);
        l_c.setAdapter(adapter);
    }
}
