package ml;

import java.util.List;
import java.util.Vector;

/**
 * Wraps another supervised learner. It applies some unsupervised operation
 * to the data before presenting it to the learner.
 */
public class Filter extends SupervisedLearner {

    private SupervisedLearner pLearner;
    private UnsupervisedLearner pTransform;
    private boolean filterInputs;
    private List<Double> buffer;

    /**
     * 	This takes ownership of pLearner and pTransform.
     * @param filterInputs if true, it applies the transform only the the input features.
     *                     if false, it applies the transform only the the output labels
     *                     (If you wish to transform both inputs and outputs, you must wrap a filter in a filter)
     */
    public Filter(SupervisedLearner pLearner, UnsupervisedLearner pTransform, boolean filterInputs) {
        this.pLearner = pLearner;
        this.pTransform = pTransform;
        this.filterInputs = filterInputs;
        this.buffer = new Vector<Double>();
    }

    /**
     * Trains the transform and the inner learner.
     */
    @Override
    public void train(Matrix features, Matrix labels) {
        if (features.getRows() != labels.getRows()) {
            throw new MLException("Features and labels must have the same number of rows.");
        }
        if (filterInputs) {
            Matrix matrix = trainAndTransform(features);
            pLearner.train(matrix, labels);

        } else {
            Matrix matrix = trainAndTransform(labels);
            pLearner.train(features, matrix);
        }
    }

    /**
     * Makes a prediction.
     */
    @Override
    public void predict(List<Double> in, List<Double> out) {
        if (filterInputs) {
            pTransform.transform(in, buffer);
            pLearner.predict(buffer, out);
        } else {
            pLearner.predict(in, buffer);
            pTransform.untransform(buffer, out);
        }
    }

    private Matrix trainAndTransform(Matrix data) {
        pTransform.train(data);
        Matrix matrix = new Matrix();
        matrix.copyMetaData(pTransform.outputTemplate());
        matrix.newRows(data.getRows());

        for (int i = 0; i < data.getRows(); i++) {
            pTransform.transform(data.row(i), matrix.row(i));
        }
        return matrix;
    }
}
