package belizenearme.infoservices.lue.belize.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import belizenearme.infoservices.lue.belize.databind.Products;
import belizenearme.infoservices.lue.belize.databind.User;
public class SharedPreferenceClass {
    public final static String LANG_KEY="_LANGUAGE";
    public final static String PRODUCT_KEY="product_key";
    public final static String PRODUCT="product";
    public final static String USER_KEY="_USERKEY";
    public final static String APP_KEY="appkey";
    public final static String USER="_USER";
    public final static String LANG="_LANG";
    public static final String ID="id";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String SIGNUP_TYPE="signup_type";
    public static final String PHONE="phone";
    public static final String PHOTO="photo";
    public static final String USER_NAME="user_name";
    public static final String PASSWORD="password";
    public static final String EMAIL="email";
    public static final String DEVICEID_KEY="deviceid_key";
    public static final String DEVICEID="deviceid";
    public static final String ISLOGINWITHGOOGLE="loginwithgoogle";
    public final static String LOGINKEY="LOGINKEY";
    public final static String IMAGE_URL="Image_url";
    public static final String ALLDATA="alldata";
    public static final String IS_LOGIN="is_login";



    public static final String PRODUCT_ID="product_id";
    public static final String CATEGORY_ID="category_id";
    public static final String SUBCATEGORY_ID="sub_category_id";
    public static final String SUBCATEGORY_NAME="sub_category_name";
    public static final String PRODUCT_NAME="product_name";
    public static final String LOCATION="location";
    public static final String PRICE="price";
    public static final String DESCRIPTION="description";
    public static final String IMAGES="images";
    public static final String DISCOUNT="discount";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";
    public static final String CATEGORY_NAME="category_name";
    public static final String SALES_UNIT="sales_unit";
    public static final String VIDEO="video";
    public static final String TOKEN="token";


    public static void setSellProductDetails(Context context, Products products) {
        SharedPreferences prefs = context.getSharedPreferences(PRODUCT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(products.getId()!=0) editor.putInt(PRODUCT_ID,products.getId());
       if(products.getCategory_id()!=0) editor.putInt(CATEGORY_ID,products.getCategory_id());
        if(products.getSub_category_id()!=0) editor.putInt(SUBCATEGORY_ID,products.getSub_category_id());
        if(!products.getSub_category_name().trim().equals(""))  editor.putString(SUBCATEGORY_NAME,products.getSub_category_name());
        if(!products.getProduct_name().trim().equals("")) editor.putString(PRODUCT_NAME,products.getProduct_name());
        if(!products.getLocation().trim().equals("")) editor.putString(LOCATION,products.getLocation());
        if(!products.getPrice().trim().equals("")) editor.putString(PRICE,products.getPrice());
        if(!products.getDescription().trim().equals("")) editor.putString(DESCRIPTION,products.getDescription());
        if(!products.getImages().trim().equals("")) editor.putString(IMAGES,products.getImages());
        if(!products.getDiscount().trim().equals("")) editor.putString(DISCOUNT,products.getDiscount());
        if(!products.getLatitude().trim().equals("")) editor.putString(LATITUDE,products.getLatitude());
        if(!products.getLongitude().trim().equals("")) editor.putString(LONGITUDE,products.getLongitude());
        if(!products.getCategory_name().trim().equals("")) editor.putString(CATEGORY_NAME,products.getCategory_name());
        if(!products.getSales_unit().trim().equals(""))  editor.putString(SALES_UNIT,products.getSales_unit());
        if(!products.getVideo().trim().equals(""))  editor.putString(VIDEO,products.getVideo());
        editor.commit();
    }



    public static Products getSellProductDetails(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PRODUCT_KEY, Context.MODE_PRIVATE);
        Products products=new Products();
        products.setCategory_id(prefs.getInt(CATEGORY_ID,0));
        products.setSub_category_id(prefs.getInt(SUBCATEGORY_ID,0));
        products.setSub_category_name(prefs.getString(SUBCATEGORY_NAME,""));
        products.setProduct_name(prefs.getString(PRODUCT_NAME,""));
        products.setLocation(prefs.getString(LOCATION,""));
        products.setPrice(prefs.getString(PRICE,""));
        products.setDescription(prefs.getString(DESCRIPTION,""));
        products.setImages(prefs.getString(IMAGES,""));
        products.setDiscount(prefs.getString(DISCOUNT,""));
        products.setLatitude(prefs.getString(LATITUDE,""));
        products.setLongitude(prefs.getString(LONGITUDE,""));
        products.setCategory_name(prefs.getString(CATEGORY_NAME,""));
        products.setSales_unit(prefs.getString(SALES_UNIT,""));
        products.setVideo(prefs.getString(VIDEO,""));
        return products;
    }

    public static void setLanguageSetting(Context context,int i) {
        SharedPreferences prefs = context.getSharedPreferences(LANG_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LANG,i);
        editor.commit();
    }


    public static void setLogin(Context context,boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IS_LOGIN,value);
        editor.commit();
    }

    public static void saveToken(Context context,String token) {
        SharedPreferences prefs = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN,token);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN,"");
    }

    public static boolean getLogin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
        return prefs.getBoolean(IS_LOGIN,false);
    }

    public static void setUserInfo(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(user!=null) {
            editor.putString(ID, user.getId());
            editor.putString(FIRST_NAME, user.getFirst_name());
            editor.putString(LAST_NAME, user.getLast_name());
            editor.putString(EMAIL, user.getEmail());
            editor.putString(PASSWORD, user.getPassword());
            editor.putString(SIGNUP_TYPE, user.getSignin_type());
            editor.putString(DEVICEID, user.getDevice_id());
            editor.putString(PHONE, user.getPhone());
            editor.putString(PHOTO, user.getPhoto());
            editor.commit();
            setLogin(context,true);
        }
    }

    public static int getLanguageSetting(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LANG_KEY, Context.MODE_PRIVATE);
        return prefs.getInt(LANG,0);
    }


    public static User  getUserInfo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        User user=new User();
        user.setId(prefs.getString(ID,""));
        user.setFirst_name(prefs.getString(FIRST_NAME,""));
        user.setLast_name(prefs.getString(LAST_NAME,""));
        user.setEmail(prefs.getString(EMAIL,""));
        user.setPassword(prefs.getString(PASSWORD,""));
        user.setSignin_type(prefs.getString(SIGNUP_TYPE,""));
        user.setDevice_id(prefs.getString(DEVICEID,""));
        user.setPhone(prefs.getString(PHONE,""));
        user.setPhoto(prefs.getString(PHOTO,""));
        return user;

    }

    public static void clearUserInfo(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(LOGINKEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }


    public static void setLoginWithGoogle(Context context,boolean flag)
    {     SharedPreferences prefs = context.getSharedPreferences(LOGINKEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(ISLOGINWITHGOOGLE,flag);
            editor.commit();
    }

    public static boolean getLoginWithGoogle(Context context)
    {      SharedPreferences prefs = context.getSharedPreferences(LOGINKEY, Context.MODE_PRIVATE);
         return prefs.getBoolean(ISLOGINWITHGOOGLE,false);
    }


    public static void setALlDataDownloaded(Context context,boolean flag)
    {     SharedPreferences prefs = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(ALLDATA,flag);
        editor.commit();
    }

    public static boolean getALlDataDownloaded(Context context)
    {      SharedPreferences prefs = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        return prefs.getBoolean(ALLDATA,false);
    }

/*
    public static void setDeviceId(Context context,String deviceId)
    {
        SharedPreferences prefs = context.getSharedPreferences(DEVICEID_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
            editor.putString(DEVICEID, deviceId);
            editor.commit();
    }

    public static String  getDeviceId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DEVICEID_KEY, Context.MODE_PRIVATE);
       return prefs.getString(DEVICEID,"");
    }*/





}
