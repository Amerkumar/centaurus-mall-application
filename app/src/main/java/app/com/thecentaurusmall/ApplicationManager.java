package app.com.thecentaurusmall;

import android.app.Application;

public class ApplicationManager extends Application {

    private boolean isIndoor;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean isIndoor() {
        return isIndoor;
    }

    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }
}
