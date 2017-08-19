package com.example.android.herbalsupply;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.herbalsupply.data.HerbContract.ContenEntry;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 */
public class ContenCursorAdapter extends CursorRecyclerAdapter<ContenCursorAdapter.ViewHolder> {

    // The Listener
    private OnItemClickListener mListener;

    public ContenCursorAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        super(context,cursor);
        this.mListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(long id);
    }

    // The ViewHolder which caches the ImageView, the sell Button and the three TextViews
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView plantImageView;
        public TextView plantNameTextView;
        public TextView quantityTotalTextView;
        public TextView quantityDispoTextView;

        public ViewHolder(View itemView){
            super(itemView);

            // Find the different component of the viewHolder
            plantImageView = (ImageView) itemView.findViewById(R.id.plant_picture);
            plantNameTextView = (TextView) itemView.findViewById(R.id.plant_name);
            quantityTotalTextView = (TextView) itemView.findViewById(R.id.quantity_total);
            quantityDispoTextView = (TextView) itemView.findViewById(R.id.quantity_dispo);
        }
        // Bind a listener to an item (Plant product)
        private void listenerBinder(final long id, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(id);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conten_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {

        final int id = cursor.getInt(cursor.getColumnIndex(ContenEntry._ID));

        // Get the index of each column
        int nameColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_NAME);
        int pictureColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_PICTURE);
        int quantityTotalColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_QUANTITY_TOTAL);
        int quantityDispoColumnIndex = cursor.getColumnIndex(ContenEntry.COLUMN_CONTEN_QUANTITY_DISPO);

        // Get the values of each column
        String plantName = cursor.getString(nameColumnIndex);
        String pictureUriString = cursor.getString(pictureColumnIndex);
        Uri pictureUri = Uri.parse(pictureUriString);
        String quantityTotal= cursor.getString(quantityTotalColumnIndex);
        String quantityDispo = cursor.getString(quantityDispoColumnIndex);

        // Populate the item with the values from the database
        holder.plantNameTextView.setText(plantName);
        holder.plantImageView.setImageURI(pictureUri);
        holder.plantImageView.invalidate();
        holder.quantityTotalTextView.setText(quantityTotal);
        holder.quantityDispoTextView.setText(quantityDispo);

        // Bind a listener to the item
        holder.listenerBinder(id, mListener);
    }
}

