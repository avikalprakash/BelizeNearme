package belizenearme.infoservices.lue.belize.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import belizenearme.infoservices.lue.belize.R;

public class ImageLoader {

    boolean save = false;
    final int default_img = R.drawable.default_img;
    private Map<Integer, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<Integer, String>());

    private Context context;
    private FileCache fileCache;
    private ImageLoaderListener loaderListener;
    BitmapLruCache memoryCache = new BitmapLruCache();
    ExecutorService executorService;
    // Handler to display images in UI thread
    Handler handler = new Handler();

    public interface ImageLoaderListener {
        public void onLoadImage(int pos);
    }

    public ImageLoader(Context context) {
        this(context, false);
    }

    public ImageLoader(Context context, ImageLoaderListener loaderListener) {
        this(context, false);
        this.loaderListener = loaderListener;
    }

    public ImageLoader(Context context, ImageLoaderListener loaderListener, boolean save) {
        this(context, save);
        this.loaderListener = loaderListener;
    }

    public ImageLoader(Context context, boolean save) {
        this.context = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
        this.save = save;
    }

    public void setLoaderListener(ImageLoaderListener loaderListener) {
        this.loaderListener = loaderListener;
    }

    public void displayImage(String url, ImageView imageView) {

        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        else {
            queuePhoto(-1, url, imageView);
            imageView.setImageResource(default_img);
        }
    }

    private void queuePhoto(int pos, String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(pos, url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private int scaleWidth = -1;
    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    private Bitmap getBitmap(String url) {
        File file = null;

        file = fileCache.getFile(url);

        //from sd cache
        Bitmap bitmap = decodeFile(file);
        if (bitmap != null)
            return bitmap;

        // Download Images from the Internet
        try {
            bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            UtilityClass.CopyStream(is, os);
            os.close();
            is.close();
            conn.disconnect();
            bitmap = decodeFile(file);
            return bitmap;

        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            // Recommended Size 512
            final int REQUIRED_SIZE = 256;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public int pos;
        public String url;
        public ImageView imageView;

        public PhotoToLoad(int p, String u, ImageView i) {
            pos = p;
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                Bitmap bitmap = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bitmap);

                BitmapDisplayer bd = new BitmapDisplayer(bitmap, photoToLoad);
                handler.post(bd);

            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.pos);
        if (tag == null)
            return false;
        return true;
    }

    // Used to display bitmap in the UI thread
    public class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
                if(loaderListener != null)
                loaderListener.onLoadImage(photoToLoad.pos);
            } else {
                photoToLoad.imageView.setImageResource(default_img);
            }
        }
    }

    public void deleteFile(String url) {
        fileCache.deleteFile(url);
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    public void resume() {
        // TODO Auto-generated method stub

    }

    public void pause() {
        // TODO Auto-generated method stub

    }
}
