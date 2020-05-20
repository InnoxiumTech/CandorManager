package co.uk.shadowchild.modmanager;

public class MathUtil {

    public static float clamp(float val, float min, float max) {

        if(val < min) return min;
        if(val > max) return max;
        return val;
    }
}
