package com.example.android.herbalsupply;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.herbalsupply.data.HerbContract.PlantEntry;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 */
public class HerbCursorAdapter extends CursorRecyclerAdapter<HerbCursorAdapter.ViewHolder> {

    // Instance of the StockActivity
    private StockActivity activity = new StockActivity();

    // The Listener
    private OnItemClickListener mListener;

    public HerbCursorAdapter(StockActivity context, Cursor cursor, OnItemClickListener listener) {
        super(context,cursor);
        this.activity = context;
        this.mListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(long id);
    }

    // The ViewHolder which caches the ImageView, the sell Button and the three TextViews
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView plantImageView;
        public TextView plantNameTextView;
        public TextView quantityTextView;
        public TextView priceTextView;
        public Button sellButton;

        public ViewHolder(View itemView){
            super(itemView);

            // Find the different component of the viewHolder
            plantImageView = (ImageView) itemView.findViewById(R.id.plant_picture);
            plantNameTextView = (TextView) itemView.findViewById(R.id.plant_name);
            quantityTextView = (TextView) itemView.findViewById(R.id.quantity);
            priceTextView = (TextView) itemView.findViewById(R.id.price);
            sellButton = (Button) itemView.findViewById(R.id.sell_button);
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

        final int id = cursor.getInt(cursor.getColumnIndex(PlantEntry._ID));

        // Get the index of each column
        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_NAME);
        int pictureColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_PICTURE);
        int quantityColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_PRICE);

        // Get the values of each column
        String plantName = cursor.getString(nameColumnIndex);
        String pictureUriString = cursor.getString(pictureColumnIndex);
        Uri pictureUri = Uri.parse(pictureUriString);
        int quantity = cursor.getInt(quantityColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);

        // Set the quantity to a global variable
        final int mQuantity = quantity;

        // Populate the item with the values from the database
        holder.plantNameTextView.setText(plantName);
        holder.plantImageView.setImageURI(pictureUri);
        holder.plantImageView.invalidate();
        holder.priceTextView.setText(String.valueOf(productPrice));
        holder.quantityTextView.setText(String.valueOf(quantity));

        // Setup a listener on the sell button
        holder.sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mQuantity >0 ) {
                    activity.onSellButtonClicked(id, mQuantity);
                } else {
                    Toast.makeText(activity, "Not in stock. Need to order.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Bind a listener to the item
        holder.listenerBinder(id, mListener);
    }
}

