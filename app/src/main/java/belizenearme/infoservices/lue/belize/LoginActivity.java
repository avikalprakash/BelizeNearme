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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout phoneLayout,passwordLayout;
    EditText phoneEdit,passwordEdit, emailEdit;
    Button loginBtn;
    TextView forgotPasswordText;
    Context context;
    CoordinatorLayout coordinatorLayout;
    UserSessionManager session;
    String phone;
    String pass;
    String fcmToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new UserSessionManager(getApplicationContext());
     //   fcmToken = SharedPreferenceClass.getToken(context);
        context=this;

        initialize();

    }

    private void initialize()
    {
        phoneLayout=(TextInputLayout)findViewById(R.id.phoneLayout);
        passwordLayout=(TextInputLayout)findViewById(R.id.passwordLayout);
        phoneEdit=(EditText)findViewById(R.id.phoneEdit);
        emailEdit=(EditText)findViewById(R.id.emailEdit);
        passwordEdit=(EditText)findViewById(R.id.passwordEdit);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        forgotPasswordText=(TextView)findViewById(R.id.forgotPasswordText);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        forgotPasswordText.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
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
            case R.id.loginBtn:
               // startActivity(new Intent(getApplicationContext(),MainActivity.class));

                if(validate()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if ((phoneEdit.length()>10)){
                            jsonObject.accumulate("email", phoneEdit.getText().toString());
                        }else {
                            jsonObject.accumulate("phone", phoneEdit.getText().toString());
                        }
                        jsonObject.accumulate("password", passwordEdit.getText().toString());
                        jsonObject.accumulate("registration_key",SharedPreferenceClass.getToken(context));

                        login(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    private void login(JSONObject jsonObject)
    {
        new DownloadThread(this, Urls.login,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movie = jsonArray.getJSONObject(i);
                                phone  = movie.getString("phone");
                                pass  = movie.getString("password");
                            }
                            User user = new User(jsonArray.getJSONObject(0));
                            if (user != null) {
                                SharedPreferenceClass.setUserInfo(context, user);
                                session.createUserLoginSession(phone);
                                Intent i= new Intent(context, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    }else if(!jsonObject.getBoolean("message"))
                        Snackbar.make(coordinatorLayout, getString(R.string.phone_or_password_wrong), Snackbar.LENGTH_LONG).show();
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }


    private boolean validate()
    {
        boolean validate=true;
        if(phoneEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            phoneLayout.setError(getString(R.string.enter)+" "+getString(R.string.phone_no));
        }

        if(passwordEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            passwordLayout.setError(getString(R.string.enter)+" "+getString(R.string.password));
        }
        return validate;
    }

}
