package belizenearme.infoservices.lue.belize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.databind.User;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class SplashActivity extends AppCompatActivity {

    Timer timer;
    int count;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UtilityClass.setStatusBarColor(this,"#111922");
        context=this;





      /*  if(SharedPreferenceClass.getLogin(this))
        {
            User user=SharedPreferenceClass.getUserInfo(this);

            if(user!=null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    if(user.getSignin_type().equals("login")) {
                        jsonObject.accumulate("phone", user.getPhone());
                        jsonObject.accumulate("email","");
                    }else
                    {
                        jsonObject.accumulate("email", user.getEmail());
                        jsonObject.accumulate("phone","");
                    }
                    jsonObject.accumulate("password", user.getPassword());
                    login(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }*/
      //  else
        startTimer();
    }


    private void startTimer()
    {
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(count>2)
                {
                    if(timer!=null) timer.cancel();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                count++;
            }
        },1000,1000);
    }

/*    private void login(JSONObject jsonObject)
    {
        new DownloadThread(this, Urls.login,jsonObject.toString(), new DownloadThread.AsyncResponse() {
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
                                finish();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },true).execute();
    }*/

}
