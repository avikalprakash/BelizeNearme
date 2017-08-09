package belizenearme.infoservices.lue.belize.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.MainActivity;
import belizenearme.infoservices.lue.belize.ProductDetailsActivity;
import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.adapter.MyItemRecyclerViewAdapter;
import belizenearme.infoservices.lue.belize.adapter.ProductListAdapter;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class NewDealFragment extends Fragment implements View.OnClickListener,NestedScrollView.OnScrollChangeListener,MyItemRecyclerViewAdapter.OnListFragmentInteractionListener{

    AppCompatTextView loadmoreText;
    GridView gridView;
    List<Products> productsList;
    int catId,subCatId;
    LinearLayout controlLayout;
    NestedScrollView nestedScrollView;
    int increment=0;
    ProductListAdapter productListAdapter;
    RecyclerView recyclerView;
    MyItemRecyclerViewAdapter.OnListFragmentInteractionListener listener;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        productsList=new ArrayList<Products>();
        productListAdapter=new ProductListAdapter(getContext(),R.layout.products,productsList);
        initialize(view);
        return view;
    }


    private void initialize(View view)
    {
        initializeAdView(view);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), MainActivity.mainActivity.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        MainActivity.mainActivity.drawer.setDrawerListener(toggle);
        toggle.syncState();

       listener=this;
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myItemRecyclerViewAdapter=new MyItemRecyclerViewAdapter(productsList,listener);

                gridView=(GridView)view.findViewById(R.id.gridView);
        nestedScrollView=(NestedScrollView) view.findViewById(R.id.nestedScrollView);

        nestedScrollView.setOnScrollChangeListener(this);

        loadmoreText=(AppCompatTextView)view.findViewById(R.id.loadmoreText);
        loadmoreText.setVisibility(View.GONE);
        loadmoreText.setOnClickListener(this);

        catId=getArguments().getInt(AppConstants.CATEGROY_ID);
        subCatId=getArguments().getInt(AppConstants.SUB_CATEGROY_ID);

        if(catId==0 && subCatId==0)  getAllProducts(++increment);
        else getProducts(catId,subCatId,++increment);

    }

    private void initializeAdView(View view)
    {
        AdView ad_view=(AdView)view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("motorola-moto_g__5__plus-ZY22423RM3")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .setRequestAgent("android_studio:ad_template")
                .build();
        ad_view.loadAd(adRequest);
    }


    public void getAllProducts(int inc)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.accumulate("increment",inc);
        } catch (JSONException e) {}
        new DownloadThread(getActivity(), Urls.getAllProducts,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            productsList.add(new Products(jsonArray.getJSONObject(i)));
                        }

                     //   gridView.setAdapter(productListAdapter);
                        recyclerView.setAdapter(myItemRecyclerViewAdapter);
                        myItemRecyclerViewAdapter.notifyDataSetChanged();

                        productListAdapter.notifyDataSetChanged();
                        UtilityClass.setGridViewHeightBasedOnChildren(gridView);

                        if(productsList.size()>=10) loadmoreText.setVisibility(View.VISIBLE);
                        else loadmoreText.setVisibility(View.GONE);

                    }
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }



    public void getProducts(int catId, int subCatId,int inc)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.accumulate("category_id",catId);
            jsonObject.accumulate("sub_category_id",subCatId);
            jsonObject.accumulate("increment",inc);
        } catch (JSONException e) {}
        new DownloadThread(getActivity(), Urls.getProducts,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            productsList.add(new Products(jsonArray.getJSONObject(i)));
                        }

                        ProductListAdapter productListAdapter=new ProductListAdapter(getContext(),R.layout.products,productsList);
                      //  gridView.setAdapter(productListAdapter);

                        MyItemRecyclerViewAdapter myItemRecyclerViewAdapter=new MyItemRecyclerViewAdapter(productsList,listener);
                        recyclerView.setAdapter(myItemRecyclerViewAdapter);

                        UtilityClass.setGridViewHeightBasedOnChildren(gridView);
                        if(productsList.size()>=10) loadmoreText.setVisibility(View.VISIBLE);
                        else loadmoreText.setVisibility(View.GONE);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loadmoreText:
                getAllProducts(++increment);
                break;

        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(scrollY-oldScrollY>10)
            MainActivity.mainActivity.navigation.animate().translationY( MainActivity.mainActivity.navigation.getBottom())
                    .setInterpolator(new AccelerateInterpolator()).start();
        else if(oldScrollY-scrollY>10)  MainActivity.mainActivity.navigation.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator()).start();
    }


    @Override
    public void onListFragmentInteraction(Products products) {
        Intent i=new Intent(getContext(), ProductDetailsActivity.class);
        i.putExtra(AppConstants.PRODUCT_ID,products.getId());
        startActivity(i);
    }
}
