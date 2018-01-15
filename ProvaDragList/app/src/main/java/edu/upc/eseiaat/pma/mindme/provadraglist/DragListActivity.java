package edu.upc.eseiaat.pma.mindme.provadraglist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DragListActivity extends AppCompatActivity {

    //ACTIVITAT PRINCIPAL

    private RecyclerView r_v;
    private DragListAdapter adapter;
    private ArrayList<DragListElement> llista_elements = new ArrayList<>();

    private String FILENAME = "DragListElementsList.txt";
    private static final int MAX_BYTES = 10000;

    private ListView l_c;
    private SearchView searchView;
    MenuItem searchitem;
    private String actLay = "galeria";
    private ImageButton btn_add;


    private void writeElementList() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i < llista_elements.size(); i++) {
                DragListElement it = llista_elements.get(i);
                String line = String.format("%s;%d;%s\n",
                        it.getNom_carpeta(),
                        it.getRuta_drawable(),
                        String.format("picture_list_%s_%d.txt", it.getNom_carpeta(), it.getRuta_drawable()));
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("marta", "writeElementList: FileNotFoundException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("marta", "writeElementList: IEOException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }
    }

    private void readElementList(){
        llista_elements = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            if (nread>0) {
                String content = new String(buffer, 0, nread);
                String[] lines = content.split("\n");
                for (String line : lines) {
                    String[] parts = line.split(";");
                    llista_elements.add(new DragListElement(
                            parts[0],
                            ContextCompat.getDrawable(this, Integer.parseInt(parts[1])),
                            Integer.parseInt(parts[1])));
                }
            }
            fis.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Log.i("marta", "readElementList: FileNotFoundException");
        } catch (IOException e) {
            Log.i("marta", "readElementList: IOEException");
            Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeElementList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readElementList();



        setContentView(R.layout.activity_list_drag);

        inicialitzarRecyclerView();
        btn_add = findViewById(R.id.btn_afegircarpeta);
    }

    private void inicialitzarRecyclerView() {
        //fem servir recyclerview en comptes de listveiew perquè permetrà fer el drag&drop
        //el problema és que no té onitemclicklistener i ho farem al adapter
        r_v = findViewById(R.id.list_folders);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        r_v.setLayoutManager(mLayoutManager);

        DynamicEventsHelper.DynamicEventsCallback callback = new DynamicEventsHelper.DynamicEventsCallback() {
            @Override
            public void onItemMove(int initialPosition, int finalPosition) {
                adapter.onItemMove(initialPosition, finalPosition);
            }
        };
        ItemTouchHelper androidItemTouchHelper = new ItemTouchHelper(new DynamicEventsHelper(callback));
        androidItemTouchHelper.attachToRecyclerView(r_v);

        adapter = new DragListAdapter( this, R.layout.activity_edit_folder, llista_elements, this, androidItemTouchHelper);
        r_v.setAdapter(adapter);

    }

    public void btn_afegir (View view){
        Intent intent = new Intent(this, EditFolderActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    String nom = intent.getStringExtra("nom carpeta");
                    int ruta_drawable = intent.getIntExtra("ruta drawable", 0);
                    int posicio_carpeta = intent.getIntExtra("posicio carpeta", -1);

                    Drawable d = ContextCompat.getDrawable(this, ruta_drawable);

                    if (!nom.isEmpty()) {
                        if (posicio_carpeta != -1) {
                            llista_elements.get(posicio_carpeta).setIcona(d);
                            llista_elements.get(posicio_carpeta).setNom_carpeta(nom);
                            llista_elements.get(posicio_carpeta).setRuta_drawable(ruta_drawable);
                        } else {
                            llista_elements.add(new DragListElement(nom, d, ruta_drawable));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        searchitem = menu.findItem(R.id.menuSearch);

        searchView = (SearchView) searchitem.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSearch:

                btn_add.setVisibility(View.GONE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });
                searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewDetachedFromWindow(View arg0) {
                        // search was detached/closed
                        btn_add.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onViewAttachedToWindow(View arg0) {
                        // search was opened
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

