package belizenearme.infoservices.lue.belize.utilities;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Shruthi on 7/22/2016.
 */
public class BitmapLruCache extends LruCache<String, Bitmap> {

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;

        return cacheSize;
    }

    public BitmapLruCache() {
        this(getDefaultLruCacheSize());
    }

    public BitmapLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    public void clear() {
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    public Bitmap getBitmap(String url) {
        return get(url);
    }

    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}

