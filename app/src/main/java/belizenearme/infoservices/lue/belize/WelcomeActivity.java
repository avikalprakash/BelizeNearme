package belizenearme.infoservices.lue.belize;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.adapter.ScrollPageAdapter;
import belizenearme.infoservices.lue.belize.adapter.SubCategoryListAdapter;
import belizenearme.infoservices.lue.belize.camera.MMPermission;
import belizenearme.infoservices.lue.belize.databind.SubCategory;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    ProgressDialog progress_dialog;
    GoogleApiClient google_api_client;

    LoginButton facebookSigninBtn;
    SignInButton gogoleSigninBtn;
    GoogleApiAvailability google_api_availability;
    private ConnectionResult connection_result;
    private boolean is_signInBtn_clicked;
    private boolean is_intent_inprogress;
    private static final int SIGN_IN_CODE = 0;
    private int request_code;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String facebooklogin = null;
    ProgressDialog progress;
    public static String fbemail = "", facebookId = "";

    private ViewPager viewPager;
    private ImageView[] pageIndicator;
    Button crateAccountBtn,signInBtn;
    Context context;
    Timer timer;
    int i=0;
    MMPermission permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_welcome);
        UtilityClass.setStatusBarColor(this);

        context=this;







        viewPager = (ViewPager) findViewById(R.id.view_pagern);
        pageIndicator = new ImageView[8];
        pageIndicator[0] = (ImageView) findViewById(R.id.indicator1);
        pageIndicator[1] = (ImageView) findViewById(R.id.indicator2);
        pageIndicator[2] = (ImageView) findViewById(R.id.indicator3);
        pageIndicator[3] = (ImageView) findViewById(R.id.indicator4);
        pageIndicator[4] = (ImageView) findViewById(R.id.indicator5);
        pageIndicator[5] = (ImageView) findViewById(R.id.indicator6);
        pageIndicator[6] = (ImageView) findViewById(R.id.indicator7);
        pageIndicator[7] = (ImageView) findViewById(R.id.indicator8);


        viewPager.setAdapter(new ScrollPageAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(pageChangeListener);

        initialize();


    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            for(int i = 0; i < 8; i++) {
                if(position == i)
                    pageIndicator[i].setImageResource(R.drawable.circle_fill2);
                else
                    pageIndicator[i].setImageResource(R.drawable.circle2);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void initialize()
    {
        crateAccountBtn=(Button) findViewById(R.id.crateAccountBtn);
        signInBtn=(Button) findViewById(R.id.signInBtn);

        gogoleSigninBtn=(SignInButton) findViewById(R.id.gogoleSigninBtn);
        gogoleSigninBtn.setSize(SignInButton.SIZE_WIDE);
        facebookSigninBtn=(LoginButton) findViewById(R.id.facebookSigninBtn);

        gogoleSigninBtn.setOnClickListener(this);

        crateAccountBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);

        startTimer();




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        try {
            if (facebooklogin != null) {
                if (facebooklogin.equalsIgnoreCase("facebook")) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                    LoginManager.getInstance().logOut();
                    CookieSyncManager.createInstance(WelcomeActivity.this);
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeAllCookie();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        progress = new ProgressDialog(WelcomeActivity.this);
        progress.setMessage("Loading");
        progress.setProgressStyle(R.style.ProgressDialogTheme);
        progress.setCancelable(false);
        callbackManager = CallbackManager.Factory.create();
        final List<String> permissionNeeds = Arrays.asList("public_profile", "email", "user_birthday");//, "public_profile");


        facebookSigninBtn.setReadPermissions(permissionNeeds);

        facebookSigninBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                progress.show();
                if(loginResult!=null)
                {
                    GraphRequest graphRequest=GraphRequest.newMeRequest(
                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(final JSONObject me, GraphResponse response) {
                                    if (response.getError() != null) {
                                        // handle error
                                    } else {
                                        try {
                                            progress.dismiss();

                                            facebookId =me.getString("id");
                                            try {fbemail = me.getString("email");}catch (Exception e)
                                            {
                                                Toast.makeText(getApplicationContext(),getString(R.string.no_valid_email_address_is_available),Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            final JSONObject jsonObject=new JSONObject();
                                            try {
                                                jsonObject.accumulate("first_name",me.getString("first_name"));
                                                jsonObject.accumulate("last_name",me.getString("last_name"));
                                                jsonObject.accumulate("email",fbemail);
                                                jsonObject.accumulate("phone","");
                                                jsonObject.accumulate("password","facebook");
                                                jsonObject.accumulate("signin_type","facebook");
                                                jsonObject.accumulate("registration_key",SharedPreferenceClass.getToken(context));

                                                Log.e("................",me.toString());
                                                jsonObject.accumulate("device_id",UtilityClass.getMacId(context));
                                                new ProfileTracker() {
                                                    @Override
                                                    protected void onCurrentProfileChanged(
                                                            Profile oldProfile,
                                                            Profile currentProfile) {
                                                        try {
                                                            if(currentProfile.getProfilePictureUri(300,300)!=null)
                                                            jsonObject.accumulate("photo",currentProfile.getProfilePictureUri(300,300).toString());
                                                            else  jsonObject.accumulate("photo","");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };

                                                signUp(jsonObject);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name,last_name,email,gender");
                    graphRequest.setParameters(parameters);
                    graphRequest.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                try {
                    if (exception instanceof FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.crateAccountBtn:
                 startActivity(new Intent(context,CreateAccountActivity.class));
                break;

            case R.id.signInBtn:
                startActivity(new Intent(context,LoginActivity.class));
                break;

            case R.id.gogoleSigninBtn:

                signIn();
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            progress.dismiss();

                      JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.accumulate("first_name",acct.getGivenName());
                jsonObject.accumulate("last_name",acct.getFamilyName());
                jsonObject.accumulate("email",acct.getEmail());
                jsonObject.accumulate("phone","");
                jsonObject.accumulate("password","google");
                jsonObject.accumulate("signin_type","google");
                jsonObject.accumulate("device_id",UtilityClass.getMacId(context));
                jsonObject.accumulate("registration_key",SharedPreferenceClass.getToken(context));
                if(acct.getPhotoUrl()!=null)jsonObject.accumulate("photo",acct.getPhotoUrl());
                else jsonObject.accumulate("photo","");
                signUp(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            // Signed out, show unauthenticated UI.
        }
    }



    // [START signIn]
    private void signIn() {
        if(!progress.isShowing())  progress.show();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    handleSignInResult(googleSignInResult);
                }
            });
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]


    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void signUp(JSONObject jsonObject)
    {
        new DownloadThread(this, Urls.signup,jsonObject.toString(), new DownloadThread.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject jsonObject= new JSONObject(output);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                            if(jsonArray.length()>0) {
                                User user = new User(jsonArray.getJSONObject(0));
                                if (user != null) {
                                    SharedPreferenceClass.setUserInfo(context, user);
                                    startActivity(new Intent(context,MainActivity.class));
                                }
                            }
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        },true).execute();
    }





    private void startTimer()
    {
        timer=new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((i++)%8,true);
                    }
                });

            }
        },1000,1500);
    }

    private void createFolder()
    {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstants.FOLDERNAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory");
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub


        permission = new MMPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission.result == -1 || permission.result == 0) {
            try {
                createFolder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (permission.result == 1) {
            createFolder();
        }
        super.onResume();

    }
}
