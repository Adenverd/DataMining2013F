package ml;

import java.util.List;

/**
 * @author Kanat Bekt
 * @date 9/1/2013
 */
public abstract class UnsupervisedLearner {

    public abstract void train(Matrix data);

    public abstract Matrix outputTemplate();

    public abstract void transform(List<Double> in, List<Double> out);

    public abstract void untransform(List<Double> in, List<Double> out);
}
