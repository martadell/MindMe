package edu.upc.eseiaat.pma.mindme.mindme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import java.util.Collections;
import java.util.List;

public class FolderListActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    private ItemTouchHelper androidItemTouchHelper;
    private List<Carpeta> carpetes_originals, carpetes_filtered;
    private NewFilter filter;
    private final Activity mActivity;
    private Context context;


    //Constructor
    public FolderListActivityAdapter(@NonNull Context context, @NonNull List<Carpeta> objects,
                           Activity mActivity, ItemTouchHelper androidItemTouchHelper) {
        this.context = context;
        carpetes_originals = objects;
        carpetes_filtered = objects;
        this.mActivity = mActivity;
        this.androidItemTouchHelper = androidItemTouchHelper;
    }

    public void editar_accio (final int position){
        Intent intent = new Intent(getContext(), EditFolderActivity.class);
        intent.putExtra("nom carpeta", carpetes_originals.get(position).getNom_carpeta());
        intent.putExtra("id drawable", carpetes_originals.get(position).getRuta_drawable());
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
                carpetes_originals.remove(position);
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

                List<Carpeta> filtered = new ArrayList<Carpeta>();

                for (int i=0; i<carpetes_filtered.size();i++) {
                    if (carpetes_filtered.get(i).getNom_carpeta().toUpperCase().contains(constraint)) {
                        filtered.add(carpetes_filtered.get(i));
                    }
                }

                results.count = filtered.size();
                results.values = filtered;


            } else {
                results.count = carpetes_filtered.size();
                results.values = carpetes_filtered;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            carpetes_originals = (ArrayList<Carpeta>) results.values;
            notifyDataSetChanged();
        }
    }

    public void onItemMove(final int initialPosition, final int finalPosition) {

        if (initialPosition < carpetes_originals.size() && finalPosition < carpetes_originals.size()) {

            if (initialPosition < finalPosition) {

                for (int i = initialPosition; i < finalPosition; i++) {
                    Collections.swap(carpetes_originals, i, i + 1);
                }
            } else {
                for (int i = initialPosition; i > finalPosition; i--) {
                    Collections.swap(carpetes_originals, i, i - 1);
                }

            }

            notifyItemMoved(initialPosition, finalPosition);

        }

        //
        new Thread(new Runnable() {
            @Override
            public void run() {

                Carpeta mElement1 = carpetes_originals.get(finalPosition);
                Carpeta mElement2 = carpetes_originals.get(initialPosition);

                mElement1.setPosicio(finalPosition);
                mElement2.setPosicio(initialPosition);

            }
        }).start();

    }

    private Context getContext() {
        return context;
    }


    public Carpeta getItem(int i) { return carpetes_originals.get(i); }

    @Override
    public int getItemCount (){
        return carpetes_originals.size();
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

            Carpeta carpeta = carpetes_originals.get(i);

            holder.image.setImageDrawable(
                    ContextCompat.getDrawable(
                            getContext(),
                            Integer.parseInt(String.valueOf(carpeta.getRuta_drawable()))));
            holder.nom_carpeta.setText(carpeta.getNom_carpeta());

        }
    }

    //Gestió opcions del menú
    private boolean itemClick(MenuItem menuItem, int position) {
        switch (menuItem.getItemId()) {
            case R.id.edita:
                editar_accio(position);
                return true;
            case R.id.elimina:
                eliminar_accio(position);
                String txt = String.format("picture_list_%s_%d.txt", getItem(position).getNom_carpeta(),
                        getItem(position).getRuta_drawable());
                getContext().deleteFile(txt);
                return true;
            default:
                return false;

        }
    }

    //Creació de Viewholder ("equivalent" a getView() + onclicklistener per a l'item)
    public class viewHolder extends RecyclerView.ViewHolder{
        public TextView nom_carpeta;
        public ImageView image;
        public final ImageButton btn_opcions;
        public PopupMenu popupMenu;

        public viewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.icona);
            nom_carpeta = v.findViewById(R.id.nom_carpeta);
            btn_opcions = v.findViewById(R.id.btn_opcions);

            //Clic element
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        Carpeta carpeta = carpetes_originals.get(pos);
                        Intent accedir_carpeta = new Intent(getContext(), FolderActivity.class);
                        accedir_carpeta.putExtra("nc", carpeta.getNom_carpeta());
                        accedir_carpeta.putExtra("rd", carpeta.getRuta_drawable());
                        ((Activity) getContext()).startActivityForResult(accedir_carpeta, 0);
                    }
                }

            });

            //Menú
            btn_opcions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupMenu = new PopupMenu(view.getContext(), btn_opcions);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menuopcions, popupMenu.getMenu());

                    final int position = getAdapterPosition();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            return itemClick(menuItem, position);

                        }
                    });

                    popupMenu.show();

                }
            });

        }
    }
}
