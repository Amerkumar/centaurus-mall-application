package app.com.thecentaurusmall.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    public static String getUrlByDensity(HashMap<String,String> urls, String density) {

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

    public static String getUrlByToken(String folderName, String venueid, String fileName, String density, String token) {
        return BASE_URL + folderName + "%2F" + venueid + "%2F" + fileName.replaceAll(" ", "_").toLowerCase()
                + "%2Fdrawable-" + density + "%2F" + fileName.replaceAll(" ", "_").toLowerCase() + ".webp?alt=media&token="
                + token;
    }

    public static Date addOrSubtractDaysFromCurrent(int days) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, days);
        dt = c.getTime();
        return dt;
    }

    public static String timestampToSimpleDateFormat(Date date) {
        String pattern = "dd MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static long numberOfDaysBetweenTwoDates(Date date) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String inputString1 = myFormat.format(new Date());
        String inputString2 = myFormat.format(date);

        long diff = 0;
        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            diff = date2.getTime() - date1.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int floorNumberToSwitchCase(int floorNumber) {
        switch (floorNumber){
            case 4:
                return 1;
            case 3:
                return 2;
            case 2:
                return 3;
            case 1:
                return 4;
            case 0:
                return 5;
        }

        return 5;
    }

    public static void hideKeyboard(FragmentActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
