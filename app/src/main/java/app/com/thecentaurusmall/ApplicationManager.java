package app.com.thecentaurusmall;

import android.app.Application;

import io.mapwize.mapwizeformapbox.AccountManager;

public class ApplicationManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AccountManager.start(this, "7ebdfe23cb2d5f74");
        
    }
}
