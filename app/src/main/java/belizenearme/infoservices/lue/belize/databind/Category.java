package belizenearme.infoservices.lue.belize.databind;

import org.json.JSONObject;

/**
 * Created by lue on 08-06-2017.
 */

public class Category {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    String category_name="";

    public Category(JSONObject jsonObject)
    {

        try{
            id=jsonObject.getInt("id");
        }catch (Exception e){}

        try{
            category_name=jsonObject.getString("category_name");
        }catch (Exception e){}
    }

}
