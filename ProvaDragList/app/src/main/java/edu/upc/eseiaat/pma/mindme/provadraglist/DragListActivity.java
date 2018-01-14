package edu.upc.eseiaat.pma.mindme.provadraglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class DragListActivity extends AppCompatActivity {

    //ACTIVITAT PRINCIPAL

        private RecyclerView mRecyclerView;
        private DragListAdapter mAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_drag);
            crearObjectes();
            inicialitzarRecyclerView();
        }

        private void crearObjectes() {

            //creació de la taula en blanc en cas de no existir
            DragListDataBase.getInstancia(this).createTable();

            //Verificar si els items ja hi són, sino s'afegeixen
            if (DragListDataBase.getInstancia(this).getElements().size() > 0) return;


            // creació dels objectes (elemets)
            for (int i = 1; i <= 20; i++) {
                DragListElement mElement = new DragListElement();
                mElement.setNom("Element #" + i);
                mElement.setPosicio(i);
                DragListDataBase.getInstancia(this).addElement(mElement);
            }

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

                @Override
                public void removeItem(int position) {
                    //no el fem servir
                }
            };
            ItemTouchHelper androidItemTouchHelper = new ItemTouchHelper(new DynamicEventsHelper(callback));
            androidItemTouchHelper.attachToRecyclerView(mRecyclerView);

            mAdapter = new DragListAdapter( this, androidItemTouchHelper);
            mRecyclerView.setAdapter(mAdapter);


        }


    }
