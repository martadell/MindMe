package edu.upc.eseiaat.pma.mindme.mindme;

/**
 * Created by berta.canes on 11/12/2017.
 */

public class Picture {
    private String foto;
    private double lat;
    private double lng;

    public Picture (String foto, double lat, double lng){
        this.foto = foto;
        this.lat = lat;
        this.lng = lng;
    }

    public String getFoto() { return foto; }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
