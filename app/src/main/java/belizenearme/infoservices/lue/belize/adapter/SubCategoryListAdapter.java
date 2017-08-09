package belizenearme.infoservices.lue.belize.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.databind.SubCategory;

/**
 * Created by lue on 08-06-2017.
 */

public class SubCategoryListAdapter extends ArrayAdapter<SubCategory> {

     private List<SubCategory> subcategoryList;
    Context context;
    int layoutResourceId;

    public SubCategoryListAdapter(Context context, int layoutResourceId, List<SubCategory> subcategoryList)
    {
        super(context, layoutResourceId, subcategoryList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.subcategoryList=subcategoryList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ImageHolder();
            holder.catText = (TextView) row.findViewById(R.id.catText);
            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }

        SubCategory subCategory = subcategoryList.get(position);
        holder.catText.setText(subCategory.getSub_category_name());
        holder = (ImageHolder) row.getTag();
        return row;

    }

    static class ImageHolder {
        TextView catText;
    }
}
