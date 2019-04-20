package app.com.thecentaurusmall;

import android.app.Application;

import io.mapwize.mapwizeformapbox.AccountManager;

public class ApplicationManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AccountManager.start(this, "7b9ead04354c17aa66dca69ec6e6e32b");
        
    }
}
