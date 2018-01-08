package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriesAdapter extends ArrayAdapter<Categories> {


    private Context context;

    ArrayList<Categories> datos = null;


    public CategoriesAdapter(Context context,ArrayList<Categories> datos) {
        super(context, R.layout.spinner_selected_item, datos);
        this.context = context;
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View result = convertView;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.spinner_selected_item, null);
        }

        Categories cat = datos.get(position);

        TextView title = (TextView) result.findViewById(R.id.cat);
        title.setText(cat.getNom());

        ImageView imagen = (ImageView) result.findViewById(R.id.icon_cat);
        imagen.setImageDrawable(cat.getImatge());

        return result;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.spinner_list_item, parent, false);
        }

        if (row.getTag() == null)
        {
            CategoriesHolder catholder = new CategoriesHolder();
            catholder.setIcono((ImageView) row.findViewById(R.id.icon_cat));
            catholder.setTextView((TextView) row.findViewById(R.id.cat));
            row.setTag(catholder);
        }

        //rellenamos el layout con los datos de la fila que se está procesando
        Categories categoria = datos.get(position);
        ((CategoriesHolder) row.getTag()).getIcono().setImageDrawable(categoria.getImatge());
        ((CategoriesHolder) row.getTag()).getTextView().setText(categoria.getNom());

        return row;
    }

    private static class CategoriesHolder
    {

        private ImageView icono;

        private TextView textView;

        public ImageView getIcono()
        {
            return icono;
        }

        public void setIcono(ImageView icono)
        {
            this.icono = icono;
        }

        public TextView getTextView()
        {
            return textView;
        }

        public void setTextView(TextView textView)
        {
            this.textView = textView;
        }

    }
}
