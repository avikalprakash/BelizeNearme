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
 * Created by LUE on 08-08-2017.
 */

public class AboutusActivity extends AppCompatActivity {
    Context context;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);
        WebView view = (WebView) findViewById(R.id.web);
        String text;
        text = "<html><head>"
                + "<p align=\"justify\">"
                + "<style type=\"text/css\">body{color: #fff; background-color: #705572;}"
                + "</style></head>"
                + "<body>"
                + "</body></html>";
        text+= "Launched in 2017, we at Belize Nearme are working to create the best experience for local buying and selling online. We wanted to create a simple, secure environment where buyers and sellers can easily trade goods from the palm of their hand. Using the power of your mobile phone, Belize Nearme has made selling as simple as taking a selfie and buying as easy as commenting on one. And free for everyone to use. Easy Snap a photo, enter a short description, and be selling to thousands of potential buyers within seconds. Belize Nearme uses your phone's location to reach a multitude of buyers and sellers in your area. And making an offer is as quick and easy as sending a text message. Safe Along with feedback ratings, users can verify their identity by phone or via Facebook, so buyers and sellers can feel confident they are dealing with a real person. At the same time, our messaging system keeps your contact info secret so you can finally relax, no unwanted messages when using Belize Nearme. And unlike most marketplaces, our support team is always hard at work reviewing items posted on Belize Nearme, making sure they meet our high standards so you can buy without worry. Fun Flick through photos to your heart's content and find a great deal near you as you scroll through our beautiful app. “Like” your favorite items, follow great sellers, and make new connections with people in your area who are selling interesting stuff with Belize Nearme.";
        text+= "</p></body></html>";
        view.loadData(text, "text/html", "utf-8");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                super.onBackPressed();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
