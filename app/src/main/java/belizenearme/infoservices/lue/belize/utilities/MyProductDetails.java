package belizenearme.infoservices.lue.belize.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.EditMyProduct;
import belizenearme.infoservices.lue.belize.MainActivity;
import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.adapter.MessageListAdapter;
import belizenearme.infoservices.lue.belize.databind.Message;
import belizenearme.infoservices.lue.belize.databind.Products;

public class MyProductDetails extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {


    LinearLayout imageLayout;
    TextView productNameText,productLocationText,productPriceText,productDiscountText,sellerNameText,productDescText;
    int id=0;
    Products products;
    Context context;
    private GoogleMap mMap;
    Button deleteBtn, editBtn;
    List<Message> messageList;
    MessageListAdapter messageListAdapter;
    ProgressDialog pDialog;
    CheckBox checkBox;
    SwitchCompat onoff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        initialize();
    }
public void Edit(View v){
    Intent intent = new Intent(MyProductDetails.this, EditMyProduct.class);
    startActivity(intent);
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initialize()
    {
        id=getIntent().getIntExtra(AppConstants.PRODUCT_ID,0);
        new CheckStatus().execute(String.valueOf(id));
        imageLayout=(LinearLayout)findViewById(R.id.imageLayout);
        productNameText=(TextView)findViewById(R.id.productNameText);
        productLocationText=(TextView)findViewById(R.id.productLocationText);
        productPriceText=(TextView)findViewById(R.id.productPriceText);
        productDiscountText=(TextView)findViewById(R.id.productDiscountText);
        sellerNameText=(TextView)findViewById(R.id.sellerNameText);
        productDescText=(TextView)findViewById(R.id.productDescText);
        onoff =(SwitchCompat)findViewById(R.id.onoff);

        deleteBtn=(Button)findViewById(R.id.deleteBtn);
        editBtn=(Button)findViewById(R.id.editBtn);

        deleteBtn.setOnClickListener(this);

        try
        {
            id=getIntent().getIntExtra(AppConstants.PRODUCT_ID,0);
            if(id!=0)
            {
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("id",id);
                getProductDetails(jsonObject);
            }

        }catch (Exception e){}

       /* if (onoff.isChecked()){
            onoff.setText("Online");
        }else{
            onoff.setText("Offline");
        }*/
//onoff.setOnCheckedChangeListener(this);
        onoff.setOnClickListener(this);


    }

    private void getProductDetails(JSONObject jsonObject)
    {
        new DownloadThread(this, Urls.getProductDetails,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        if(jsonArray.length()>0) {
                            products = new Products(jsonArray.getJSONObject(0));
                            if (products != null) {
                                setProductDetails();
                            }
                        }
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }


    private void setProductDetails()
    {
        productNameText.setText(products.getProduct_name());
        productLocationText.setText(products.getLocation());
        productPriceText.setText(products.getPrice());
        productDiscountText.setText(products.getDiscount());
        sellerNameText.setText(products.getSeller_name());
        productDescText.setText(products.getDescription());

        DisplayMetrics displayMetrics=UtilityClass.getDisplayMatrix(this);

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(displayMetrics.widthPixels,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,0,10,0);
        layoutParams.gravity= Gravity.CENTER_VERTICAL;
        ImageView imageView;
        String[] images=products.getImages().split(",");
        for(int i=0;i<images.length;i++)
        {
            imageView=new ImageView(this);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


            UtilityClass.getImage(this,images[i],imageView,0);
            imageLayout.addView(imageView);
        }
        if(mMap!=null)
        {
            setLocationOnMap();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.onoff:
                if (onoff.isChecked()){
                    onoff.setText("Online");
                    new OnlineOfflineProduct().execute(String.valueOf(id), String.valueOf(1));
                }else {
                    onoff.setText("Offline");
                    new OnlineOfflineProduct().execute(String.valueOf(id), String.valueOf(0));
                }
                break;

            case R.id.deleteBtn:

               final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View eulaLayout = layoutInflater.inflate(R.layout.alert_dialog, null);
        checkBox=(CheckBox)eulaLayout.findViewById(R.id.checkbox);
        alertDialog.setView(eulaLayout);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (checkBox.isChecked()) {
                    new DeleteData().execute(String.valueOf(id));
                }else {
                    alertDialog.setCancelable(false);
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.create().show();

                break;
        }
    }

  /*  @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (onoff.isChecked()){
            onoff.setText("Online");
            new OnlineOfflineProduct().execute(String.valueOf(id), String.valueOf(1));
        }else {
            onoff.setText("Offline");
            new OnlineOfflineProduct().execute(String.valueOf(id), String.valueOf(0));
        }
    }*/

    class  DeleteData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyProductDetails.this);
            pDialog.setMessage("deleting...");
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://belizenearme.com/api/deleteUserProducts.php");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("post_id",params[0]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(httpiEntity);
                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
            }catch(Exception e){
                e.printStackTrace();
            }
            return return_text;
        }
        public String readResponse(HttpResponse res) {
            InputStream is=null;
            String return_text="";
            try {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line="";
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                return_text=sb.toString();
            } catch (Exception e){}
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            try{
                if(pDialog.isShowing()) pDialog.dismiss();
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.getString("error").equalsIgnoreCase("false")) {
                    if (jsonObject.getString("message").equals("true")) ;
                    {
                        Toast.makeText(getApplicationContext(), "product deleted successfully", Toast.LENGTH_LONG).show();
                         Intent iSave=new Intent(getApplicationContext(), MainActivity.class);
                         startActivity(iSave);
                         finish();
                    }
                }
                else Toast.makeText(getApplicationContext(), "product not deleted", Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void shareContent()
    {
        String shareText="Belize Nearme Post:-->\n"+products.getProduct_name()+" \n   By "+products.getSeller_name()+
                "\n Location: "+products.getLocation()+"\n Price: "+products.getPrice()+"\n Discount: "+products.getDiscount()+
                "\n Description: "+products.getDescription()+"\n\n Contact Seller: "+products.getSeller_contact();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,shareText);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share product details"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

    }


    private void setLocationOnMap()
    {
        LatLng latLng = new LatLng(Double.parseDouble(products.getLatitude()),Double.parseDouble(products.getLongitude()));
        /*View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

        AppCompatTextView productNameText=(AppCompatTextView)marker.findViewById(R.id.productNameText);
        AppCompatTextView productLocationText=(AppCompatTextView)marker.findViewById(R.id.productLocationText);

        productNameText.setText(products.getProduct_name());
        productLocationText.setText(products.getLocation());
            custommarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.
                fromBitmap(UtilityClass.createDrawableFromView(context, marker))));
        */


        mMap.addMarker(new MarkerOptions().position(latLng).title(products.getProduct_name()+"\n"+products.getLocation()));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

    }


    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



    public void onShowPopup(View v){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.chat_dialog, null,false);
        // find the ListView in the popup layout
        ListView messageListView = (ListView)inflatedView.findViewById(R.id.messageListView);
        ImageView sendBtn = (ImageView)inflatedView.findViewById(R.id.sendBtn);

        final AppCompatEditText writeMessageText = (AppCompatEditText)inflatedView.findViewById(R.id.writeMessageText);

        messageList=new ArrayList<>();
        messageListAdapter=new MessageListAdapter(context,messageList);
        messageListView.setAdapter(messageListAdapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=writeMessageText.getText().toString().trim();
                writeMessageText.setText("");
                if(!msg.equals("")) {
                    Message message = new Message(SharedPreferenceClass.getUserInfo(context).getId(),
                            products.getUpload_by(),msg,UtilityClass.getTime(),2);

                    messageList.add(message);
                    messageListAdapter.notifyDataSetChanged();

                }
            }
        });
        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        // set height depends on the device size
      //  popupWindow = new PopupWindow(inflatedView, size.x - 50,(size.y*2)/5, true );

        // set a background drawable with rounders corners
      //  popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.light_grey));
        // make it focusable to show the keyboard to enter in `EditText`
      //  popupWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
     //   popupWindow.setOutsideTouchable(true);
        // show the popup at bottom of the screen and set some margin at bottom ie,
     //   popupWindow.showAtLocation(v, Gravity.TOP, 0,10);

    }

    class  OnlineOfflineProduct extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyProductDetails.this);
            pDialog.setMessage("Updating...");
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://belizenearme.com/api/statusUserProducts.php");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("post_id",params[0]);
                jsonObject.accumulate("status",params[1]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(httpiEntity);
                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
            }catch(Exception e){
                e.printStackTrace();
            }
            return return_text;
        }
        public String readResponse(HttpResponse res) {
            InputStream is=null;
            String return_text="";
            try {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line="";
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                return_text=sb.toString();
            } catch (Exception e){}
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            try{
                if(pDialog.isShowing()) pDialog.dismiss();
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.getString("error").equalsIgnoreCase("false")) {
                    String msg = jsonObject.getString("message");
                    if (jsonObject.getString("message").equals("true")) ;
                    {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent iSave=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(iSave);
                        finish();
                    }
                }
                else Toast.makeText(getApplicationContext(), "product not updated", Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    class  CheckStatus extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyProductDetails.this);
            pDialog.setMessage("Changing...");
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://belizenearme.com/api/getProductDetails.php");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("id",params[0]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(httpiEntity);
                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
            }catch(Exception e){
                e.printStackTrace();
            }
            return return_text;
        }
        public String readResponse(HttpResponse res) {
            InputStream is=null;
            String return_text="";
            try {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line="";
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                return_text=sb.toString();
            } catch (Exception e){}
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            try{
                if(pDialog.isShowing()) pDialog.dismiss();
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.getString("error").equalsIgnoreCase("false")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    for (int i=0; i<=jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status = jsonObject1.getString("status");
                        if (Integer.parseInt(status)==1){
                            onoff.setChecked(true);
                            onoff.setText("Online");
                        }else if (Integer.parseInt(status)==0){
                            onoff.setChecked(false);
                            onoff.setText("Offline");
                        }
                    }
                }
                else Toast.makeText(getApplicationContext(), "product not updated", Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
