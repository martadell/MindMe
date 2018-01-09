package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

                    Bundle bundle = afegir.getExtras();
                    byte[] b = bundle.getByteArray("icona");

                    Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                    Drawable d = new BitmapDrawable(getResources(), bmp);

                    llista_carpetes.add(new Carpeta(nom, d, new ArrayList<Picture>()));
                    adapter.notifyDataSetChanged();
                }
        }
    }
}
