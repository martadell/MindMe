package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berta.canes on 15/12/2017.
 */

public class PicturesAdapter extends ArrayAdapter<Picture> {

    private Context context;

    public PicturesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Picture> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View result = convertView;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.picture,null);
        }

        Picture p = getItem(position);

        ImageView image = (ImageView) result.findViewById(R.id.imagen);
        image.setImageURI(Uri.parse(p.getFoto()));
        return result;
    }
}
