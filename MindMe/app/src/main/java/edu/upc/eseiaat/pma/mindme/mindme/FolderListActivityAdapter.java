package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FolderListActivityAdapter extends ArrayAdapter<Carpeta> {

    public FolderListActivityAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Carpeta> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View result = convertView;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.itemfolderlist,null);
        }

        Carpeta c = getItem(position);

        ImageView image = (ImageView) result.findViewById(R.id.icona);
        image.setImageDrawable(c.getIcona());

        TextView nom_carpeta = (TextView) result.findViewById(R.id.nom_carpeta);
        nom_carpeta.setText(c.getNom_carpeta());

        return result;
    }

}
