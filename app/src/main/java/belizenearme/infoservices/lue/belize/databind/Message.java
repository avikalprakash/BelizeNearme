package belizenearme.infoservices.lue.belize.databind;

import org.json.JSONException;
import org.json.JSONObject;

import belizenearme.infoservices.lue.belize.ChatActivity;
import belizenearme.infoservices.lue.belize.utilities.SharedPreferenceClass;

/**
 * Created by lue on 23-06-2017.
 */

public class Message {
    String id="";
    String sender_id="";
    String reciever_id="";
    String message="";

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    int messageType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    String message_date="";




    public Message(String sender_id, String reciever_id, String message, String message_date, int messageType)
    {
        this.sender_id=sender_id;
        this.reciever_id=reciever_id;
        this.message=message;
        this.message_date=message_date;
        this.messageType=messageType;

    }


    public Message(JSONObject jsonObject)
    {
        try {
            this.id=jsonObject.getString("id");
        } catch (JSONException e) {}
        try {
            this.sender_id=jsonObject.getString("sender_id");
        } catch (JSONException e) {}
        try {
            this.reciever_id=jsonObject.getString("reciever_id");
        } catch (JSONException e) {}
        try {
            this.message=jsonObject.getString("message");
        } catch (JSONException e) {}
        try {
            this.message_date=jsonObject.getString("message_date");
        } catch (JSONException e) {}


    }


}
