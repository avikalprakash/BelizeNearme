package belizenearme.infoservices.lue.belize.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.MainActivity;
import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.adapter.MyItemRecyclerViewAdapter;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.databind.SellerBuyer;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;

public class BuyFragment extends Fragment{



    List<SellerBuyer> sellerList;
    SellerListRecyclerViewAdapter sellerListRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_fragment, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            sellerList=new ArrayList<>();
            sellerListRecyclerViewAdapter=new SellerListRecyclerViewAdapter(sellerList);
            recyclerView.setAdapter(sellerListRecyclerViewAdapter);
            getSellerList();
        }


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SellerBuyer item);
    }


    private void getSellerList()
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.accumulate("id",SharedPreferenceClass.getUserInfo(getContext()).getId());
            new DownloadThread(getActivity(), Urls.getSeller,jsonObject.toString(), new DownloadThread.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        JSONObject jsonObject= new JSONObject(output);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray=jsonObject.getJSONArray("message");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                sellerList.add(new SellerBuyer(jsonArray.getJSONObject(i)));
                            }
                            sellerListRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            },true).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
