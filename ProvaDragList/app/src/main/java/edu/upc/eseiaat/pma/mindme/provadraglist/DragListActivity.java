package edu.upc.eseiaat.pma.mindme.provadraglist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DragListActivity extends AppCompatActivity {

    //ACTIVITAT PRINCIPAL

    private RecyclerView mRecyclerView;
    private DragListAdapter mAdapter;
    private static ArrayList<DragListElement> llista_elements = new ArrayList<>();
    private EditText edit_text;

    private String FILENAME = "DragListElementList.txt";
    private static final int MAX_BYTES = 10000;

    private void writeElementList(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i<llista_elements.size(); i++){
                DragListElement it = llista_elements.get(i);
                String line = String.format("%s;%d\n",
                        it.getNom(),
                        it.getPosicio());
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
                            parts[0],Integer.parseInt(parts[1])));
                }
            }
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

        setContentView(R.layout.activity_list_drag);

        edit_text = (EditText) findViewById(R.id.edit_text);
        readElementList();
        inicialitzarRecyclerView();
    }

    private void inicialitzarRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DynamicEventsHelper.DynamicEventsCallback callback = new DynamicEventsHelper.DynamicEventsCallback() {
            @Override
            public void onItemMove(int initialPosition, int finalPosition) {
                mAdapter.onItemMove(initialPosition, finalPosition);
            }
        };
        ItemTouchHelper androidItemTouchHelper = new ItemTouchHelper(new DynamicEventsHelper(callback));
        androidItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mAdapter = new DragListAdapter( this, androidItemTouchHelper);
        mRecyclerView.setAdapter(mAdapter);

    }

    public static ArrayList <DragListElement> getLlista_elements() {
        return llista_elements;
    }


    public void btn_add(View view) {
        String text = edit_text.getText().toString();
        Log.i("marta", text);

        llista_elements.add(new DragListElement(text, llista_elements.size()));
        mAdapter.notifyDataSetChanged();
        edit_text.setText("");

        writeElementList();

    }

}

