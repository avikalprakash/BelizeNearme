package belizenearme.infoservices.lue.belize.utilities;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.internal.ImageRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import belizenearme.infoservices.lue.belize.Manifest;
import belizenearme.infoservices.lue.belize.R;

public class UtilityClass {

	public UtilityClass() {
		// TODO Auto-generated constructor stub
	}

	public static void setStatusBarColor(Activity activity) {
		if (Build.VERSION.SDK_INT >= 21) {
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// finally change the color
			window.setStatusBarColor(activity.getResources().getColor(R.color.statusbar_color));

		}
	}

	public static void setStatusBarColor(Activity activity,String colorCode) {
		if (Build.VERSION.SDK_INT >= 21) {
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// finally change the color
			window.setStatusBarColor(Color.parseColor(colorCode));

		}
	}

	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected() == true);
	}


	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (; ; ) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}


	public static void getImage(Context context, String url, final ImageView imageView, final int defaultImage)
	{
		com.android.volley.toolbox.ImageRequest request = new com.android.volley.toolbox.ImageRequest(url,
				new Response.Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap bitmap) {
						if(bitmap!=null) imageView.setImageBitmap(bitmap);
						else if(defaultImage!=0) imageView.setImageResource(defaultImage);

					}
				},0,0,null,
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
					}
				});
		MySingleton.getInstance(context).addToRequestQueue(request);
	}

	public static void loadImage(Context context,String url,NetworkImageView networkImageView){
		com.android.volley.toolbox.ImageLoader imageLoader = MySingleton.getInstance(context)
				.getImageLoader();
		imageLoader.get(url, com.android.volley.toolbox.ImageLoader.getImageListener(networkImageView,
				R.drawable.default_img, android.R.drawable
						.ic_dialog_alert));
		networkImageView.setImageUrl(url, imageLoader);
	}





	public static String getMacId(Context context)
	{
		String macId=null;
		MarshmallowPermission permission=new MarshmallowPermission(context, Manifest.permission.ACCESS_WIFI_STATE);
		if(permission.result==-1 || permission.result==0)
		{
			try
			{
				macId=getMacAddress(context);
			}catch(Exception e){}
		}
		else if(permission.result==1)
		{
			macId=getMacAddress(context);
		}
		return macId;
	}

	private static String getMacAddress(Context context)
	{
		WifiManager manager;
		String macId=null;
		manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(manager!=null)
			macId= manager.getConnectionInfo().getMacAddress();
		return macId;
	}

	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		int measureHeight=0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, gridView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			measureHeight=view.getMeasuredHeight();
			totalHeight +=measureHeight ;

		}
		totalHeight +=50;

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight + ((listAdapter.getCount()));
		gridView.setLayoutParams(params);
	}



	public static DisplayMetrics getDisplayMatrix(Context context)
	{
		DisplayMetrics displayMetrics=new DisplayMetrics();
		WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}


	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}


    public static Bitmap getImage(String imageName)
    {
        Bitmap bitmap=null;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstants.FOLDERNAME);
        try {
            File mypath = new File(mediaStorageDir.getPath(), imageName);
            bitmap= BitmapFactory.decodeStream(new FileInputStream(mypath));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return bitmap;
    }

	public static Bitmap getImageFromPath(String path)
	{
		Bitmap bitmap=null;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

    public static File getImagePath(String imageName)
    {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstants.FOLDERNAME);
        File mypath = new File(mediaStorageDir.getPath(), imageName);
        return mypath;
    }

	public static String getImageFolderPath(String folderName)
	{
		return new File(Environment.getExternalStorageDirectory(), folderName).getPath();
	}

	public static Bitmap getVideoThumb(String videoPath)
	{
		return ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);

	}


	public static String getDateTime()
	{
			Calendar cal=Calendar.getInstance();
			int day=cal.get(Calendar.DAY_OF_MONTH);
			int month=cal.get(Calendar.MONTH);
			int year=cal.get(Calendar.YEAR);
			month =month+1;

			int h=cal.get(Calendar.HOUR);
			int m=cal.get(Calendar.MINUTE);
			int s=cal.get(Calendar.SECOND);

			String date=day+"/"+month+"/"+year;
			return date;

	}

	public static String getTime()
	{
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  dateFormat2.format(new Date());

	}


	public static String getTimeDifference(String createdDate){
		try {

			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDateTime = dateFormat2.format(new Date());
			Date firstParsedDate = dateFormat2.parse(currentDateTime);
			Date tempdate= dateFormat2.parse(createdDate);
			String dateCreated=dateFormat2.format(tempdate);
			Date secondParsedDate =dateFormat2.parse(dateCreated);
			long diff = firstParsedDate.getTime() - secondParsedDate.getTime();
			String elapsed2=elapsed(diff);
			return elapsed2;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String elapsed( long duration ) {
		int seconds = (int)(duration / 1000) % 60 ;
		int minutes = (int)((duration / (1000*60)) % 60);
		int hours = (int)((duration / (1000*60*60)) % 24);
		int days = (int)((duration / (1000*60*60*24)) % 365);
		int years = (int)(duration / 1000*60*60*24*365);

		ArrayList<String> timeArray = new ArrayList<String>();

       /* if(years > 0)
            timeArray.add(String.valueOf(years)   + "y");*/

		if(days > 0)
			timeArray.add(String.valueOf(days) + "d");

		if(hours>0)
			timeArray.add(String.valueOf(hours) + "h");

		if(minutes>0)
			timeArray.add(String.valueOf(minutes) + "min");

		if(seconds>0)
			timeArray.add(String.valueOf(seconds) + "sec");

		String time = "";
	/*	for (int i = 0; i < timeArray.size(); i++)
		{
			time = time + timeArray.get(i);
			if (i != timeArray.size() - 1)
				time = time + ", ";
		}*/

	if(timeArray.size()>0) time+=timeArray.get(0);
		if(!time.trim().equals(""))time+=" ago";
		else time="Just now";

		if (time == "")
			time = "0 sec";
		return time;
	}
}
