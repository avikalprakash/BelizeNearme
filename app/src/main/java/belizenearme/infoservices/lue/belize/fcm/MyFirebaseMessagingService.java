package belizenearme.infoservices.lue.belize.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import belizenearme.infoservices.lue.belize.ChatActivity;
import belizenearme.infoservices.lue.belize.MainActivity;
import belizenearme.infoservices.lue.belize.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    NotificationManager notificationManager;
    String msg;
    int from_user_id;
    int to_user_id;

    @Override
    public void onMessageReceived (RemoteMessage remoteMessage){
        // Log.d("Firebase notification","received");
        // Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        // Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());

        sendNotification(remoteMessage.getData());

    }


    private void sendNotification(Map<String, String> params) {

        Log.d(TAG, "Notification Message Body1: " + params);
        JSONObject jsonObject = new JSONObject(params);

        try {
            String data = jsonObject.getString("data");
            JSONObject jsonObject1 = new JSONObject(data);
            String message = jsonObject1.getString("message");
            JSONObject jsonObject2 = new JSONObject(message);
            int user_id = jsonObject2.getInt("to_user_id");
            String created_at = jsonObject2.getString("created_at");
            int message_id = jsonObject2.getInt("message_id");
            msg = jsonObject2.getString("message");
            from_user_id = jsonObject2.getInt("from_user_id");
            String user = jsonObject1.getString("user");
            JSONObject jsonObject3 = new JSONObject(user);
            to_user_id = jsonObject3.getInt("to_user_id");

            Log.d("cyhsb", " " + user);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("message", msg);
        intent.putExtra("uploaded_by", String.valueOf(from_user_id));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Belize")

                //.setContentText("image")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


/*    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_LONG);
        Intent intent=new Intent(MyFirebaseMessagingService.this, ChatActivity.class);
        intent.putExtra("message", msg);
        intent.putExtra("uploaded_by", String.valueOf(from_user_id));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }*/
}