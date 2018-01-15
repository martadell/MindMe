package edu.upc.eseiaat.pma.mindme.provadraglist;

import android.graphics.Picture;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marta on 14/01/2018.
 */

public class DragListElement implements Serializable {
    private String nom_carpeta;
    private Drawable icona;
    private ArrayList<Picture> llista_fotos;
    private int ruta_drawable;
    private int posicio; //NOU! S'AFEGEIX LA POSICIÓ (PER PODER ORDENAR-HO)

    //DRAGLISTELEMENT -> CARPETA

    public DragListElement (String nom_carpeta, Drawable icona, int ruta_drawable, ArrayList<Picture> llista_fotos){
        this.nom_carpeta = nom_carpeta;
        this.icona = icona;
        this.llista_fotos = llista_fotos;
        this.ruta_drawable = ruta_drawable;
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

    public int getRuta_drawable (){
        return ruta_drawable;
    }

    public void setRuta_drawable (int ruta_drawable){
        this.ruta_drawable = ruta_drawable;
    }

    public void setIcona(Drawable icona) {
        this.icona = icona;
    }

    public ArrayList<Picture> getLlista_fotos() {
        return llista_fotos;
    }

    public void setLlista_fotos(ArrayList<Picture> llista_fotos) {
        this.llista_fotos = llista_fotos;
    }

    public int getPosicio() {
        return posicio;
    }

    public void setPosicio(int posicio) {
        this.posicio = posicio; }
}

