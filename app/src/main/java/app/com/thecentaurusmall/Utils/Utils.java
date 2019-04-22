package app.com.thecentaurusmall.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {

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

}
