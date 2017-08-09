package belizenearme.infoservices.lue.belize.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.ProductDetailsActivity;
import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.adapter.ProductListAdapter;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;

/**
 * Created by lue on 08-06-2017.
 */

public class ProductListFragment extends Fragment{

    GridView gridView;

    List<Products> productsList;
    int catId,subCatId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list_fragment, container, false);
        gridView=(GridView)view.findViewById(R.id.gridView);


        try
        {
         Bundle bundle=getArguments();
            catId=bundle.getInt(AppConstants.CATEGROY_ID);
            subCatId=bundle.getInt(AppConstants.SUB_CATEGROY_ID);

        }catch (Exception e){}

        getProducts(catId,subCatId);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    public void getProducts(int catId, int subCatId)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.accumulate("category_id",catId);
            jsonObject.accumulate("sub_category_id",subCatId);
        } catch (JSONException e) {}
        new DownloadThread(getActivity(), Urls.getProducts,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {
                        productsList=new ArrayList<Products>();
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            productsList.add(new Products(jsonArray.getJSONObject(i)));
                        }

                        ProductListAdapter productListAdapter=new ProductListAdapter(getContext(),R.layout.products,productsList);
                        gridView.setAdapter(productListAdapter);

                    }
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }


}
