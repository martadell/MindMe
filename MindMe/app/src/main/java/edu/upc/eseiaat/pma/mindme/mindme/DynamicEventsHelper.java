package edu.upc.eseiaat.pma.mindme.mindme;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Marta on 16/01/2018.
 */

public class DynamicEventsHelper extends ItemTouchHelper.Callback {

    private DynamicEventsCallback callback;

    public DynamicEventsHelper(DynamicEventsCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.END | ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        callback.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //no el fem servir
    }

    public interface DynamicEventsCallback {
        void onItemMove(int initialPosition, int finalPosition);
    }


}