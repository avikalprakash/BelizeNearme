package belizenearme.infoservices.lue.belize.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import belizenearme.infoservices.lue.belize.MainActivity;
import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Products> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Products> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Products products=mValues.get(position);
        UtilityClass.loadImage(MainActivity.mainActivity,products.getImages(),holder.productImageView);
        holder.productNameText.setText(products.getProduct_name());
        holder.productLocationText.setText(products.getLocation());
        holder.productPriceText.setText(products.getPrice());
        holder.productDescText.setText(products.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(products);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView productImageView;
        TextView productNameText,productLocationText,productPriceText,productDescText;

        public ViewHolder(View view) {
            super(view);
            productImageView = (NetworkImageView) view.findViewById(R.id.productImageView);
            productNameText = (TextView) view.findViewById(R.id.productNameText);
            productLocationText = (TextView) view.findViewById(R.id.productLocationText);
            productPriceText = (TextView) view.findViewById(R.id.productPriceText);
            productDescText = (TextView) view.findViewById(R.id.productDescText);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + productNameText.getText() + "'";
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Products item);
    }
}
