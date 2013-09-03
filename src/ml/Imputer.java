package ml;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author Kanat Bekt
 * @date 9/1/2013
 * Replaces missing continuous values with the mean.
 * Replaces missing categorical values with the most-common value (or mode).
 */
public class Imputer extends UnsupervisedLearner {

    private List<Double> centroid = new Vector<Double>();
    private Matrix template = new Matrix();

    /**
     * Computes the column means of continuous attributes, and
     * mode of of nominal attributes.
     */
    @Override
    public void train(Matrix data) {
        template.copyMetaData(data);
        for (int i = 0; i < data.getCols(); i++) {
            double value = data.valueCount(i) == 0 ? data.columnMean(i) : data.mostCommonValue(i);
            centroid.add(value);
        }
    }

    /**
     * Replaces missing values with the centroid value.
     * @param in Features
     */
    @Override
    public List<Double> transform(List<Double> in) {
        if (in.size() != centroid.size()) {
            throw new MLException(String.format(
                    "Unexpected in-vector size. Expected: %d, Got: %d", centroid.size(), in.size()));
        }
        List<Double> out = new ArrayList<Double>();
        for (int i = 0; i < centroid.size(); i++) {
            double value = in.get(i) == Matrix.UNKNOWN_VALUE ? centroid.get(i) : in.get(i);
            out.add(value);
        }
        return out;
    }

    /**
     * Copy in to out. (In other words, this is a no-op).
     */
    @Override
    public List<Double> untransform(List<Double> in) {
        if (in.size() != centroid.size()) {
            throw new MLException(String.format(
                    "Unexpected in-vector size. Expected: %d, Got: %d", centroid.size(), in.size()));
        }
        List<Double> out = new ArrayList<Double>();
        for (int i = 0; i < centroid.size(); i++) {
            out.add(in.get(i));
        }
        return out;
    }

    /**
     * Returns a zero-row matrix with the same column
     * meta-data as the one that was passed to train.
     */
    @Override
    public Matrix outputTemplate() {
        return template;
    }

}
