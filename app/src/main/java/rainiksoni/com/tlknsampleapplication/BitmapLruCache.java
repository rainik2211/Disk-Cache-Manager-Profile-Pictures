package rainiksoni.com.tlknsampleapplication;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by rainiksoni on 23/08/16.
 */

public class BitmapLruCache extends LruCache<String, Bitmap> {

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 6;

        return cacheSize;
    }

    public BitmapLruCache() {
        this(getDefaultLruCacheSize());
    }

    public BitmapLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

}
