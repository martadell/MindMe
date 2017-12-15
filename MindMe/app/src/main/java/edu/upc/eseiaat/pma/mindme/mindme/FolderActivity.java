package edu.upc.eseiaat.pma.mindme.mindme;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {

    //private ArrayList<Picture> blabla;
    //private PicturesAdapter picadapter;
    private ArrayList<Picture> llista_fotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        // ArrayAdapter<Picture> adapter = new ArrayAdapter<Picture>(this, R.layout.activity_folder);
        llista_fotos = new ArrayList<>();
        llista_fotos.add(new Picture(getResources(), R.drawable.ic_action_name, 0.0, 0.0));

        PicturesAdapter adapter = new PicturesAdapter(this, R.layout.activity_folder, llista_fotos);

        GridView gridview = (GridView) findViewById(R.id.pic_grid);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(FolderActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
