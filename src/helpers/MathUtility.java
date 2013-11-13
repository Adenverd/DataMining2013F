package helpers;

import static java.lang.Math.*;

/**
 * Credits:
 * Normal (Gaussian) distribution: http://introcs.cs.princeton.edu/java/22library/Gaussian.java.html
 */
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

    /**
     * normal-pdf(x) with mu = 0, stddev = 1
     */
    public static double normalPdf(double x) {
        return exp(-x * x / 2) / sqrt(2 * PI);
    }

    /**
     * normal-pdf(x, mu, stddev) with given mu and stddev
     */
    public static double normalPdf(double x, double mu, double sigma) {
        return normalPdf((x - mu) / sigma) / sigma;
    }

    /**
     * normal-cdf(z) with mu = 0, stddev = 1
     */
    public static double normalCdf(double z) {
        if (z < -8.0) {
            return 0.0;
        }
        if (z > 8.0) {
            return 1.0;
        }
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * normalCdf(z);
    }

    /**
     * normal-cdf(z, mu, stddev) with given mu and stddev
     */
    public static double normalCdf(double z, double mu, double sigma) {
        return normalCdf((z - mu) / sigma);
    }

    /**
     * normal-inv(z)
     * @return x such that normal-cdf(z) = x
     */
    public static double normalInv(double y) {
        return normalInv(y, .00000001, -8, 8);
    }

    private static double normalInv(double y, double delta, double lo, double hi) {
        double mid = lo + (hi - lo) / 2;
        if (hi - lo < delta) {
            return mid;
        }
        return normalCdf(mid) > y
                ? normalInv(y, delta, lo, mid)
                : normalInv(y, delta, mid, hi);
    }
}
