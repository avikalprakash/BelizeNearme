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

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    CircularImageView userImg;
    TextInputLayout firstNameLayout,lastNameLayout,phoneNoLayout;
    EditText firsttNameEdit,lastNameEdit,phoneNoEdit;
    TextView fullNameText,emailText,editDetailsText,changePasswordText;
    Button updateBtn;
    Context context;
    User user;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UtilityClass.setStatusBarColor(this);
        context=this;
        initialize();
    }


    private void initialize()
    {
        userImg=(CircularImageView)findViewById(R.id.userImg);
        firstNameLayout=(TextInputLayout)findViewById(R.id.firstNameLayout);
        lastNameLayout=(TextInputLayout)findViewById(R.id.lastNameLayout);
        phoneNoLayout=(TextInputLayout)findViewById(R.id.phoneNoLayout);
        firsttNameEdit=(EditText)findViewById(R.id.firsttNameEdit);
        lastNameEdit=(EditText)findViewById(R.id.lastNameEdit);
        phoneNoEdit=(EditText)findViewById(R.id.phoneNoEdit);

        fullNameText=(TextView)findViewById(R.id.fullNameText);
        emailText=(TextView)findViewById(R.id.emailText);
        editDetailsText=(TextView)findViewById(R.id.editDetailsText);
        updateBtn=(Button) findViewById(R.id.updateBtn);
        changePasswordText=(TextView) findViewById(R.id.changePasswordText);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        changePasswordText.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        editDetailsText.setOnClickListener(this);


        setProfileInfo();

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
            case R.id.changePasswordText:
                startActivity(new Intent(getApplicationContext(),ChangePasswordActivity.class));
                break;
            case R.id.updateBtn:
                 updateProfile();
                break;
            case R.id.editDetailsText:
                enableInfo();
                break;

        }
    }


    private void enableInfo()
    {
        firsttNameEdit.setEnabled(true);
        lastNameEdit.setEnabled(true);
        phoneNoEdit.setEnabled(true);
    }



    private void setProfileInfo()
    {
      user=SharedPreferenceClass.getUserInfo(context);
        if(user!=null)
        {

            UtilityClass.getImage(context,user.getPhoto(),userImg,R.drawable.user_default_image);
            firsttNameEdit.setText(user.getFirst_name().trim());
            lastNameEdit.setText(user.getLast_name().trim());
            phoneNoEdit.setText(user.getPhone().trim());
            fullNameText.setText(user.getFirst_name().trim()+" "+user.getLast_name().trim());
            emailText.setText(user.getEmail().trim());
            firsttNameEdit.setEnabled(false);
            lastNameEdit.setEnabled(false);
            phoneNoEdit.setEnabled(false);
        }
    }

    private boolean validate()
    {
        boolean validate=true;
        if(firsttNameEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            firstNameLayout.setError(getString(R.string.enter)+" "+getString(R.string.first_name));
        }else user.setFirst_name(firsttNameEdit.getText().toString().trim());

        if(lastNameEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            lastNameLayout.setError(getString(R.string.enter)+" "+getString(R.string.last_name));
        }else user.setLast_name(lastNameEdit.getText().toString().trim());

        if(phoneNoEdit.getText().toString().trim().equals(""))
        {
            validate=false;
            phoneNoLayout.setError(getString(R.string.enter)+" "+getString(R.string.phone_no));
        }else user.setPhone(phoneNoEdit.getText().toString().trim());
        return validate;
    }




    private void updateProfile() {

        if (validate()) {
            user.setFirst_name(firsttNameEdit.getText().toString());
            user.setLast_name(lastNameEdit.getText().toString());
            user.setPhone(phoneNoEdit.getText().toString());

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("id", user.getId());
            } catch (JSONException e) {
            }
            try {
                jsonObject.accumulate("first_name", user.getFirst_name());
            } catch (JSONException e) {
            }
            try {
                jsonObject.accumulate("last_name", user.getLast_name());
            } catch (JSONException e) {
            }
            try {
                jsonObject.accumulate("phone", user.getPhone());
            } catch (JSONException e) {
            }



            new DownloadThread(this, Urls.update_profileDetails, jsonObject.toString(), new DownloadThread.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        if (!jsonObject.getBoolean("error")) {
                            if (jsonObject.getBoolean("message")) {
                                SharedPreferenceClass.setUserInfo(context,user);
                                setProfileInfo();
                                MainActivity.mainActivity.setUserInfo();
                                Snackbar.make(coordinatorLayout,getString(R.string.updated_successfully),Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },true).execute();
        }
    }


}
