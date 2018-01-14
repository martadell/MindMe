package edu.upc.eseiaat.pma.mindme.provadraglist;

/**
 * Created by Marta on 14/01/2018.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class DragListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //ADAPTADOR PER LA LLISTA

    private ItemTouchHelper androidItemTouchHelper;

    private ArrayList<DragListElement> mList = new ArrayList();
    private final Activity mActivity;

    public DragListAdapter(Activity mActivity, ItemTouchHelper androidItemTouchHelper) {
        this.androidItemTouchHelper = androidItemTouchHelper;
        this.mActivity = mActivity;
        mList = new ArrayList<>();

        update();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = View.inflate(mActivity, R.layout.rv_view, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;

            DragListElement mElement = mList.get(i);

            holder.tvNom.setText(mElement.getNom());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update() {
        mList = DragListActivity.getLlista_elements();
        Collections.sort(mList, new DragListComparator().ordenarPerPosicio);
        notifyDataSetChanged();
    }

    public void onItemMove(final int initialPosition, final int finalPosition) {

        if (initialPosition < mList.size() && finalPosition < mList.size()) {

            if (initialPosition < finalPosition) {

                for (int i = initialPosition; i < finalPosition; i++) {
                    Collections.swap(mList, i, i + 1);
                }
            } else {
                for (int i = initialPosition; i > finalPosition; i--) {
                    Collections.swap(mList, i, i - 1);
                }

            }

            notifyItemMoved(initialPosition, finalPosition);

        }

        //
        new Thread(new Runnable() {
            @Override
            public void run() {

                DragListElement mElement1 = mList.get(finalPosition);
                DragListElement mElement2 = mList.get(initialPosition);

                mElement1.setPosicio(finalPosition);
                mElement2.setPosicio(initialPosition);

            }
        }).start();

    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNom;

        MyViewHolder(View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tv_nom);

        }


    }


}