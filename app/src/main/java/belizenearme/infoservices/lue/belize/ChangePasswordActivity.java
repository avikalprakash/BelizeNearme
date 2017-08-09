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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{


    TextInputLayout currentPasswordLayout,newPasswordLayout,reenterPasswordLayout;
    EditText currentPasswordEdit,newPasswordEdit,reenterPasswordEdit;
    Button changePasswordBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

    }

    private void initialize()
    {
        currentPasswordLayout=(TextInputLayout)findViewById(R.id.currentPasswordLayout);
        newPasswordLayout=(TextInputLayout)findViewById(R.id.newPasswordLayout);
        reenterPasswordLayout=(TextInputLayout)findViewById(R.id.reenterPasswordLayout);
        currentPasswordEdit=(EditText)findViewById(R.id.currentPasswordEdit);
        newPasswordEdit=(EditText)findViewById(R.id.newPasswordEdit);
        reenterPasswordEdit=(EditText)findViewById(R.id.reenterPasswordEdit);
        changePasswordBtn=(Button)findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(this);
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
            case R.id.forgotPasswordText:
                startActivity(new Intent(getApplicationContext(),CodeVerificationActivity.class));
                break;
        }
    }

}
