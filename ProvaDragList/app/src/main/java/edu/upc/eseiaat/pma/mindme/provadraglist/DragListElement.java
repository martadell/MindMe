package edu.upc.eseiaat.pma.mindme.provadraglist;

/**
 * Created by Marta on 14/01/2018.
 */

public class DragListElement {
    private String nom;

        //variable necessària per saber la posició de l'item al recycleview
    private int posicio;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPosicio() {
        return posicio;
    }

    public void setPosicio(int posicio) {
        this.posicio = posicio;
    }
}

