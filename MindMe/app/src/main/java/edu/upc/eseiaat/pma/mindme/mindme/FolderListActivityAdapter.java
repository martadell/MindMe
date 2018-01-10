package edu.upc.eseiaat.pma.mindme.mindme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FolderListActivityAdapter extends ArrayAdapter<Carpeta> {

    private List<Carpeta> carpetes;
    private Carpeta c;

    public FolderListActivityAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Carpeta> objects) {
        super(context, resource, objects);
        carpetes = objects;
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
                popupMenu.getMenuInflater().inflate(R.menu.popup_menuopcions, popupMenu.getMenu() );

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edita:
                                Intent intent = new Intent(getContext(), EditFolderActivity.class);
                                intent.putExtra("nom carpeta", c.getNom_carpeta());
                                intent.putExtra("id drawable", c.getRuta_drawable());
                                intent.putExtra("posicio carpeta", position);
                                ((Activity) getContext()).startActivityForResult(intent, 0);
                            case R.id.elimina:
                                /*Intent intent1 = new Intent(getContext(), FolderListActivity.class);
                                carpetes.remove(position);
                                getContext().startActivity(intent1);
                                return true;*/
                            default:return false;

                        }

                        //Toast.makeText(getContext(), " " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });

                popupMenu.show();

            }
        });

        return result;
    }

}
