package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by berta.canes on 11/12/2017.
 */

public class Picture {
    private Bitmap foto; // Això serà una URI!!!
    private double lat;
    private double lng;

    public Picture(Resources res, int recurs, double lat, double lng) {
        foto = BitmapFactory.decodeResource(res, recurs);
        this.lat = lat;
        this.lng = lng;
    }

    public Picture (Bitmap foto, double lat, double lng){
        this.foto = foto;
        this.lat = lat;
        this.lng = lng;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
