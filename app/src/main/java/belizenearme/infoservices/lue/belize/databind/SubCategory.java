package belizenearme.infoservices.lue.belize.databind;

import org.json.JSONObject;

/**
 * Created by lue on 08-06-2017.
 */

public class SubCategory {
    int id;
    int category_id;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    String sub_category_name="";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public SubCategory(JSONObject jsonObject)
    {

        try{
            id=jsonObject.getInt("id");
        }catch (Exception e){}

        try{
            category_id=jsonObject.getInt("category_id");
        }catch (Exception e){}

        try{
            sub_category_name=jsonObject.getString("sub_category_name");
        }catch (Exception e){}
    }

}
