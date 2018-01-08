package edu.upc.eseiaat.pma.mindme.mindme;


import android.graphics.drawable.Drawable;

public class Categories {

    private String nom;
    private Drawable imatge;

    public Categories (String nom, Drawable imatge){
        this.nom = nom;
        this.imatge = imatge;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Drawable getImatge() {
        return imatge;
    }

    public void setImatge(Drawable imatge) {
        this.imatge = imatge;
    }
}
