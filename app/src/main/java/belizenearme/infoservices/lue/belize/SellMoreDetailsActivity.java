package belizenearme.infoservices.lue.belize;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.googlecode.mp4parser.authoring.Edit;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.camera.MMPermission;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;
import life.knowledge4.videotrimmer.utils.FileUtils;

import static android.R.attr.bitmap;

public class SellMoreDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    TextInputLayout titleLayout, descriptionLayout, priceLayout, salesUnitLayout,discountLayout;
    EditText titleEdit, descriptionEdit, priceEdit, salesUnitEdit,discountEdit;
    RelativeLayout locationLayout;
    Button publishBtn;
    Context context;
    String locationName = "";
    String latitude = "";
    String longitude = "";
    String errorMsg = "";
    CoordinatorLayout coordinatorLayout;
    LocationManager mlocManager;
    AlertDialog.Builder alert;
    ProgressDialog progressDialog;
    TextView locationText;
    boolean isLocation;
    MMPermission permission;
    Products products;
    String locality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_more_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;


        initialize();
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

    private void initialize() {
        alert = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait_for_location));

        titleLayout = (TextInputLayout) findViewById(R.id.titleLayout);
        descriptionLayout = (TextInputLayout) findViewById(R.id.descriptionLayout);
        priceLayout = (TextInputLayout) findViewById(R.id.priceLayout);
        salesUnitLayout = (TextInputLayout) findViewById(R.id.salesUnitLayout);
        discountLayout = (TextInputLayout) findViewById(R.id.discountLayout);

        titleEdit = (EditText) findViewById(R.id.titleEdit);
        descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
        priceEdit = (EditText) findViewById(R.id.priceEdit);
        salesUnitEdit = (EditText) findViewById(R.id.salesUnitEdit);
        discountEdit = (EditText) findViewById(R.id.discountEdit);
        locationLayout = (RelativeLayout) findViewById(R.id.locationLayout);
        publishBtn = (Button) findViewById(R.id.publishBtn);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        locationText= (TextView) findViewById(R.id.locationText);

        locationLayout.setOnClickListener(this);
        publishBtn.setOnClickListener(this);
      products = SharedPreferenceClass.getSellProductDetails(context);

    }


    private boolean validate() {
        boolean validate = true;

        if (titleEdit.getText().toString().trim().equals("")) {
            validate = false;
            titleLayout.setError(getString(R.string.enter) + " " + getString(R.string.product_title));
        } else products.setProduct_name(titleEdit.getText().toString().trim());

        if (descriptionEdit.getText().toString().trim().equals("")) {
            validate = false;
            descriptionLayout.setError(getString(R.string.enter) + " " + getString(R.string.product_description));
        } else products.setDescription(descriptionEdit.getText().toString().trim());

        if (priceEdit.getText().toString().trim().equals("")) {
            validate = false;
            priceLayout.setError(getString(R.string.enter) + " " + getString(R.string.price));
        } else products.setPrice(priceEdit.getText().toString().trim());

        if (discountEdit.getText().toString().trim().equals("")) {
            validate = false;
            discountLayout.setError(getString(R.string.enter) + " " + getString(R.string.discount));
        } else products.setDiscount(discountEdit.getText().toString().trim());

        if (salesUnitEdit.getText().toString().trim().equals("")) {
            validate = false;
            salesUnitLayout.setError(getString(R.string.enter) + " " + getString(R.string.sales_unit));
        } else products.setSales_unit(salesUnitEdit.getText().toString().trim());

        if (locationName.equals("") || latitude.equals("") || longitude.equals("")) {
            validate = false;
            errorMsg = getString(R.string.enter) + " " + getString(R.string.location);
            Snackbar.make(coordinatorLayout, errorMsg, Snackbar.LENGTH_SHORT).show();
        }else {
            products.setLatitude(latitude);
            products.setLongitude(longitude);
            products.setLocation(locationName);
        }

        return validate;
    }





    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.locationLayout:
         if(UtilityClass.isOnline(context))
         {
             permission = new MMPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
             if (permission.result == -1 || permission.result == 0) {
                 try {
                     registerForGPS();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             } else if (permission.result == 1) {
                 registerForGPS();
             }

         }
                break;
            case R.id.publishBtn:
                   if(validate())
                   {
                     if(UtilityClass.isOnline(context))
                         publish();
                   }
                break;

        }

    }


    private void registerForGPS() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);


        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alert.setTitle("GPS");
            alert.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS..");
            alert.setPositiveButton("Turn on GPS",
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, (float) 0.01, listener);

                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, (float) 0.01, listener);

                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(I);
                        }
                    });

            alert.show();
        }

        else {

            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    (float) 0.01, listener);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    (float) 0.01, listener);
            if(!progressDialog.isShowing())progressDialog.show();
        }
    }

    private void unregisterForGPS() {
        mlocManager.removeUpdates(listener);
    }

    private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            if (location.getLatitude() > 0.0) {
                if (location.getAccuracy()>0 && location.getAccuracy()<1000) {
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                    if(!isLocation)
                    {
                      latitude=String.valueOf(location.getLatitude());
                        longitude=String.valueOf(location.getLongitude());
                        locationName=getAddress(location.getLatitude(),location.getLongitude());
                        locationText.setText(locationName);
                        unregisterForGPS();
                    }
                    isLocation=true;
                } else {
                    if(!progressDialog.isShowing()) progressDialog.show();
                }
            }
        }
        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


    private  String getAddress(double lat,double lang)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address="";

        try {
            addresses = geocoder.getFromLocation(lat,lang,1);
            address=addresses.get(0).getLocality()+","+addresses.get(0).getAdminArea()+","+addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }


    private void publish()
    {

        JSONObject jsonObject=new JSONObject();
        try {
            User user=SharedPreferenceClass.getUserInfo(context);
            products.setSeller_name(user.getFirst_name()+" "+user.getLast_name());
            products.setSeller_contact(user.getPhone());
            jsonObject.accumulate("id",SharedPreferenceClass.getUserInfo(context).getId());
            jsonObject.accumulate("category_id",products.getCategory_id());
            jsonObject.accumulate("sub_category_id",products.getSub_category_id());
            jsonObject.accumulate("product_name",products.getProduct_name());
            jsonObject.accumulate("location",products.getLocation());
            jsonObject.accumulate("price",products.getPrice());
            jsonObject.accumulate("discount",products.getDiscount());
            jsonObject.accumulate("seller_name",products.getSeller_name());
            jsonObject.accumulate("seller_contact",products.getSeller_contact());
            jsonObject.accumulate("description",products.getDescription());
            jsonObject.accumulate("latitude",products.getLatitude());
            jsonObject.accumulate("longitude",products.getLongitude());
            jsonObject.accumulate("sales_unit",products.getSales_unit());
            new DownloadThread(this, Urls.product_upload,jsonObject.toString(), new DownloadThread.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    String id="";
                    try {
                        JSONObject jsonObject= new JSONObject(output);
                        if (!jsonObject.getBoolean("error")) {
                            id=jsonObject.getString("id");
                            uploadImage(id,Urls.upload_photo,true);
                          //  new ConvertStringVideo(id).execute(products.getVideo());
                            if(!products.getVideo().equals(""))  uploadImage(id,Urls.upload_video,false);

                            //uploadFile("video",products.getVideo(),id,Urls.video_upload);
                            Snackbar.make(coordinatorLayout,"Uploaded "+id, Snackbar.LENGTH_LONG).show();

                        } else  Snackbar.make(coordinatorLayout, jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                    }catch (Exception e){e.printStackTrace();}
                }
            },true).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(final String id, String url, final boolean isImage){
        //Showing the progress dialog
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                      if(progressDialog.isShowing())  progressDialog.dismiss();
                        //Showing toast message of the response
                        Snackbar.make(coordinatorLayout, getString(R.string.published_successfully), Snackbar.LENGTH_LONG).show();
                        Intent i = new Intent(SellMoreDetailsActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        if(progressDialog.isShowing()) progressDialog.dismiss();

                        //Showing toast
                        Toast.makeText(context, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("id", id);
                if(isImage) {
                    String[] images = products.getImages().split(",");
                    if (images.length > 0) params.put("image1", getStringImage(images[0]));
                    else params.put("image1", "");
                    if (images.length > 1) params.put("image2", getStringImage(images[1]));
                    else params.put("image2", "");
                    if (images.length > 2) params.put("image3", getStringImage(images[2]));
                    else params.put("image3", "");
                    if (images.length > 3) params.put("image4", getStringImage(images[3]));
                    else params.put("image4", "");
                    if (images.length > 4) params.put("image5", getStringImage(images[4]));
                    else params.put("image5", "");
                }
                else {
                }
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public String getStringImage(String path){
        String encodedImage="";
       Bitmap bmp= UtilityClass.getImageFromPath(path);
        if(bmp!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
             encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        return encodedImage;
    }
}
