package rainiksoni.com.tlknsampleapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by rainiksoni on 23/08/16.
 */

public class AvatarController {

    private static final String TAG = AvatarController.class.getSimpleName();

    private BitmapLruCache memCache = null;
    private DiskLruCache diskLruCache = null;
    private static AvatarController _instance = null;

    private ArrayList<IAvatarListener> listeners = new ArrayList<>();

    private static final int NUMBER_OF_THREADS_IN_POOL = 5;
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMBER_OF_THREADS_IN_POOL);
    private static final int APP_VERSION = BuildConfig.VERSION_CODE;
    private static final int VALUE_COUNT = 2;
    public static final int              DISK_IMAGE_CACHE_SIZE                = 1024*1024*10;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;
    private static int IO_BUFFER_SIZE = 8*1024;
    private int mCompressQuality = 70;


    //defining a static array of URLs that will download the image
    public static final String[] urls = {"http://r.ddmcdn.com/s_f/o_1/APL/uploads/2014/10/bird-garden-pictures-4.jpg",
            "http://yuripysar.com/wp-content/uploads/2016/02/abstrct-landscape-painting-winter.jpg",
            "http://yuripysar.com/wp-content/uploads/2015/02/abstract-landscape-painting-red.jpg",
            "http://yuripysar.com/wp-content/uploads/2016/03/abstract-still-life.jpg",
            "http://yuripysar.com/wp-content/uploads/2013/12/butterfly-painting-black-white.jpg",
            "http://yuripysar.com/wp-content/uploads/2013/11/ZGZGkjRDQE.jpg",
            "http://yuripysar.com/wp-content/uploads/2013/11/EzAQ2Ibs3qE.jpg" };


    public AvatarController(){
    }

    public static AvatarController getInstance(){
        if(_instance == null){
            _instance = new AvatarController();
        }
        return _instance;
    }

    public void init(){
        memCache = new BitmapLruCache();
        final File diskCacheDir = getDiskCacheDir(TlknApp.getInstance().getApplicationContext(), TlknApp.getInstance().getPackageCodePath() );
        try {
            diskLruCache = DiskLruCache.open( diskCacheDir, APP_VERSION, VALUE_COUNT, DISK_IMAGE_CACHE_SIZE );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(IAvatarListener listener){
        listeners.add(listener);
    }

    public void unregister(IAvatarListener listener){
        listeners.remove(listener);
    }

    /**
     * {@code startImageRequester} implements a runnable interface to execute the
     * ThreadPoolExecutor service and provides url to {@link DownloadImageTask} for downloading
     * image according to the given manner
     */
    public void startImageRequester() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < urls.length; i++) {
                    String url = urls[i];
                    DownloadImageTask downloadImageTask = new DownloadImageTask(url);
                    executor.execute(downloadImageTask);
                }
            }
        });
        thread.start();
    }

    class DownloadImageTask implements Runnable {
        String url = null;

        public DownloadImageTask(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            /*try {*/

//                Bitmap bitmap = downloadBitmap(url);
            Bitmap bitmap = downloadBitmapAndroid(url);
            if(bitmap != null){
                Log.d(TAG,"  Bitmap downloaded successfully. Saving to caches");

                memCache.put(createKey(url),bitmap);
                putBitmap(createKey(url),bitmap);
            }
            notifyListeners();

          /*  } catch (IOException e) {

            }*/
        }
    }

    private void notifyListeners(){
        for(int i=0; i<listeners.size(); i++){
            IAvatarListener iAvatarListener = listeners.get(i);
            if(iAvatarListener != null) {
                iAvatarListener.updateUI();
            }
        }
    }

    private Bitmap downloadBitmapAndroid(String urlStr){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int res = connection.getResponseCode();
            if(res == 200){
                return BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    private void putBitmap( String key, Bitmap data ) {

        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit( key );
            if ( editor == null ) {
                return;
            }

            if( writeBitmapToFile( data, editor ) ) {
                diskLruCache.flush();
                editor.commit();
                if ( BuildConfig.DEBUG ) {
                    Log.d( "cache_test_DISK_", "image put on disk cache " + key );
                }
            } else {
                editor.abort();
                if ( BuildConfig.DEBUG ) {
                    Log.d( "cache_test_DISK_", "ERROR on: image put on disk cache " + key );
                }
            }
        } catch (IOException e) {
            if ( BuildConfig.DEBUG ) {
                Log.d( "cache_test_DISK_", "ERROR on: image put on disk cache " + key );
            }
            try {
                if ( editor != null ) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }
        }

    }

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor )
            throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream( editor.newOutputStream( 0 ), IO_BUFFER_SIZE );
            return bitmap.compress( mCompressFormat, mCompressQuality, out );
        } finally {
            if ( out != null ) {
                out.close();
            }
        }
    }

    public Bitmap getBitmap(String url){
        Bitmap bitmap = memCache.get(createKey(url));
        if(bitmap == null){
            Log.d(TAG,"memCache bitmap is NULL for url : "+url);
            bitmap = getBitmapFromDisk(createKey(url));
            if(bitmap != null) {
                memCache.put(createKey(url), bitmap);
            }else {
                Log.d(TAG,"DiskCache bitmap is NULL for url : "+url);
                startImageRequester();
            }
        }
        return bitmap;
    }

    public Bitmap getBitmapFromDisk( String key ) {

        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {

            snapshot = diskLruCache.get( key );
            if ( snapshot == null ) {
                Log.d(TAG,"Snapshot is null for Key : "+key);
                return null;
            }
            final InputStream in = snapshot.getInputStream( 0 );
            if ( in != null ) {
                final BufferedInputStream buffIn =
                        new BufferedInputStream( in, IO_BUFFER_SIZE );
                bitmap = BitmapFactory.decodeStream( buffIn );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( snapshot != null ) {
                snapshot.close();
            }
        }

        if ( BuildConfig.DEBUG ) {
            Log.d( "cache_test_DISK_", bitmap == null ? "" : "image read from disk " + key);
        }

        return bitmap;

    }

    private String createKey(String url){
        return url;//String.valueOf(url.hashCode());
    }

}
