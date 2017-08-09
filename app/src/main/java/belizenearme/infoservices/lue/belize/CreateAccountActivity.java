package belizenearme.infoservices.lue.belize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout firstNameLayout,lastNameLayout,phoneLayout, emailLayout,passwordLayout,repasswordLayout;
    EditText firstNameEdit,lastNameEdit,phoneNoEdit, emailEdit, passwordEdit,repasswordEdit;
    Button signupBtn;
    Context context;
    CoordinatorLayout coordinatorLayout;
    User user;
    String fcmToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        context=this;
        SharedPreferenceClass.getToken(context);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fcmToken = SharedPreferenceClass.getToken(context);
        initialize();
    }

    private void initialize()
    {
        firstNameLayout=(TextInputLayout)findViewById(R.id.firstNameLayout);
        lastNameLayout=(TextInputLayout)findViewById(R.id.lastNameLayout);
        phoneLayout=(TextInputLayout)findViewById(R.id.phoneLayout);
        emailLayout=(TextInputLayout)findViewById(R.id.emailLayout);
        passwordLayout=(TextInputLayout)findViewById(R.id.passwordLayout);
        firstNameEdit=(EditText)findViewById(R.id.firstNameEdit);
        lastNameEdit=(EditText)findViewById(R.id.lastNameEdit);
        phoneNoEdit=(EditText)findViewById(R.id.phoneNoEdit);
        emailEdit=(EditText)findViewById(R.id.emailEdit);
        passwordEdit=(EditText)findViewById(R.id.passwordEdit);
        signupBtn=(Button)findViewById(R.id.signupBtn);
        repasswordLayout=(TextInputLayout)findViewById(R.id.repasswordLayout);
        repasswordEdit=(EditText)findViewById(R.id.repasswordEdit);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        signupBtn.setOnClickListener(this);

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
            case R.id.signupBtn:
               // startActivity(new Intent(getApplicationContext(),SignUpCodeVerifyActivity.class));

                if(validate()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.accumulate("first_name",user.getFirst_name());
                        jsonObject.accumulate("last_name", user.getLast_name());
                        jsonObject.accumulate("phone", user.getPhone());
                        jsonObject.accumulate("email", user.getEmail());
                        jsonObject.accumulate("password", user.getPassword());
                        jsonObject.accumulate("signin_type","login");
                        jsonObject.accumulate("device_id", UtilityClass.getMacId(context));
                        jsonObject.accumulate("photo","");
                        jsonObject.accumulate("registration_key", fcmToken);
                        signUp(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    private boolean validate()
    {
        boolean validate=true;
        user=new User();
        if(firstNameEdit.getText().toString().trim().equals(""))
        {
          validate=false;
            firstNameLayout.setError(getString(R.string.enter)+" "+getString(R.string.first_name));
        }else user.setFirst_name(firstNameEdit.getText().toString().trim());
        if(lastNameEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            lastNameLayout.setError(getString(R.string.enter)+" "+getString(R.string.last_name));
        }else user.setLast_name(lastNameEdit.getText().toString().trim());
        if(phoneNoEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            phoneLayout.setError(getString(R.string.enter)+" "+getString(R.string.phone_no));
        }else user.setPhone(phoneNoEdit.getText().toString().trim());
        if(emailEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            emailLayout.setError(getString(R.string.enter)+" "+getString(R.string.email_id));
        }else user.setEmail(emailEdit.getText().toString().trim());

        if(passwordEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            passwordLayout.setError(getString(R.string.enter)+" "+getString(R.string.password));
        }else user.setPassword(passwordEdit.getText().toString().trim());
        if(repasswordEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            repasswordLayout.setError(getString(R.string.enter)+" "+getString(R.string.password));
        }

        if(!passwordEdit.getText().toString().trim().equals(repasswordEdit.getText().toString().trim()))
        {
            validate=false;
            repasswordLayout.setError(getString(R.string.enter)+" "+getString(R.string.passowrd_not_matched));
        }
        return validate;
    }


    private void signUp(JSONObject jsonObject)
    {
        new DownloadThread(this, Urls.signup,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {

                        if(jsonObject.getBoolean("message")) {
                                SharedPreferenceClass.setUserInfo(context, user);
                                startActivity(new Intent(context,LoginActivity.class));
                        }
                    } else  Snackbar.make(coordinatorLayout, jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }

}
