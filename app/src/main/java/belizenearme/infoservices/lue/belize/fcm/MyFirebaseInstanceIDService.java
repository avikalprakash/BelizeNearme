package belizenearme.infoservices.lue.belize.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MySharedPreference";

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(SharedPreferenceClass.getToken(this).equals("")) {
            SharedPreferenceClass.saveToken(this,refreshedToken);
        }
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}