package belizenearme.infoservices.lue.belize;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView support;
    TextView privacypolicies;
    TextView aboutus;
    TextView feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);
        support=(TextView) findViewById(R.id.supportText);
        support.setOnClickListener(this);
        privacypolicies=(TextView)findViewById(R.id.privacyText);
        privacypolicies.setOnClickListener(this);
        aboutus=(TextView) findViewById(R.id.aboutUsText);
        aboutus.setOnClickListener(this);
        feedback=(TextView) findViewById(R.id.leaveFeedbackText);
        feedback.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.supportText:
                Intent intent= new Intent(this, Supportjava.class);
                startActivity(intent);
                break;
            case R.id.privacyText:
                Intent intent1= new Intent(this, PrivacypoliciesActivity.class);
                startActivity(intent1);
                break;
            case R.id.aboutUsText:
                Intent intent2= new Intent(this, AboutusActivity.class);
                startActivity(intent2);
                break;
            case R.id.leaveFeedbackText:
                Intent intent3= new Intent(this, FeedbackActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
