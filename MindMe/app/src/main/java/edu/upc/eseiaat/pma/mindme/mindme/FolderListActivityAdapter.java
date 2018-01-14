package edu.upc.eseiaat.pma.mindme.mindme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FolderListActivityAdapter extends ArrayAdapter<Carpeta> implements Filterable{

    private List<Carpeta>  carpetes_originals;
    private Carpeta c;
    private NewFilter filter;


    public FolderListActivityAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Carpeta> objects) {
        super(context, resource, objects);
        carpetes_originals = objects;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View result = convertView;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.itemfolderlist,null);
        }
            c = getItem(position);

            ImageView image = (ImageView) result.findViewById(R.id.icona);
            image.setImageDrawable(c.getIcona());

            TextView nom_carpeta = (TextView) result.findViewById(R.id.nom_carpeta);
            nom_carpeta.setText(c.getNom_carpeta());

            final ImageButton btn_opcions = (ImageButton) result.findViewById(R.id.btn_opcions);
            btn_opcions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(getContext(), btn_opcions);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menuopcions, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edita:
                                    editar_accio(position);
                                    return true;
                                case R.id.elimina:
                                    eliminar_accio(position);
                                    return true;
                                default:
                                    return false;

                            }

                            //Toast.makeText(getContext(), " " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    popupMenu.show();

                }
            });

        return result;
    }

    public void editar_accio (int position){
        Intent intent = new Intent(getContext(), EditFolderActivity.class);
        intent.putExtra("nom carpeta", c.getNom_carpeta());
        intent.putExtra("id drawable", c.getRuta_drawable());
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

                for (int i=0; i<carpetes_originals.size();i++) {
                    if (carpetes_originals.get(i).getNom_carpeta().toUpperCase().contains(constraint)) {

                    }
                }

                results.count = filtered.size();
                results.values = filtered;


            } else {
                results.count = carpetes_originals.size();
                results.values = carpetes_originals;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            carpetes_originals = (ArrayList<Carpeta>) results.values;
            notifyDataSetChanged();
        }

    }
}
