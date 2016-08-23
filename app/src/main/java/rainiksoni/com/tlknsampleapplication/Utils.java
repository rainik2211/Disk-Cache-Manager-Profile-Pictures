package rainiksoni.com.tlknsampleapplication;

/**
 * Created by rainiksoni on 22/08/16.
 */

public class Utils {

    public static final int IMAGE_FROM_URL_1 = 0;
    public static final int IMAGE_FROM_URL_2 = 1;
    public static final int IMAGE_FROM_URL_3 = 2;
    public static final int IMAGE_FROM_URL_4 = 3;
    public static final int IMAGE_FROM_URL_5 = 4;
    public static final int IMAGE_FROM_URL_6 = 5;
    public static final int IMAGE_FROM_URL_7 = 6;

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
}
