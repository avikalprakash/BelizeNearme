package belizenearme.infoservices.lue.belize;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

/**
 * Created by LUE on 08-08-2017.
 */

public class PrivacypoliciesActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);
        context = this;
        WebView view = (WebView) findViewById(R.id.webView);
        String text;
        text = "<html><head>"
                + "<p align=\"justify\">"
                + "<style type=\"text/css\">body{color: #fff; background-color: #705572;}"
                + "</style></head>"
                + "<body>"
                + "</body></html>";
        text+= "This Privacy Policy covers our treatment of information, including Personal Information (defined below) that we gather when you access or use any online service location that posts a link to this Privacy Policy and also applies to your use of our website(s), products, services and mobile applications (collectively, the “Services”), but not to the practices of companies we don’t own or control, or people that we don’t manage. We gather various types of information from our users, as explained in more detail below, and we use this information for you to interact with our Services, internally in connection with our Services, including to personalize, provide, and improve our Services, to allow you to set up a user account and profile, to contact you and allow other users to contact you, to fulfill your requests for certain products and services, and to analyze how you use the Services. In certain cases, we may also share some Personal Information with third parties as described below. Please link to http://belizenearme.com/ for more details.";
        text+= "</p></body></html>";
        view.loadData(text, "text/html", "utf-8");
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
