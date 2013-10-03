package helpers;
import java.util.Map;

import static java.lang.Math.*;

public class MathUtility {

    private static final double EPSILON = 0.001;

    public static boolean isEquals(double a, double b) {
        return isEquals(a, b, EPSILON);
    }

    public static boolean isEquals(double a, double b, double epsilon) {
        return abs(a - b) < epsilon;
    }

    public static boolean isGreaterThan(double a, double b) {
        return isGreaterThan(a, b, EPSILON);
    }

    public static boolean isGreaterThan(double a, double b, double epsilon) {
        throw new UnsupportedOperationException("Not implemented");
    }

}
