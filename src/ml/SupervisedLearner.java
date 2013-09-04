package ml;

import java.util.List;

/**
 * @author Sawyer Anderson
 * @date 2013.08.27
 */
public abstract class SupervisedLearner {

    public abstract void train(Matrix features, Matrix labels);

    public abstract List<Double> predict(List<Double> in);

    public double getAccuracy() {
        return Matrix.UNKNOWN_VALUE;
    }
}
