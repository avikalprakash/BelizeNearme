package belizenearme.infoservices.lue.belize.databind;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lue on 22-06-2017.
 */

public class SellerBuyer {
    private String id="";
    private String first_name="";
    private String last_name="";
    private String email="";
    private String phone="";
    private String photo="";
    private String messageCount="";

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    private String message_date="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    private String timeDate="";


    public SellerBuyer(JSONObject jsonObject)
    {
        try {
            id=jsonObject.getString("id");
        } catch (JSONException e) {}
        try {
            first_name=jsonObject.getString("first_name");
        } catch (JSONException e) {}
        try {
            last_name=jsonObject.getString("last_name");
        } catch (JSONException e) {}
        try {
            email=jsonObject.getString("email");
        } catch (JSONException e) {}
        try {
            phone=jsonObject.getString("phone");
        } catch (JSONException e) {}
        try {
            photo=jsonObject.getString("photo");
        } catch (JSONException e) {}

        try {
            message_date=jsonObject.getString("message_date");
        } catch (JSONException e) {}
    }

}
