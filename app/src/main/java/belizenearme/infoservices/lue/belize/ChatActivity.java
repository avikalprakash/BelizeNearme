package belizenearme.infoservices.lue.belize;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.adapter.MessageListAdapter;
import belizenearme.infoservices.lue.belize.databind.Message;
import belizenearme.infoservices.lue.belize.fcm.MyFirebaseMessagingService;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

public class ChatActivity extends AppCompatActivity {


    ListView messageListView;
    ImageView sendBtn;
    AppCompatEditText writeMessageText;
    List messageList;
    MessageListAdapter messageListAdapter;
    String senderId = "";
    String recieverId = "";
    Context context;
    LinearLayout chatMessageLayout;
    ScrollView scrollView;
    String senderID;
    String uploaded_by;
    String messageSend;
    String msg="";
    String getMessage="";
    TextView receiveMessage, sendMessage;
    private static final String TAG = "ChatApplication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UtilityClass.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        chatMessageLayout = (LinearLayout) findViewById(R.id.chatMessageLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        receiveMessage=(TextView)findViewById(R.id.receiveMessage);
        sendMessage=(TextView)findViewById(R.id.sendMessage);


        senderId = SharedPreferenceClass.getUserInfo(context).getId();
        try {
            recieverId = getIntent().getStringExtra("uploaded_by");
           // senderId = getIntent().getStringExtra("sender_id");
            getMessage=getIntent().getStringExtra("message");
            receiveMessage.setText(getMessage);


          //  getMessages();
        } catch (Exception e) {
        }

        messageListView = (ListView) findViewById(R.id.messageListView);
        sendBtn = (ImageView) findViewById(R.id.sendBtn);

        writeMessageText = (AppCompatEditText) findViewById(R.id.writeMessageText);
        messageList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(this, messageList);
        messageListView.setAdapter(messageListAdapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 msg = writeMessageText.getText().toString().trim();
                writeMessageText.setText("");
                if (!msg.equals("")) {
                    Message message = new Message(senderId, recieverId, msg, UtilityClass.getTime(), 2);
                    messageList.add(message);
                    messageListAdapter.notifyDataSetChanged();
                    addMessages(message);

                }
            }
        });
    }

    private void getMessages() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("user_id", recieverId);
            jsonObject.accumulate("sender_id", senderId);
            new DownloadThread(this, Urls.getMessage, jsonObject.toString(), new DownloadThread.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                Message message = new Message(jsonArray.getJSONObject(i));
                                if (message.getSender_id().equals(SharedPreferenceClass.getUserInfo(context).getId()))
                                    message.setMessageType(1);
                                else message.setMessageType(0);

                                addMessages(message);
                                messageList.add(message);
                                messageListAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, true).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    private void addMessages(Message message) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view;
        if (message.getMessageType() == 0)
            view = layoutInflater.inflate(R.layout.message_view_left, chatMessageLayout, false);
        else view = layoutInflater.inflate(R.layout.message_view_right, chatMessageLayout, false);

        TextView dateText = (TextView) view.findViewById(R.id.dateText);
        TextView messageText = (TextView) view.findViewById(R.id.messageText);
        ImageView statusImageView = (ImageView) view.findViewById(R.id.statusImageView);

       // messageText.setText(message.getMessage());
        sendMessage.setText(msg);
        dateText.setText(UtilityClass.getTimeDifference(message.getMessage_date()));
       // chatMessageLayout.addView(view);
        messageText.setText(getMessage);
        if (message.getMessageType() == 2) {
            sendMessage(message, this, statusImageView);
            message.setMessageType(1);
        }
        scrollView.smoothScrollTo(0, 0);
      //  new SendMessage().execute();
        sendMSG();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("Chat", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }



public  void sendMSG(){

    final  String KEY_SENDERID = "user_id";
    final  String KEY_MESSAGE = "message";


    String REGISTER_URL = "http://api.belizenearme.com/chat/v1/users/"+recieverId+"/message";
    String url = null;
    URL sourceUrl = null;
    REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
    try {
        sourceUrl = new URL(REGISTER_URL);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    url = sourceUrl.toString();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //   Log.d("jaba", usernsme);
                    try {
                        JSONObject jsonresponse = new JSONObject(response);
                        String error = jsonresponse.getString("error");
                        if (error.equals("false")) {
                            JSONArray jsonArray = jsonresponse.getJSONArray("message");
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                                if (jsonObject1 != null) {

                                    // nfcMode=2;

                                    //  progressBarShowHide("",false,0);
                                    //  progressBarShowHide(WRITE_TAG,true,2);

                                }
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                            builder.setMessage("your message not send..")
                                    .setNegativeButton("ok", null)
                                    .create()
                                    .show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //   Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //       Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

                }
            }) {
        @Override
        public String getBodyContentType() {
            return "application/x-www-form-urlencoded; charset=UTF-8";
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            //Adding parameters to request

            params.put(KEY_SENDERID,senderId);
            params.put(KEY_MESSAGE, msg);
            return params;

        }

    };
    RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
    requestQueue.add(stringRequest);
}

    public String readResponse(HttpResponse res) {
        InputStream is = null;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
        } catch (Exception e) {

        }
        return return_text;
    }

    private void sendMessage(final Message message, Activity activity, final ImageView statusImageView) {
        messageSend = message.getMessage();

        /*senderID=message.getSender_id();
        JSONObject jsonObject=new JSONObject();
        try {
           // jsonObject.accumulate("sender_id",message.getSender_id());
            jsonObject.accumulate("user_id",recieverId);
            jsonObject.accumulate("message",message.getMessage());
            new DownloadThread(activity, "http://api.belizenearme.com/chat/v1/users/"+senderID+"/message",jsonObject.toString(), new DownloadThread.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        JSONObject jsonObject= new JSONObject(output);
                        if (!jsonObject.getBoolean("error")) {
                            if(jsonObject.getBoolean("message")) {
                                statusImageView.setVisibility(View.VISIBLE);
                            }
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            },false).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    }
}