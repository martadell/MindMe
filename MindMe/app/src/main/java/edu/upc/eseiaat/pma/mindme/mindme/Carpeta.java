package edu.upc.eseiaat.pma.mindme.mindme;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Carpeta {

    private String nom_carpeta;
    private Drawable icona;
    private ArrayList<Picture> llista_fotos;

    public Carpeta (String nom_carpeta, Drawable icona, ArrayList<Picture> llista_fotos){
        this.nom_carpeta = nom_carpeta;
        this.icona = icona;
        this.llista_fotos = llista_fotos;
    }

    public void afegirFoto (Picture foto){
        llista_fotos.add(foto);
    }

    public void eliminarFoto (Picture foto){
        llista_fotos.remove(foto);
    }

    public String getNom_carpeta() {
        return nom_carpeta;
    }

    public void setNom_carpeta(String nom_carpeta) {
        this.nom_carpeta = nom_carpeta;
    }

    public Drawable getIcona() {
        return icona;
    }

    public void setIcona(Drawable icona) {
        this.icona = icona;
    }

    public ArrayList<Picture> getLlista_fotos() {
        return llista_fotos;
    }
}
