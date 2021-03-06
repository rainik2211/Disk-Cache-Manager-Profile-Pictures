package rainiksoni.com.tlknsampleapplication;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Created by rainiksoni on 22/08/16.
 */

public class Utils {

    static final Charset US_ASCII = Charset.forName("US-ASCII");
    static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final int IMAGE_FROM_URL_1 = 0;
    public static final int IMAGE_FROM_URL_2 = 1;
    public static final int IMAGE_FROM_URL_3 = 2;
    public static final int IMAGE_FROM_URL_4 = 3;
    public static final int IMAGE_FROM_URL_5 = 4;
    public static final int IMAGE_FROM_URL_6 = 5;
    public static final int IMAGE_FROM_URL_7 = 6;


    /**
     * Generalize usage is it distributes the 7 urls of image over the given number of contacts
     * from the phonebook
     * @param position current position of the view
     * @return
     */
    public static int getSortedImage(int position){

        if (position % 7 == 0){
            return IMAGE_FROM_URL_1;
        }
        else if (position % 7 == 1){
            return IMAGE_FROM_URL_2;
        }
        else if (position % 7 == 2){
            return IMAGE_FROM_URL_3;
        }
        else if (position % 7 == 3){
            return IMAGE_FROM_URL_4;
        }
        else if (position % 7 == 4){
            return IMAGE_FROM_URL_5;
        }
        else if (position % 7 == 5){
            return IMAGE_FROM_URL_6;
        }
        else return IMAGE_FROM_URL_7;
    }

    static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Deletes the contents of {@code dir}. Throws an IOException if any file
     * could not be deleted, or if {@code dir} is not a readable directory.
     */
    static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    static void closeQuietly(/*Auto*/Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }


    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
}
