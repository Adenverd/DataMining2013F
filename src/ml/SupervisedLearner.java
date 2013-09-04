package ml;

import java.util.List;

/**
 * @author Sawyer Anderson
 * @date 2013.08.27
 */
public abstract class SupervisedLearner {

    public abstract void train(Matrix features, Matrix labels);

    public abstract List<Double> predict(List<Double> in);

    /**
     * Uses Hamming distance for Categorical attributes, and means for Continuous
     * @return Vector based SSE
     */
    public double getAccuracy(Matrix features, Matrix labels) {
        double sum = 0;
        for (int i = 0; i < labels.getNumRows(); i++) {
            List<Double> result = predict(features.getRow(i));

            if (labels.getNumCols() != result.size()) {
                throw new MLException("Returned result size is different than number of columns in labels!");
            }

            double magnitude = 0;
            for (int j = 0; j < labels.getNumCols(); j++) {

                double actual = labels.getRow(i).get(j);
                double predicted = result.get(j);

                if (labels.isCategorical(j)) {
                    magnitude += actual == predicted ? 1 : 0;
                } else {
                    double res = actual - predicted;
                    magnitude += res * res;
                }
            }
            sum += magnitude;
        }
        return sum;
    }
}
