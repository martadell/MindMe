package edu.upc.eseiaat.pma.mindme.mindme;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.net.URI;

/**
 * Created by berta.canes on 11/12/2017.
 */

public class Picture {
    private Uri foto;
    private double lat;
    private double lng;

    public Picture (Uri foto, double lat, double lng){
        this.foto = foto;
        this.lat = lat;
        this.lng = lng;
    }

    public Uri getFoto() { return foto; }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
