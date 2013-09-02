package ml;

import dataMining.ml.Matrix;

import java.util.List;
import java.util.Vector;

public class Filter extends SupervisedLearner {

    private SupervisedLearner pLearner;
    private UnsupervisedLearner pTransform;
    private boolean filterInputs;
    private List<Double> buffer;

    public Filter(SupervisedLearner pLearner, UnsupervisedLearner pTransform, boolean filterInputs) {
        this.pLearner = pLearner;
        this.pTransform = pTransform;
        this.filterInputs = filterInputs;
        this.buffer = new Vector<Double>();
    }

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
