package ml;

import dataMining.ml.Matrix;

import java.util.List;

public class Imputer extends UnsupervisedLearner {

    private List<Double> centroid;
    private Matrix template;

    /**
     * Computes the column means of continuous attrivutes, and
     * mode of of nominal attributes.
     */
    public void train(Matrix data) {
    }

    /**
     * Returns a zero-row matrix with the same column
     * meta-data as the one that was passed to train.
     */
    public Matrix outputTemplate() {
        return null;
    }

    /**
     * Replaces missing values with the centroid value
     */
    public void transform(List<Double> in, List<Double> out) {
    }

    /**
     * Copy in to out. (In other words, this is a no-op)
     */
    public void untransform(List<Double> in, List<Double> out) {
    }
}
