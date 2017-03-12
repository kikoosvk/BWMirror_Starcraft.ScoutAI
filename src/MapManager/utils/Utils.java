package MapManager.utils;

import javafx.scene.paint.Color;

/**
 * Created by Chudjak Kristián on 12.03.2017.
 */
public class Utils {
    private final static double BLUE_HUE = Color.BLUE.getHue() ;
    private final static double RED_HUE = Color.RED.getHue() ;

    /**
     * soutce: http://stackoverflow.com/questions/27583122/how-to-find-the-nearest-number-that-is-power-of-two-to-another-number
     * @param x
     * @return next power of two
     */
    public static int ceilingPowerOfTwo(int x){
        x = x - 1;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }

    public static int closestPowerOfTwo(int x){
        int floor = Integer.highestOneBit(x-1);
        int ceil = ceilingPowerOfTwo(x);
        if(ceil - x < x - floor){
            return ceil;
        }else{
            return floor;
        }

    }


    /**
     * source: http://stackoverflow.com/questions/25214538/draw-a-smooth-color-scale-and-assign-specific-values-to-it
     * @param value
     * @param
     * @param
     * @return
     */
    public static Color getColorForValue(double value, double MIN, double MAX) {
        if (value < MIN || value > MAX) {
            return Color.BLACK ;
        }
        double hue = BLUE_HUE + (RED_HUE - BLUE_HUE) * (value - MIN) / (MAX - MIN) ;
        return Color.hsb(hue, 1.0, 1.0);
    }

    public static int getIntFromColor(double Red, double Green, double Blue){
        long R = Math.round(255 * Red);
        long G = Math.round(255 * Green);
        long B = Math.round(255 * Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return (int) (0xFF000000 | R | G | B);
    }


}
