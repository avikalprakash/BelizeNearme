package belizenearme.infoservices.lue.belize.databind;

import org.json.JSONObject;

/**
 * Created by lue on 08-06-2017.
 */

public class Products {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    int id;
    int category_id;
    int sub_category_id;
    String sub_category_name="";
    String product_name="";
    String location="";
    String price="";
    String description="";
    String images="";
    String discount="";
    String seller_name="";
    String seller_contact="";
    String latitude="";
    String longitude="";
    String category_name="";

    String sales_unit="";
    String video="";

    public String getUpload_by() {
        return upload_by;
    }

    String upload_by="";


    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSales_unit() {
        return sales_unit;
    }

    public void setSales_unit(String sales_unit) {
        this.sales_unit = sales_unit;
    }



    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }



    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }




    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_contact() {
        return seller_contact;
    }

    public void setSeller_contact(String seller_contact) {
        this.seller_contact = seller_contact;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public Products(JSONObject jsonObject)
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
        try{
            product_name=jsonObject.getString("product_name");
        }catch (Exception e){}
        try{
            location=jsonObject.getString("location");
        }catch (Exception e){}
        try{
            price=jsonObject.getString("price");
        }catch (Exception e){}
        try{
            description=jsonObject.getString("description");
        }catch (Exception e){}

        try{
            images=jsonObject.getString("images");
        }catch (Exception e){}

        try{
            discount=jsonObject.getString("discount");
        }catch (Exception e){}
        try{
            seller_name=jsonObject.getString("seller_name");
        }catch (Exception e){}
        try{
            seller_contact=jsonObject.getString("seller_contact");
        }catch (Exception e){}
        try{
            latitude=jsonObject.getString("latitude");
        }catch (Exception e){}
        try{
            longitude=jsonObject.getString("longitude");
        }catch (Exception e){}
        try{
            upload_by=jsonObject.getString("upload_by");
        }catch (Exception e){}
    }

    public Products(){}

}
