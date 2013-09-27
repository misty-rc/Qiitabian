package org.misty.rc.Qiitabian;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/25
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class QiitaImageCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }


    public QiitaImageCache() {
        this(getDefaultLruCacheSize());
    }

    public QiitaImageCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String s) {
        return get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        put(s, bitmap);
    }
}
