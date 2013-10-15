package helpers;

import java.util.Random;
import static java.lang.Math.*;

/**
 * Static wrapper for of java.util.Random
 */
public class Rand {

    private static Random instance;

    public static boolean nextBoolean() {
        return getInstance().nextBoolean();
    }

    public static double nextDouble() {
        return getInstance().nextDouble();
    }

    public static int nextInt() {
        return getInstance().nextInt();
    }

    public static int nextInt(int n) {
        return getInstance().nextInt(n);
    }

    public static long nextLong() {
        return getInstance().nextLong();
    }

    public static double nextGaussian() {
        return getInstance().nextGaussian();
    }

    public static double drawFromStandardExp() {
        return -log(nextDouble());
    }

    private static Random getInstance() {
        if (instance == null) {
            instance = new Random();
        }
        return instance;
    }
}
