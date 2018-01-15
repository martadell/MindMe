package edu.upc.eseiaat.pma.mindme.provadraglist;

/**
 * Created by Marta on 14/01/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;

public class DragListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    //ADAPTADOR PER LA LLISTA

    private ItemTouchHelper androidItemTouchHelper;
    private List<DragListElement> elements_originals, elements_filtered;
    private DragListElement element;
    private NewFilter filter;
    private final Activity mActivity;
    private Context context;
    private int resource;

    //Constructor
    public DragListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DragListElement> objects,
                           Activity mActivity, ItemTouchHelper androidItemTouchHelper) {
        this.context = context;
        this.resource = resource;
        elements_originals = objects;
        elements_filtered = objects;
        this.mActivity = mActivity;
        this.androidItemTouchHelper = androidItemTouchHelper;
    }

    public void editar_accio (final int position){
        Intent intent = new Intent(getContext(), EditFolderActivity.class);
        intent.putExtra("nom carpeta", elements_originals.get(position).getNom_carpeta());
        intent.putExtra("id drawable", elements_originals.get(position).getRuta_drawable());
        intent.putExtra("posicio carpeta", position);
        ((Activity) getContext()).startActivityForResult(intent, 0);
    }

    public void eliminar_accio (final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.confirmation);
        String message = " " + getItem(position).getNom_carpeta() + "?";
        builder.setMessage(getContext().getResources().getString(R.string.mess) + message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                elements_originals.remove(position);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    public Filter getFilter (){

        if (filter == null){
            filter = new NewFilter();
        }
        return filter;
    }

    private class NewFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                constraint = constraint.toString().toUpperCase();

                List<DragListElement> filtered = new ArrayList();

                for (int i=0; i<elements_filtered.size();i++) {
                    if (elements_filtered.get(i).getNom_carpeta().toUpperCase().contains(constraint)) {
                        filtered.add(elements_filtered.get(i));
                    }
                }

                results.count = filtered.size();
                results.values = filtered;


            } else {
                results.count = elements_filtered.size();
                results.values = elements_filtered;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            elements_originals = (ArrayList<DragListElement>) results.values;
            notifyDataSetChanged();
        }
    }

    public void onItemMove(final int initialPosition, final int finalPosition) {

        if (initialPosition < elements_originals.size() && finalPosition < elements_originals.size()) {

            if (initialPosition < finalPosition) {

                for (int i = initialPosition; i < finalPosition; i++) {
                    Collections.swap(elements_originals, i, i + 1);
                }
            } else {
                for (int i = initialPosition; i > finalPosition; i--) {
                    Collections.swap(elements_originals, i, i - 1);
                }

            }

            notifyItemMoved(initialPosition, finalPosition);

        }

        //
        new Thread(new Runnable() {
            @Override
            public void run() {

                DragListElement mElement1 = elements_originals.get(finalPosition);
                DragListElement mElement2 = elements_originals.get(initialPosition);

                mElement1.setPosicio(finalPosition);
                mElement2.setPosicio(initialPosition);

            }
        }).start();

    }

    private Context getContext() {
        return context;
    }


    public DragListElement getItem(int i) {
        return elements_originals.get(i);
    }

    @Override
    public int getItemCount (){
        return elements_originals.size();
    }


    //Inicialitzar ViewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = View.inflate(mActivity, R.layout.itemfolderlist, null);
        return new viewHolder(v);
    }

    //Colocar els elements al viewholder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof viewHolder) {
            final viewHolder holder = (viewHolder) viewHolder;

            DragListElement mElement = elements_originals.get(i);
            final int position = i;

            holder.image.setImageDrawable(mElement.getIcona());
            holder.nom_carpeta.setText(mElement.getNom_carpeta());
            holder.btn_opcions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.popupMenu = new PopupMenu(view.getContext(), holder.btn_opcions);
                    holder.popupMenu.getMenuInflater().inflate(R.menu.popup_menuopcions, holder.popupMenu.getMenu());

                    holder.popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            return itemClick(menuItem, position);

                        }
                    });

                    holder.popupMenu.show();

                }
            });
        }
    }

    //Gestió opcions del menú
    private boolean itemClick(MenuItem menuItem, int position) {
        switch (menuItem.getItemId()) {
            case R.id.edita:
                editar_accio(position);
                Log.i("marta", "Edita");
                return true;
            case R.id.elimina:
                Log.i("marta", "Elimina");
                eliminar_accio(position);
                String txt = String.format("picture_list_%s_%d.txt", getItem(position).getNom_carpeta(),
                        getItem(position).getRuta_drawable());

                Log.i("marta", "ruta: " + txt);
                getContext().deleteFile(txt);
                return true;
            default:
                return false;

        }
    }

    //Creació de Viewholder ("equivalent" a getView() + onclicklistener per a l'item)
    public class viewHolder extends RecyclerView.ViewHolder {
        public TextView nom_carpeta;
        public ImageView image;
        public final ImageButton btn_opcions;
        public PopupMenu popupMenu;
        public int position;

        public viewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.icona);
            nom_carpeta = v.findViewById(R.id.nom_carpeta);
            btn_opcions = v.findViewById(R.id.btn_opcions);
            position = getLayoutPosition();

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Accedir a la carpeta ->
                    Log.i("marta", "has clicat la carpeta");
                }
            });
        }
    }
}