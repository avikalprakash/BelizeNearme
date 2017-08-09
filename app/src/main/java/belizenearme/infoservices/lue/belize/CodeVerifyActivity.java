package belizenearme.infoservices.lue.belize;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class CodeVerifyActivity extends AppCompatActivity {

    TextView verifyText;
    TextInputLayout verificationCodeLayout;
    EditText verificationCodeEdit;
    Button verifydBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();

    }

    private void initialize()
    {
        verificationCodeLayout=(TextInputLayout)findViewById(R.id.verificationCodeLayout);
        verificationCodeEdit=(EditText)findViewById(R.id.verificationCodeEdit);
        verifydBtn=(Button)findViewById(R.id.verifydBtn);
        verifyText=(TextView)findViewById(R.id.verifyText);
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
