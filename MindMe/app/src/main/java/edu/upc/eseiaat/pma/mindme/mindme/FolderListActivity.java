package edu.upc.eseiaat.pma.mindme.mindme;

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
        ArrayList<Picture> hola = new ArrayList<Picture>();
        llista_carpetes.add(new Carpeta("HOLA", getResources().getDrawable(R.drawable.ic_action_name, getTheme()),
                hola));
        adapter = new FolderListActivityAdapter(this, R.layout.activity_folder_list, llista_carpetes);
        l_c.setAdapter(adapter);
    }
}
