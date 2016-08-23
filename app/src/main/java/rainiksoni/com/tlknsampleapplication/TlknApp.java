package rainiksoni.com.tlknsampleapplication;

import android.app.Application;

/**
 * Created by rainiksoni on 23/08/16.
 */

public class TlknApp extends Application{

    private static TlknApp _instance = null;

    public TlknApp(){
        _instance = this;
    }

    public static TlknApp getInstance(){
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AvatarController.getInstance().startImageRequester();
    }
}
