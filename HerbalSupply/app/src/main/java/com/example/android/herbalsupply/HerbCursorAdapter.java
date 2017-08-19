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

import com.example.android.herbalsupply.data.HerbContract.HydrolatEntry;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 */
public class HerbCursorAdapter extends CursorRecyclerAdapter<HerbCursorAdapter.ViewHolder> {

    // The Listener
    private OnItemClickListener mListener;

    public HerbCursorAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
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
        public TextView quantityGramTextView;
        public TextView quantityMlTextView;

        public ViewHolder(View itemView){
            super(itemView);

            // Find the different component of the viewHolder
            plantImageView = (ImageView) itemView.findViewById(R.id.plant_picture);
            plantNameTextView = (TextView) itemView.findViewById(R.id.plant_name);
            quantityGramTextView = (TextView) itemView.findViewById(R.id.quantity_gram);
            quantityMlTextView = (TextView) itemView.findViewById(R.id.quantity_ml);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {

        final int id = cursor.getInt(cursor.getColumnIndex(HydrolatEntry._ID));

        // Get the index of each column
        int nameColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_NAME);
        int pictureColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_PICTURE);
        int quantityGramColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_QUANTITY_GRAM);
        int quantityMlColumnIndex = cursor.getColumnIndex(HydrolatEntry.COLUMN_HYDRO_QUANTITY_ML);

        // Get the values of each column
        String plantName = cursor.getString(nameColumnIndex);
        String pictureUriString = cursor.getString(pictureColumnIndex);
        Uri pictureUri = Uri.parse(pictureUriString);
        String quantityGram= cursor.getString(quantityGramColumnIndex);
        String quantityMl = cursor.getString(quantityMlColumnIndex);

        // Populate the item with the values from the database
        holder.plantNameTextView.setText(plantName);
        holder.plantImageView.setImageURI(pictureUri);
        holder.plantImageView.invalidate();
        holder.quantityGramTextView.setText(quantityGram);
        holder.quantityMlTextView.setText(quantityMl);

        // Bind a listener to the item
        holder.listenerBinder(id, mListener);
    }
}

