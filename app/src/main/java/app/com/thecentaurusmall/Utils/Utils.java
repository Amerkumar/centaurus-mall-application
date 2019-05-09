package app.com.thecentaurusmall.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.HashMap;

public class Utils {


    public static String BASE_URL = "https://firebasestorage.googleapis.com/v0/b/mapin-220013.appspot.com/o/";

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static String floorNumberToName(int floor) {
        String floorName = null;
        switch (floor) {
            case 0:
                floorName = "Ground Floor";
                break;
            case 1:
                floorName = "First Floor";
                break;
            case 2:
                floorName = "Second Floor";
                break;
            case 3:
                floorName = "Third Floor";
                break;
            case 4:
                floorName = "Fourth Floor";
                break;
        }

        return floorName;
    }

    public static String getTokenByDensity(HashMap<String,String> urls, String density) {

        Log.d("Utils", density);
        if (density.equals("ldpi")) {
            return urls.get("ldpi");
        }
        if (density.equals("mdpi")) {
            return urls.get("mdpi");
        }
        if (density.equals("hdpi")) {
            return urls.get("hdpi");
        }
        if (density.equals("xhdpi")) {
            return urls.get("xhdpi");
        }
        if (density.equals("xxhdpi")) {
//                Log.d("Utils - token", urls.xxhdpi);
            return urls.get("xxhdpi");
        }
        if (density.equals("xxxhdpi")) {
            return urls.get("xxxhdpi");
        }
        return urls.get("hdpi");
    }


    public static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public static String getDealUrlByToken(String folderName, String venueid, String fileName, String density, String token) {
        return BASE_URL + folderName + "%2F" + venueid + "%2F" + fileName.replaceAll(" ", "_").toLowerCase()
                + "%2Fdrawable-" + density + "%2F" + fileName.replaceAll(" ", "_").toLowerCase() + ".webp?alt=media&token="
                + token;
    }

}
