package belizenearme.infoservices.lue.belize.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.ProductDetailsActivity;
import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.ImageLoader;
import belizenearme.infoservices.lue.belize.utilities.MySingleton;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

/**
 * Created by lue on 08-06-2017.
 */

public class ProductListAdapter extends ArrayAdapter<Products> {

     private List<Products> productsList;
    Context context;
    int layoutResourceId;
    ImageLoader imageLoader;

    public ProductListAdapter(Context context, int layoutResourceId, List<Products> productsList)
    {
        super(context, layoutResourceId, productsList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.productsList=productsList;
        imageLoader=new ImageLoader(getContext());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            holder.productImageView = (NetworkImageView) row.findViewById(R.id.productImageView);
            holder.productNameText = (TextView) row.findViewById(R.id.productNameText);
            holder.productLocationText = (TextView) row.findViewById(R.id.productLocationText);
            holder.productPriceText = (TextView) row.findViewById(R.id.productPriceText);
            holder.productDescText = (TextView) row.findViewById(R.id.productDescText);
            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }

        final Products products = productsList.get(position);
        UtilityClass.getImage(getContext(),products.getImages(),holder.productImageView,0);
        holder.productNameText.setText(products.getProduct_name());
        holder.productLocationText.setText(products.getLocation());
        holder.productPriceText.setText(products.getPrice());
        holder.productDescText.setText(products.getDescription());



        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), ProductDetailsActivity.class);
                i.putExtra(AppConstants.PRODUCT_ID,products.getId());
                context.startActivity(i);

            }
        });

        holder = (ImageHolder) row.getTag();
        return row;

    }

    static class ImageHolder {
    NetworkImageView productImageView;
        TextView productNameText,productLocationText,productPriceText,productDescText;
    }




}
