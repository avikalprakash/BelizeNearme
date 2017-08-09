package belizenearme.infoservices.lue.belize;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.adapter.MessageListAdapter;
import belizenearme.infoservices.lue.belize.databind.Message;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.LocationAddress;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {


    LinearLayout imageLayout;
    TextView productNameText,productLocationText,productPriceText,productDiscountText,sellerNameText,productDescText;
    TextView sellerContactText,addFavotireText,shareText,reportText;
    FrameLayout mapFrame;
    int id=0;
    MapView mMapView;
    Products products;
    Marker custommarker;
    Context context;
    private GoogleMap mMap;
    Button chatBtn;
    PopupWindow popupWindow;
    List<Message> messageList;
    MessageListAdapter messageListAdapter;
    LinearLayout sellerContactLayout;
    String latitude;
    String longitude;
    String giiglemap_adress;
    private GoogleMap googleMap;
    LocationAddress locationAddress;
    String uploaded_by;
    String sender_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           context=this;
        User user= SharedPreferenceClass.getUserInfo(this);
        sender_id= user.getId();
        mMapView = (MapView)findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void initialize()
    {
        imageLayout=(LinearLayout)findViewById(R.id.imageLayout);
        productNameText=(TextView)findViewById(R.id.productNameText);
        productLocationText=(TextView)findViewById(R.id.productLocationText);
        productPriceText=(TextView)findViewById(R.id.productPriceText);
        productDiscountText=(TextView)findViewById(R.id.productDiscountText);
        sellerNameText=(TextView)findViewById(R.id.sellerNameText);
        productDescText=(TextView)findViewById(R.id.productDescText);
        sellerContactText=(TextView)findViewById(R.id.sellerContactText);
        addFavotireText=(TextView)findViewById(R.id.addFavotireText);
        shareText=(TextView)findViewById(R.id.shareText);
        reportText=(TextView)findViewById(R.id.reportText);
       // mapFrame=(FrameLayout)findViewById(R.id.mapFrame);

        chatBtn=(Button)findViewById(R.id.chatBtn);

        sellerContactLayout=(LinearLayout) findViewById(R.id.sellerContactLayout);

        addFavotireText.setOnClickListener(this);
        shareText.setOnClickListener(this);
        reportText.setOnClickListener(this);
        sellerContactText.setOnClickListener(this);
        chatBtn.setOnClickListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
     //   mapFragment.getMapAsync(this);

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
        sellerContactText.setText(products.getSeller_contact());
        uploaded_by=products.getUpload_by();

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
      /*  if(mMap!=null)
        {
            setLocationOnMap();
        }*/

     /*   if(products.getUpload_by().equals(SharedPreferenceClass.getUserInfo(context).getId())) {
            chatBtn.setVisibility(View.GONE);
            addFavotireText.setVisibility(View.GONE);
            shareText.setVisibility(View.GONE);
            reportText.setVisibility(View.GONE);
            sellerContactLayout.setVisibility(View.GONE);

        }*/
        latitude = products.getLatitude();
         longitude = products.getLongitude();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

              double lat=25.6020534;
                double lang=85.1602404;
                // Add a marker in Sydney, Australia, and move the camera.

               // LatLng LatLong = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                LatLng LatLong = new LatLng(lat, lang);
                Log.d("valuein","  "+lang+" "+lat);
                locationAddress.getAddressFromLocation(lat, lang,
                        getApplicationContext(), new GeocoderHandler());

                googleMap.addMarker(new MarkerOptions().position(LatLong).title(giiglemap_adress).position(LatLong));

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLong));
                googleMap.animateCamera( CameraUpdateFactory.zoomTo( 8.0f ) );

                googleMap.getMaxZoomLevel();
            }
        });

    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Log.d("addresdd"," "+locationAddress);
            // adresstext.setText(locationAddress);
            giiglemap_adress=locationAddress;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.addFavotireText:

                break;
            case R.id.shareText:
            shareContent();
                break;
            case R.id.reportText:

                break;
            case R.id.sellerContactText:
                if(!sellerContactText.getText().toString().trim().equals(""))
              dialPhoneNumber(sellerContactText.getText().toString());
                break;

            case R.id.chatBtn:
                //onShowPopup(v);

                Intent i = new Intent(ProductDetailsActivity.this, ChatActivity.class);
                i.putExtra("uploaded_by", products.getUpload_by());
                i.putExtra("sender_id", sender_id);
                startActivity(i);
                break;

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
         popupWindow = new PopupWindow(inflatedView, size.x - 50,(size.y*2)/5, true );

        // set a background drawable with rounders corners
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.light_grey));
        // make it focusable to show the keyboard to enter in `EditText`
        popupWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popupWindow.setOutsideTouchable(true);
        // show the popup at bottom of the screen and set some margin at bottom ie,
        popupWindow.showAtLocation(v, Gravity.TOP, 0,10);

    }





}
