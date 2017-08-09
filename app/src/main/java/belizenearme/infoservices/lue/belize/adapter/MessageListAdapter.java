package belizenearme.infoservices.lue.belize.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import belizenearme.infoservices.lue.belize.Async.DownloadThread;
import belizenearme.infoservices.lue.belize.Async.Urls;
import belizenearme.infoservices.lue.belize.ProductDetailsActivity;

import belizenearme.infoservices.lue.belize.R;
import belizenearme.infoservices.lue.belize.databind.Message;
import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.utilities.AppConstants;
import belizenearme.infoservices.lue.belize.utilities.ImageLoader;
import belizenearme.infoservices.lue.belize.utilities.UtilityClass;

/**
 * Created by lue on 08-06-2017.
 */

public class MessageListAdapter extends ArrayAdapter<Message> {

     private List<Message> messageList;
    Context context;
    ImageLoader imageLoader;


    public MessageListAdapter(Context context, List<Message> messageList)
    {
        super(context,0,messageList);
        this.context = context;
        this.messageList=messageList;
        imageLoader=new ImageLoader(getContext());

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;
        final Message message = messageList.get(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            if(message.getMessageType()==0)
            row = inflater.inflate(R.layout.message_view_left, parent, false);
            else row = inflater.inflate(R.layout.message_view_right, parent, false);

            holder = new ImageHolder();
            holder.dateText = (TextView) row.findViewById(R.id.dateText);
            holder.messageText = (TextView) row.findViewById(R.id.messageText);
            holder.statusImageView = (ImageView) row.findViewById(R.id.statusImageView);

            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }
        holder.messageText.setText(message.getMessage());
        holder.dateText.setText(UtilityClass.getTimeDifference(message.getMessage_date()));
        if(message.getMessageType()==2)
        {
            sendMessage(message,(Activity)getContext(),holder.statusImageView);
            message.setMessageType(1);
        }
        holder = (ImageHolder) row.getTag();
        return row;

    }

    static class ImageHolder {
        TextView messageText,dateText;
        ImageView statusImageView;
    }



    private void sendMessage(final Message message, Activity activity, final ImageView statusImageView)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.accumulate("sender_id",message.getSender_id());
            jsonObject.accumulate("reciever_id",message.getReciever_id());
            jsonObject.accumulate("message",message.getMessage());
            new DownloadThread(activity, Urls.send_message,jsonObject.toString(), new DownloadThread.AsyncResponse() {
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

    }


}
