package edu.upc.eseiaat.pma.mindme.provadraglist;

import java.util.Comparator;

/**
 * Created by Marta on 14/01/2018.
 */

public class DragListComparator {

    public final Comparator<DragListElement> ordenarPerPosicio = new Comparator<DragListElement>() {
        @Override
        public int compare(DragListElement mElement1, DragListElement mElement2) {
            return mElement1.getPosicio() - mElement2.getPosicio();
        }
    };
}
