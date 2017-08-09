package belizenearme.infoservices.lue.belize;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

/**
 * Created by LUE on 03-08-2017.
 */

public class FaqActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);
        context=this;
        WebView view1 = (WebView) findViewById(R.id.web1);
        WebView view2 = (WebView) findViewById(R.id.web2);
        WebView view3 = (WebView) findViewById(R.id.web3);
        WebView view4 = (WebView) findViewById(R.id.web4);
        String text1;
        text1 = "<html><head>"
                + "<p align=\"justify\">"
                + "<style type=\"text/css\">body{color: #fff; background-color: #705572;}"
                + "</style></head>"
                + "<body>"
                + "</body></html>";
        String text2;
        text2 = "<html><head>"
                + "<p align=\"justify\">"
                + "<style type=\"text/css\">body{color: #fff; background-color: #705572;}"
                + "</style></head>"
                + "<body>"
                + "</body></html>";
        String text3;
        text3 = "<html><head>"
                + "<p align=\"justify\">"
                + "<style type=\"text/css\">body{color: #fff; background-color: #705572;}"
                + "</style></head>"
                + "<body>"
                + "</body></html>";
        String text4;
        text4 = "<html><head>"
                + "<p align=\"justify\">"
                + "<style type=\"text/css\">body{color: #fff; background-color: #705572;}"
                + "</style></head>"
                + "<body>"
                + "</body></html>";
        text1+= "BelizeNearme is the fun and easy way to buy &amp; sell items and services with your phone. Snap a photo, enter a short description, and be selling to thousands of potential buyers within seconds. BelizeNearme uses your phone's GPS location to reach a multitude of buyers and sellers in your area. And making an offer is as quick and easy as sending a text message.";
        text1+= "</p></body></html>";
        view1.loadData(text1, "text/html", "utf-8");
        text2+= "Our main focus is Belize. You have full access to application Overall Belize.";
        text2+= "</p></body></html>";
        view2.loadData(text2, "text/html", "utf-8");
        text3+= "The app, available for Android devices, can be downloaded for free via the Google Play marketplace. BelizeNearme is free to use for both buyers and sellers. It is very easy to list an item, to contact sellers, and how safe it is to make a deal, they never look back to other platforms. Users who buy or sell one item successfully are more likely to do so a second time. In each city BelizeNearme enters we are rapidly expanding both the number of items listed and sold, and more people are converting to BelizeNearme fans.";
        text3+= "</p></body></html>";
        view3.loadData(text3, "text/html", "utf-8");
        text4+= "Safety is our number one priority. BelizeNearme has a comprehensive system to identify and quickly disable any account with suspicious or fraudulent activity. Our goal always is to create a vibrant, worry-free marketplace for all users.";
        text4+= "</p></body></html>";
        view4.loadData(text4, "text/html", "utf-8");
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
}