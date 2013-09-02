package ml;

import dataMining.ml.Matrix;

import java.util.List;

public abstract class UnsupervisedLearner {

    public abstract void train(Matrix data);

    public abstract Matrix outputTemplate();

    public abstract void transform(List<Double> in, List<Double> out);

    public abstract void untransform(List<Double> in, List<Double> out);
}
