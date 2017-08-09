package belizenearme.infoservices.lue.belize;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class CodeVerificationActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout mobileNoLayout;
    EditText mobileNoEdit;
    Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();

    }

    private void initialize()
    {
        mobileNoLayout=(TextInputLayout)findViewById(R.id.mobileNoLayout);
        mobileNoEdit=(EditText)findViewById(R.id.mobileNoEdit);
        sendBtn=(Button)findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sendBtn:
                startActivity(new Intent(getApplicationContext(),CodeVerifyActivity.class));
                break;
        }
    }
}
