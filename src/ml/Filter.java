package ml;

import dataMining.ml.Matrix;

import java.util.List;

public class Filter extends SupervisedLearner {

    private SupervisedLearner pLearner;
    private UnsupervisedLearner pTransform;
    private boolean filterInputs;
    private List<Double> buffer;

    public Filter(SupervisedLearner pLearner, UnsupervisedLearner pTransform, boolean filterInputs) {
        this.pLearner = pLearner;
        this.pTransform = pTransform;
        this.filterInputs = filterInputs;
    }

    @Override
    public void train(Matrix features, Matrix labels) {
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
}
