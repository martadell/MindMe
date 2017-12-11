package edu.upc.eseiaat.pma.mindme.mindme;

import android.graphics.Bitmap;

/**
 * Created by berta.canes on 11/12/2017.
 */

public class Picture {
    private Bitmap foto;
    private double lat;
    private double lng;

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
