package ml;

import java.util.List;
import java.util.Vector;
import static java.lang.Math.max;

public class Normalizer extends UnsupervisedLearner {

    private List<Double> inputMins = new Vector<Double>();
    private List<Double> inputMaxs = new Vector<Double>();
    private Matrix template = new Matrix();

    /**
     * Computes the min and max of each column.
     */
    @Override
    public void train(Matrix data) {
        template.copyMetaData(data);
        for (int i = 0; i < data.getCols(); i++) {
            if (data.valueCount(i) == 0) {
                inputMins.add(data.columnMin(i));
                inputMaxs.add(max(inputMins.get(i) + 1e-9,
                                  data.columnMax(i)));
            } else {
                inputMins.add(Matrix.UNKNOWN_VALUE);
                inputMaxs.add(Matrix.UNKNOWN_VALUE);
            }
        }
    }

    /**
     * Normalizes continuous features.
     */
    @Override
    public void transform(List<Double> in, List<Double> out) {
        if (in.size() != inputMins.size()) {
            throw new MLException(String.format(
                    "Unexpected in-vector size. Expected: %d, Got: %d", inputMins.size(), in.size()));
        }
        for (int i = 0; i < in.size(); i++) {
            if (inputMins.get(i) == Matrix.UNKNOWN_VALUE) {
                out.add(in.get(i));
            } else {
                if (in.get(i) == Matrix.UNKNOWN_VALUE) {
                    out.add(Matrix.UNKNOWN_VALUE);
                } else {
                    double val = (in.get(i) - inputMins.get(i)) / (inputMaxs.get(i) - inputMins.get(i));
                    out.add(val);
                }
            }
        }
    }

    /**
     * De-normalizes continuous values.
     */
    @Override
    public void untransform(List<Double> in, List<Double> out) {
        if (in.size() != inputMins.size()) {
            throw new MLException(String.format(
                    "Unexpected in-vector size. Expected: %d, Got: %d", inputMins.size(), in.size()));
        }
        for (int i = 0; i < in.size(); i++) {
            if (inputMins.get(i) == Matrix.UNKNOWN_VALUE) {
                out.add(in.get(i));
            } else {
                if (in.get(i) == Matrix.UNKNOWN_VALUE) {
                    out.add(Matrix.UNKNOWN_VALUE);
                } else {
                    double val = in.get(i) * (inputMaxs.get(i) - inputMins.get(i)) + inputMins.get(i);
                    out.add(val);
                }
            }
        }
    }

    /**
     * Returns a zero-row matrix with the same column meta-data
     * as the one that was passed to train.
     */
    @Override
    public Matrix outputTemplate() {
        return template;
    }

}
